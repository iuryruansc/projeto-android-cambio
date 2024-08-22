package com.betrybe.currencyview.data.repositories

import androidx.lifecycle.MutableLiveData
import com.betrybe.currencyview.data.api.ApiService
import com.betrybe.currencyview.common.util.Result
import com.betrybe.currencyview.common.util.RetryHelper
import java.io.IOException

class CurrencyRepository(
    private val apiService: ApiService,
    private val currencyCodesLiveData: MutableLiveData<List<String>>
) {
    // Function to fetch currency symbols
    suspend fun fetchCurrenciesSymbols(): Result<List<String>> {
        return try {
            RetryHelper.retry {
                val response = apiService.getSymbols()

                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (responseData != null) {
                        currencyCodesLiveData.postValue(responseData.symbols.values.toList())
                        val symbols = responseData.symbols.keys.toList()

                        Result.Success(symbols)
                    } else {
                        // Handling null rates
                        Result.Failure(IOException("Symbols data is null"))
                    }
                } else {
                    // Handling API request failure
                    Result.Failure((IOException("API request failed")))
                }
            }
        } catch (e: Exception) {
            // Handling exceptions
            Result.Failure(e)
        }
    }

    // Function to fetch currency rates
    suspend fun fetchCurrencyRates(symbol: String): Result<Map<String, String>> {

        return try {
            RetryHelper.retry {
                val currencyRateResponse = apiService.getLatestRates(symbol)

                if (currencyRateResponse.isSuccessful) {
                    val rates = currencyRateResponse.body()?.rates
                    if (rates != null) {
                        Result.Success(rates)
                    } else {
                        // Handling null rates
                        Result.Failure(IOException("Rates data is null"))
                    }
                } else {
                    // Handling API request failure, IOException
                    Result.Failure((IOException("API request failed")))
                }
            }
        } catch (e: Exception) {
            // Handling exceptions
            Result.Failure(e)
        }
    }

}