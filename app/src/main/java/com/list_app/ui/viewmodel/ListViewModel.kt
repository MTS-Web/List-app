package com.list_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.list_app.data.model.AccentTheme
import com.list_app.data.model.ListItem
import com.list_app.data.model.ThemeConfig
import com.list_app.data.model.UserList
import com.list_app.data.model.UserListWithItemCount
import com.list_app.data.repository.ListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: ListRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.prepopulateSampleDataIfNeeded()
        }
    }

    @Suppress("unused")
    val userLists: StateFlow<List<UserList>> = repository.allLists
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val userListsWithCount: StateFlow<List<UserListWithItemCount>> = repository.allListsWithItemCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val themeConfig: StateFlow<ThemeConfig> = repository.themeConfig
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeConfig(),
        )

    private val itemsForListFlows = mutableMapOf<String, StateFlow<List<ListItem>>>()
    private val listByIdFlows = mutableMapOf<String, StateFlow<UserList?>>()

    fun getItemsForList(listId: String): StateFlow<List<ListItem>> {
        return itemsForListFlows.getOrPut(listId) {
            repository.getItemsForList(listId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList(),
                )
        }
    }

    fun getListById(listId: String): StateFlow<UserList?> {
        return listByIdFlows.getOrPut(listId) {
            repository.getListById(listId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null,
                )
        }
    }

    fun createList(title: String, description: String = "", coverImageUri: String? = null) {
        viewModelScope.launch {
            repository.createList(title, description, coverImageUri)
        }
    }

    fun updateList(list: UserList) {
        viewModelScope.launch {
            repository.updateList(list)
        }
    }

    fun updateListCoverUri(listId: String, coverImageUri: String?) {
        viewModelScope.launch {
            repository.updateListCoverUri(listId, coverImageUri)
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            repository.deleteList(listId)
        }
    }

    fun addItemToList(
        listId: String,
        name: String,
        description: String = "",
        price: Double = 0.0,
        imageUrl: String? = null,
        webUrl: String = "",
    ) {
        viewModelScope.launch {
            repository.addItemToList(listId, name, description, price, imageUrl, webUrl)
        }
    }

    fun updateItem(item: ListItem) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            repository.deleteItem(itemId)
        }
    }

    @Suppress("unused")
    fun updateThemeConfig(isDarkMode: Boolean, accentTheme: AccentTheme) {
        viewModelScope.launch {
            repository.updateThemeConfig(ThemeConfig(isDarkMode, accentTheme))
        }
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            repository.setDarkMode(isDarkMode)
        }
    }

    fun setAccentTheme(accentTheme: AccentTheme) {
        viewModelScope.launch {
            repository.setAccentTheme(accentTheme)
        }
    }

    fun setCustomAccentColorHex(hex: String?) {
        viewModelScope.launch {
            repository.setCustomAccentColorHex(hex)
        }
    }

    fun setCurrencyCode(code: String) {
        viewModelScope.launch {
            repository.setCurrencyCode(code)
        }
    }

    class Factory(private val repository: ListRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
                return ListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
