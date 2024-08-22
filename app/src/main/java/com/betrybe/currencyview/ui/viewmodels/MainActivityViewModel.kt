package com.betrybe.currencyview.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.betrybe.currencyview.common.idling.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiServiceClient
import com.betrybe.currencyview.data.repositories.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.betrybe.currencyview.common.util.Result

class MainActivityViewModel: ViewModel() {

    private val apiService = ApiServiceClient.instance

    private var _currencyCodesLiveData = MutableLiveData<List<String>>()
    val currencyCodesLiveData: LiveData<List<String>>
        get() = _currencyCodesLiveData

    private var _symbolErrorLiveData = MutableLiveData<String?>()
    val symbolErrorLiveData: LiveData<String?>
        get() = _symbolErrorLiveData

    private var _currencyRatesErrorLiveData = MutableLiveData<String?>()
    val currencyRatesErrorLiveData: LiveData<String?>
        get() = _currencyRatesErrorLiveData

    private var _currencyRatesLiveData = MutableLiveData<Map<String, String>>()
    val currencyRatesLiveData: LiveData<Map<String, String>>
        get() = _currencyRatesLiveData

    private val currencyRepository = CurrencyRepository(apiService, _currencyCodesLiveData)

    fun currenciesSymbols() {
       CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()
                when (val result = currencyRepository.fetchCurrenciesSymbols()) {
                    is Result.Success -> _currencyCodesLiveData.postValue(result.data)
                    is Result.Failure -> _symbolErrorLiveData.postValue(result.exception.message)
                }
            } finally {
                ApiIdlingResource.decrement()
            }
        }
    }

    fun currenciesRates(symbol: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()
                when (val result = currencyRepository.fetchCurrencyRates(symbol)) {
                    is Result.Success -> _currencyRatesLiveData.postValue(result.data)
                    is Result.Failure -> _currencyRatesErrorLiveData.postValue(result.exception.message)
                }
            } finally {
                ApiIdlingResource.decrement()
            }
        }
    }

}