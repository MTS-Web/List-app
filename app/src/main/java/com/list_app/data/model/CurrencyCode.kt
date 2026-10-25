package com.list_app.data.model

@Suppress("unused")
enum class CurrencyCode(val code: String, val symbol: String, val displayName: String) {
    SYSTEM("SYSTEM", "", "Device Default"),
    USD("USD", "$", "US Dollar ($)"),
    EUR("EUR", "€", "Euro (€)"),
    GBP("GBP", "£", "British Pound (£)"),
    CAD("CAD", "CA$", "Canadian Dollar (CA$)"),
    AUD("AUD", "A$", "Australian Dollar (A$)"),
    JPY("JPY", "¥", "Japanese Yen (¥)"),
    INR("INR", "₹", "Indian Rupee (₹)"),
    BRL("BRL", "R$", "Brazilian Real (R$)"),
    MXN("MXN", "MX$", "Mexican Peso (MX$)")
}
