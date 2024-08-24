package com.betrybe.currencyview.data.models

data class Response<T>(
        var success: Boolean,
        var message: String?,
        var data: T?
)