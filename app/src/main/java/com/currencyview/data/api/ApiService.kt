package com.currencyview.data.api

import com.currencyview.data.api.models.CurrencySymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("symbols")
    suspend fun  getSymbols (): Response<CurrencySymbolResponse>

    @GET("latest")
    suspend fun getLatestRates (
        @Query("base") base: String
    ): Response<com.currencyview.data.api.models.CurrencyRateResponse>

}
