package com.currencyview.data.network

import com.currencyview.data.api.ApiService
import com.currencyview.data.api.ApiServiceClient
import com.currencyview.data.api.models.CurrencyRateResponse
import com.currencyview.data.api.models.CurrencySymbolResponse

class CurrencyDataSource(private val apiService: ApiService = ApiServiceClient.instance) {

    // Function to fetch currency symbols
    suspend fun fetchCurrenciesSymbols(): CurrencySymbolResponse? {
        val currencySymbolResponse = apiService.getSymbols()
        return currencySymbolResponse.body()
    }

    // Function to fetch currency rates
    suspend fun fetchCurrencyRates(symbol: String): CurrencyRateResponse? {
        val currencyRateResponse = apiService.getLatestRates(symbol)
        return currencyRateResponse.body()
    }
}
