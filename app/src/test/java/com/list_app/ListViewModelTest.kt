package com.list_app

import com.list_app.data.local.ListItemDao
import com.list_app.data.local.ThemePreferences
import com.list_app.data.local.UserListDao
import com.list_app.data.model.AccentTheme
import com.list_app.data.model.ListItem
import com.list_app.data.model.ThemeConfig
import com.list_app.data.model.ThemeMode
import com.list_app.data.model.UserList
import com.list_app.data.model.UserListWithItemCount
import com.list_app.data.repository.ListRepository
import com.list_app.ui.viewmodel.ListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeUserListDao : UserListDao {
        val lists = MutableStateFlow<List<UserList>>(emptyList())
        val listsWithCount = MutableStateFlow<List<UserListWithItemCount>>(emptyList())

        override fun getAllListsFlow(): Flow<List<UserList>> = lists
        override fun getAllListsWithItemCountFlow(): Flow<List<UserListWithItemCount>> = listsWithCount
        override fun getListByIdFlow(listId: String): Flow<UserList?> = MutableStateFlow(lists.value.find { it.id == listId })
        override suspend fun getListById(listId: String): UserList? = lists.value.find { it.id == listId }

        override suspend fun insertList(list: UserList) {
            val updated = lists.value.filter { it.id != list.id } + list
            lists.value = updated
            listsWithCount.value = updated.map { UserListWithItemCount(it, 0) }
        }

        override suspend fun updateList(list: UserList) {
            insertList(list)
        }

        override suspend fun updateListCoverUri(listId: String, coverImageUri: String?) {
            val existing = lists.value.find { it.id == listId }
            if (existing != null) {
                insertList(existing.copy(coverImageUri = coverImageUri))
            }
        }

        override suspend fun deleteList(list: UserList) {
            deleteListById(list.id)
        }

        override suspend fun deleteListById(listId: String) {
            val updated = lists.value.filter { it.id != listId }
            lists.value = updated
            listsWithCount.value = updated.map { UserListWithItemCount(it, 0) }
        }
    }

    private class FakeListItemDao : ListItemDao {
        val items = MutableStateFlow<List<ListItem>>(emptyList())

        override fun getItemsForListFlow(listId: String): Flow<List<ListItem>> {
            return MutableStateFlow(items.value.filter { it.listId == listId })
        }

        override fun getItemByIdFlow(itemId: String): Flow<ListItem?> {
            return MutableStateFlow(items.value.find { it.id == itemId })
        }

        override suspend fun getItemById(itemId: String): ListItem? {
            return items.value.find { it.id == itemId }
        }

        override suspend fun insertItem(item: ListItem) {
            items.value = items.value.filter { it.id != item.id } + item
        }

        override suspend fun updateItem(item: ListItem) {
            insertItem(item)
        }

        override suspend fun deleteItem(item: ListItem) {
            deleteItemById(item.id)
        }

        override suspend fun deleteItemById(itemId: String) {
            items.value = items.value.filter { it.listId != itemId }
        }

        override suspend fun deleteItemsForList(listId: String) {
            items.value = items.value.filter { it.listId != listId }
        }
    }

    private class FakeThemePreferences : ThemePreferences() {
        private var config = ThemeConfig()
        private var firstLaunchCompleted = false
        override val themeConfigFlow: Flow<ThemeConfig> = MutableStateFlow(config)
        override suspend fun updateThemeConfig(themeConfig: ThemeConfig) { config = themeConfig }
        override suspend fun setDarkMode(isDarkMode: Boolean) { config = config.copy(themeMode = if (isDarkMode) ThemeMode.DARK else ThemeMode.LIGHT) }
        override suspend fun setAccentTheme(accentTheme: AccentTheme) { config = config.copy(accentTheme = accentTheme) }
        override suspend fun isFirstLaunchCompleted(): Boolean = firstLaunchCompleted
        override suspend fun setFirstLaunchCompleted(completed: Boolean) { firstLaunchCompleted = completed }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testPrepopulateSampleDataOnlyOnce() = runTest {
        val userListDao = FakeUserListDao()
        val listItemDao = FakeListItemDao()
        val fakeThemePref = FakeThemePreferences()
        val repo = ListRepository(userListDao, listItemDao, fakeThemePref)

        // First call should prepopulate sample data
        repo.prepopulateSampleDataIfNeeded()
        testDispatcher.scheduler.advanceUntilIdle()

        val listsAfterFirst = userListDao.lists.value
        assertEquals(2, listsAfterFirst.size)

        // Delete one sample list ("Tech Wishlist")
        repo.deleteList("sample_list_1")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, userListDao.lists.value.size)

        // Second call (app restart simulation) should NOT re-populate sample data
        repo.prepopulateSampleDataIfNeeded()
        testDispatcher.scheduler.advanceUntilIdle()

        // Deleted list ("sample_list_1") should remain deleted (still 1 list remaining)
        assertEquals(1, userListDao.lists.value.size)
        assertEquals("Weekly Groceries", userListDao.lists.value[0].title)
    }

    @Test
    fun testCreateAndFetchList() = runTest {
        val userListDao = FakeUserListDao()
        val listItemDao = FakeListItemDao()
        val fakeThemePref = FakeThemePreferences()
        val repo = ListRepository(userListDao, listItemDao, fakeThemePref)
        val viewModel = ListViewModel(repo)

        viewModel.createList("My Vacation Wishlist", "Places to visit and pack", "file:///cover.jpg")
        testDispatcher.scheduler.advanceUntilIdle()

        val list = userListDao.lists.value.find { it.title == "My Vacation Wishlist" }
        assertNotNull(list)
        assertEquals("Places to visit and pack", list?.description)
        assertEquals("file:///cover.jpg", list?.coverImageUri)
    }

    @Test
    fun testAddItemAndQuery() = runTest {
        val userListDao = FakeUserListDao()
        val listItemDao = FakeListItemDao()
        val fakeThemePref = FakeThemePreferences()
        val repo = ListRepository(userListDao, listItemDao, fakeThemePref)
        val viewModel = ListViewModel(repo)

        viewModel.addItemToList(
            listId = "list_100",
            name = "Camera Lens",
            description = "50mm f/1.8 lens",
            price = 125.0,
            imageUrl = "file:///lens.jpg",
            webUrl = "https://example.com/lens"
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val item = listItemDao.items.value.find { it.name == "Camera Lens" }
        assertNotNull(item)
        assertEquals(125.0, item?.price ?: 0.0, 0.001)
        assertEquals("https://example.com/lens", item?.webUrl)
    }

    @Test
    fun testUpdateAndDeleteList() = runTest {
        val userListDao = FakeUserListDao()
        val listItemDao = FakeListItemDao()
        val fakeThemePref = FakeThemePreferences()
        val repo = ListRepository(userListDao, listItemDao, fakeThemePref)
        val viewModel = ListViewModel(repo)

        val list = repo.createList("Original Title", "Desc", "file:///old.jpg")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateList(list.copy(title = "Updated Title"))
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = userListDao.lists.value.find { it.id == list.id }
        assertEquals("Updated Title", updated?.title)

        viewModel.deleteList(list.id)
        testDispatcher.scheduler.advanceUntilIdle()

        val deleted = userListDao.lists.value.find { it.id == list.id }
        assertEquals(null, deleted)
    }
}
