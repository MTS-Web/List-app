package com.list_app

import com.list_app.util.ImageUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageUtilsTest {

    @Test
    fun testNormalizeUrlWithHttp() {
        val input = "http://example.com"
        val result = ImageUtils.normalizeUrl(input)
        assertEquals("http://example.com", result)
    }

    @Test
    fun testNormalizeUrlWithHttps() {
        val input = "https://example.com/product/123"
        val result = ImageUtils.normalizeUrl(input)
        assertEquals("https://example.com/product/123", result)
    }

    @Test
    fun testNormalizeUrlWithoutScheme() {
        val input = "example.com/item"
        val result = ImageUtils.normalizeUrl(input)
        assertEquals("https://example.com/item", result)
    }

    @Test
    fun testNormalizeUrlWithSpaces() {
        val input = "   github.com/kotlin   "
        val result = ImageUtils.normalizeUrl(input)
        assertEquals("https://github.com/kotlin", result)
    }

    @Test
    fun testNormalizeUrlEmpty() {
        val result = ImageUtils.normalizeUrl("   ")
        assertEquals("", result)
    }
}
