package com.list_app

import com.list_app.data.model.AccentTheme
import com.list_app.data.model.ListItem
import com.list_app.data.model.ThemeConfig
import com.list_app.data.model.UserList
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class ListDomainTest {

    @Test
    fun testUserListDataModel() {
        val id = UUID.randomUUID().toString()
        val list = UserList(
            id = id,
            title = "Shopping List",
            description = "Groceries to buy",
            coverImageUri = "content://media/external/images/media/123",
            createdAt = 1000L
        )

        assertEquals(id, list.id)
        assertEquals("Shopping List", list.title)
        assertEquals("Groceries to buy", list.description)
        assertEquals("content://media/external/images/media/123", list.coverImageUri)
        assertEquals(1000L, list.createdAt)
    }

    @Test
    fun testListItemDataModel() {
        val id = UUID.randomUUID().toString()
        val listId = UUID.randomUUID().toString()
        val item = ListItem(
            id = id,
            listId = listId,
            name = "Headphones",
            description = "Bluetooth noise cancelling",
            price = 199.99,
            imageUrl = "https://example.com/item.jpg",
            webUrl = "https://example.com",
            createdAt = 2000L
        )

        assertEquals(id, item.id)
        assertEquals(listId, item.listId)
        assertEquals("Headphones", item.name)
        assertEquals("Bluetooth noise cancelling", item.description)
        assertEquals(199.99, item.price, 0.001)
        assertEquals("https://example.com/item.jpg", item.imageUrl)
        assertEquals("https://example.com", item.webUrl)
        assertEquals(2000L, item.createdAt)
    }

    @Test
    fun testThemeConfigDataModel() {
        val config = ThemeConfig(isDarkMode = true, accentTheme = AccentTheme.CORAL)
        assertEquals(true, config.isDarkMode)
        assertEquals(AccentTheme.CORAL, config.accentTheme)
        assertEquals("Coral", config.accentTheme.displayName)

        val customConfig = ThemeConfig(accentTheme = AccentTheme.TEAL, customAccentColorHex = "#3F51B5")
        assertEquals("#3F51B5", customConfig.customAccentColorHex)
    }

    @Test
    fun testAccentThemes() {
        val themes = AccentTheme.entries
        assertEquals(12, themes.size)
        assertEquals(AccentTheme.TEAL, themes[0])
        assertEquals(AccentTheme.PURPLE, themes[1])
        assertEquals(AccentTheme.CORAL, themes[2])
        assertEquals(AccentTheme.OCEAN, themes[3])
        assertEquals(AccentTheme.SUNSET, themes[4])
        assertEquals(AccentTheme.EMERALD, themes[5])
        assertEquals(AccentTheme.AMBER, themes[6])
        assertEquals(AccentTheme.ROSE, themes[7])
        assertEquals(AccentTheme.INDIGO, themes[8])
        assertEquals(AccentTheme.CYAN, themes[9])
        assertEquals(AccentTheme.LIME, themes[10])
        assertEquals(AccentTheme.VIOLET, themes[11])
    }
}
