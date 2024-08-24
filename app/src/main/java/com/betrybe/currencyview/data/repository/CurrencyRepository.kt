package com.betrybe.currencyview.data.repository

import com.betrybe.currencyview.data.models.CurrencyType
import com.betrybe.currencyview.data.models.Response
import com.betrybe.currencyview.data.network.CurrencyDataSource

class CurrencyRepository {

    private val mCurrencyDataSource = CurrencyDataSource()

    suspend fun getCurrencySymbol(): Response<CurrencyType> {
        try {
            val result = mCurrencyDataSource.fetchCurrenciesSymbols()
            if (result != null) {
                val currencySymbols = CurrencyType(
                    result.symbols
                )
                return Response(true, "", currencySymbols)
            }

        } catch (ex: Exception) {
            return Response(false, ex.message.orEmpty(), null)
        }
        return Response(false, "Um erro ocorrreu", null)
    }

    suspend fun getCurrencyRate(symbol: String): Response<CurrencyType> {
        try {
            val result = mCurrencyDataSource.fetchCurrencyRates(symbol)
            if (result != null) {
                val currencyRates = CurrencyType(
                    result.rates
                )
                return Response(true, "", currencyRates)
            }
        } catch (ex: Exception) {
            return Response(false, ex.message.orEmpty(), null)
        }
        return Response(false, "Um erro ocorrreu", null)
    }
}