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

    private var _currencySymbols = MutableLiveData<List<String>>()
    val currencySymbols: LiveData<List<String>>
        get() = _currencySymbols

    private var _currencyFullName = MutableLiveData<List<String>>()
    val currencyFullName: LiveData<List<String>>
        get() = _currencyFullName

    private var _currencyRates = MutableLiveData<Map<String, String>>()
    val currencyRates: LiveData<Map<String, String>>
        get() = _currencyRates

    private var _isErrorOccurred = MutableStateFlow(false)
    val isErrorOccurred: MutableStateFlow<Boolean>
        get() = _isErrorOccurred

    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    // Function to update currency symbols
    fun currenciesSymbols() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mCurrencyRepository.getCurrencySymbol()
            if (response.success) {
                val currency = response.data!!
                _currencySymbols.postValue(currency.currencyType.keys.toList())
                _currencyFullName.postValue(currency.currencyType.values.toList())
                isErrorOccurred.value = false
            } else {
                _errorMessage.postValue(response.message)
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
                isErrorOccurred.value = true
            }
        }
    }
}
