package com.betrybe.currencyview.data.api.models

data class CurrencySymbolResponse (
    val success: Boolean,
    val symbols: Map<String, String>
)