package com.betrybe.currencyview.data.repository

import com.betrybe.currencyview.data.models.CurrencyType
import com.betrybe.currencyview.data.models.Response
import com.betrybe.currencyview.data.network.CurrencyDataSource
import com.betrye.currencyview.R

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
        return Response(false, R.string.error_message.toString(), null)
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
        return Response(false, R.string.error_message.toString(), null)
    }
}