package com.currencyview.data.models

data class CurrencyResponse<T>(
        var success: Boolean,
        var message: String?,
        var data: T?
)
