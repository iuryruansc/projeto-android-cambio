package com.currencyview.data.repository

import com.currencyview.R
import com.currencyview.common.util.testing.EspressoIdlingResourceHelper
import com.currencyview.common.util.testing.IdlingResourceHelper
import com.currencyview.data.models.CurrencyResponse
import com.currencyview.data.models.CurrencyType
import com.currencyview.data.network.CurrencyDataSource

class CurrencyRepository(private val mCurrencyDataSource: CurrencyDataSource = CurrencyDataSource(),
                         private val mIdlingResourceHelper: IdlingResourceHelper = EspressoIdlingResourceHelper()
) {

    suspend fun getCurrencySymbol(): CurrencyResponse<CurrencyType> {
        try {
            mIdlingResourceHelper.increment()
            val result = mCurrencyDataSource.fetchCurrenciesSymbols()
            if (result != null) {
                val currencySymbols = CurrencyType(
                    result.symbols!!
                )
                return CurrencyResponse(true, "", currencySymbols)
            }
        } catch (ex: Exception) {
            return CurrencyResponse(false, ex.message.orEmpty(), null)
        } finally {
            mIdlingResourceHelper.decrement()
        }
        return CurrencyResponse(false, R.string.error_message.toString(), null)
    }

    suspend fun getCurrencyRate(symbol: String): CurrencyResponse<CurrencyType> {
        try {
            mIdlingResourceHelper.increment()
            val result = mCurrencyDataSource.fetchCurrencyRates(symbol)
            if (result != null) {
                val currencyRates = CurrencyType(
                    result.rates!!
                )
                return CurrencyResponse(true, "", currencyRates)
            }
        } catch (ex: Exception) {
            return CurrencyResponse(false, ex.message.orEmpty(), null)
        } finally {
            mIdlingResourceHelper.decrement()
        }
        return CurrencyResponse(false, R.string.error_message.toString(), null)
    }
}
