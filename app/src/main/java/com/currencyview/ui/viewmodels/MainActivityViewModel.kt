package com.currencyview.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.currencyview.data.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val mCurrencyRepository = CurrencyRepository()

    private var _currencySymbols = MutableLiveData<Map<String, String>>()
    val currencySymbols: LiveData<Map<String, String>>
        get() = _currencySymbols

    private var _currencyRates = MutableLiveData<Map<String, String>>()
    val currencyRates: LiveData<Map<String, String>>
        get() = _currencyRates

    private var _errorType = MutableLiveData("")
    val errorType: LiveData<String>
        get() = _errorType

    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _isErrorOccurred = MutableStateFlow(false)
    val isErrorOccurred: MutableStateFlow<Boolean>
        get() = _isErrorOccurred


    // Function to update currency symbols
    fun currenciesSymbols() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mCurrencyRepository.getCurrencySymbol()
            if (response.success) {
                val currency = response.data!!
                _currencySymbols.postValue(currency.currencyType)
                isErrorOccurred.value = false
            } else {
                _errorMessage.postValue(response.message)
                _errorType.postValue("symbol")
                isErrorOccurred.value = true
            }
        }
    }

    // Function to update currency rates
    fun currenciesRates(symbol: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mCurrencyRepository.getCurrencyRate(symbol)
            if (response.success) {
                val rates = response.data!!
                _currencyRates.postValue(rates.currencyType)
                isErrorOccurred.value = false
            } else {
                _errorMessage.postValue(response.message)
                _errorType.postValue("rates")
                isErrorOccurred.value = true
            }
        }
    }
}
