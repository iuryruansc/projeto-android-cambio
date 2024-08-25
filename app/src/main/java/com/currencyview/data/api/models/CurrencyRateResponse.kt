package com.currencyview.data.api.models

data class CurrencyRateResponse (
    val success: Boolean,
    val base: String,
    val date: String,
    val rates: Map<String, String>
)
