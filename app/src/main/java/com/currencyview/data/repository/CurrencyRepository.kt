package com.currencyview.data.repository

import com.betrye.currencyview.R
import com.currencyview.data.models.CurrencyType
import com.currencyview.data.models.Response
import com.currencyview.data.network.CurrencyDataSource

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
