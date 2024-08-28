package com.currencyview

import com.currencyview.data.api.models.CurrencyRateResponse
import com.currencyview.data.api.models.CurrencySymbolResponse
import com.currencyview.data.models.CurrencyResponse
import com.currencyview.data.models.CurrencyType

object CurrencyTestMock {

    // TEST SYMBOL EXIST
    val currencySymbolMockSuccess = CurrencySymbolResponse(
        (true),
        mapOf("USD" to "United States Dollar")
    )

    val symbolResponseMockExist = CurrencyResponse(
        true, "",
        CurrencyType(mapOf("USD" to "United States Dollar"))
    )

    // TEST SYMBOL NULL
    val currencySymbolMockSuccessNull = CurrencySymbolResponse(
        (true),
        null
    )

    val symbolResponseMockNull = CurrencyResponse(
        false, "",
        null
    )

    // TEST SYMBOL ERROR
    val symbolResponseMockNotExist = CurrencyResponse(
        false, "Error message",
        null
    )

    // TEST RATES EXIST
    val currencyRatesMockSuccess = CurrencyRateResponse(
        (true),
        "USD",
        mapOf("EUR" to "0.813399")
    )

    val rateResponseMockExist = CurrencyResponse(
        true, "",
        CurrencyType(mapOf("EUR" to "0.813399"))
    )

    // TEST RATES NULL
    val currencyRatesMockSuccessNull = CurrencyRateResponse(
        (true),
        "USD",
        null
    )

    val rateResponseMockNull = CurrencyResponse(
        false, "", null
    )

    // TEST RATES ERROR
    val rateResponseMockNotExist = CurrencyResponse(
        false, "Error message", null
    )
}
