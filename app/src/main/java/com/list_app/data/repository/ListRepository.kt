package com.list_app.data.repository

import com.list_app.data.local.ListItemDao
import com.list_app.data.local.ThemePreferences
import com.list_app.data.local.UserListDao
import com.list_app.data.model.AccentTheme
import com.list_app.data.model.ListItem
import com.list_app.data.model.ThemeConfig
import com.list_app.data.model.ThemeMode
import com.list_app.data.model.UserList
import com.list_app.data.model.UserListWithItemCount
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ListRepository(
    private val userListDao: UserListDao,
    private val listItemDao: ListItemDao,
    private val themePreferences: ThemePreferences
) {
    val allLists: Flow<List<UserList>> = userListDao.getAllListsFlow()
    val allListsWithItemCount: Flow<List<UserListWithItemCount>> = userListDao.getAllListsWithItemCountFlow()
    val themeConfig: Flow<ThemeConfig> = themePreferences.themeConfigFlow

    fun getListById(listId: String): Flow<UserList?> {
        return userListDao.getListByIdFlow(listId)
    }

    fun getItemsForList(listId: String): Flow<List<ListItem>> {
        return listItemDao.getItemsForListFlow(listId)
    }

    suspend fun createList(title: String, description: String = "", coverImageUri: String? = null): UserList {
        val newList = UserList(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            coverImageUri = coverImageUri,
            createdAt = System.currentTimeMillis()
        )
        userListDao.insertList(newList)
        return newList
    }

    suspend fun updateList(list: UserList) {
        userListDao.updateList(list)
    }

    suspend fun updateListCoverUri(listId: String, coverImageUri: String?) {
        userListDao.updateListCoverUri(listId, coverImageUri)
    }

    suspend fun deleteList(listId: String) {
        userListDao.deleteListById(listId)
    }

    suspend fun addItemToList(
        listId: String,
        name: String,
        description: String = "",
        price: Double = 0.0,
        imageUrl: String? = null,
        webUrl: String = ""
    ): ListItem {
        val newItem = ListItem(
            id = UUID.randomUUID().toString(),
            listId = listId,
            name = name,
            description = description,
            price = price,
            imageUrl = imageUrl,
            webUrl = webUrl,
            createdAt = System.currentTimeMillis()
        )
        listItemDao.insertItem(newItem)
        return newItem
    }

    suspend fun updateItem(item: ListItem) {
        listItemDao.updateItem(item)
    }

    suspend fun deleteItem(itemId: String) {
        listItemDao.deleteItemById(itemId)
    }

    @Suppress("unused")
    suspend fun updateThemeConfig(themeConfig: ThemeConfig) {
        themePreferences.updateThemeConfig(themeConfig)
    }

    @Suppress("unused")
    suspend fun setThemeMode(themeMode: ThemeMode) {
        themePreferences.setThemeMode(themeMode)
    }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        themePreferences.setDarkMode(isDarkMode)
    }

    suspend fun setAccentTheme(accentTheme: AccentTheme) {
        themePreferences.setAccentTheme(accentTheme)
    }

    @Suppress("unused")
    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        themePreferences.setUseDynamicColor(useDynamicColor)
    }

    suspend fun setCustomAccentColorHex(hex: String?) {
        themePreferences.setCustomAccentColorHex(hex)
    }

    suspend fun setCurrencyCode(code: String) {
        themePreferences.setCurrencyCode(code)
    }

    suspend fun prepopulateSampleDataIfNeeded() {
        val isCompleted = themePreferences.isFirstLaunchCompleted()
        if (!isCompleted) {
            val wishlist = UserList(
                id = "sample_list_1",
                title = "Tech Wishlist",
                description = "Gadgets and accessories I want to check out",
                coverImageUri = null,
                createdAt = System.currentTimeMillis() - 86400000 * 2
            )
            userListDao.insertList(wishlist)

            listItemDao.insertItem(
                ListItem(
                    id = UUID.randomUUID().toString(),
                    listId = "sample_list_1",
                    name = "Wireless Noise-Canceling Headphones",
                    description = "Over-ear bluetooth headphones with active noise cancellation",
                    price = 299.99,
                    imageUrl = null,
                    webUrl = "https://example.com/headphones",
                    createdAt = System.currentTimeMillis() - 86400000
                )
            )
            listItemDao.insertItem(
                ListItem(
                    id = UUID.randomUUID().toString(),
                    listId = "sample_list_1",
                    name = "Smart Monitor 32-inch",
                    description = "4K display with streaming apps built-in",
                    price = 399.50,
                    imageUrl = null,
                    webUrl = "https://example.com/monitor",
                    createdAt = System.currentTimeMillis() - 43200000
                )
            )

            val groceryList = UserList(
                id = "sample_list_2",
                title = "Weekly Groceries",
                description = "Essentials to buy for the week",
                coverImageUri = null,
                createdAt = System.currentTimeMillis() - 86400000
            )
            userListDao.insertList(groceryList)

            listItemDao.insertItem(
                ListItem(
                    id = UUID.randomUUID().toString(),
                    listId = "sample_list_2",
                    name = "Organic Almond Milk",
                    description = "Unsweetened 1 Gallon",
                    price = 4.49,
                    imageUrl = null,
                    webUrl = "",
                    createdAt = System.currentTimeMillis() - 3600000
                )
            )

            themePreferences.setFirstLaunchCompleted(true)
        }
    }
}
