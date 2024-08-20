package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiServiceClient
import com.betrybe.currencyview.data.repositories.CurrencyRepository
import com.betrybe.currencyview.ui.adapters.CurrencyArrayAdapter
import com.betrybe.currencyview.ui.adapters.CurrencyRatesAdapter
import com.betrybe.currencyview.utils.DialogBoxUtils
import com.betrybe.currencyview.utils.Result
import com.betrye.currencyview.R
import com.betrye.currencyview.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apiService = ApiServiceClient.instance
    private val currencyCodesLiveData = MutableLiveData<List<String>>()
    private val currencyRepository = CurrencyRepository(apiService, currencyCodesLiveData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadCurrencyState.visibility = View.VISIBLE
        binding.currencyRatesState.layoutManager = LinearLayoutManager(this)

        //Start the function to get the dropdown menu items
        currenciesSymbols()

        //Apply the function do get the currency rates to the menu items
        binding.currencySelectionInputLayout.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->

            binding.selectCurrencyState.visibility = View.GONE
            binding.waitingResponseState.visibility = View.VISIBLE
            currenciesRates()
        }
    }


    private fun currenciesSymbols() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                //Getting the symbols from the API
                val result = currencyRepository.fetchCurrenciesSymbols()

                //Switching to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Success -> {
                            updateDropDownMenu(result.data)
                        }

                        is Result.Failure -> {
                            showSymbolsErrorDialog(result.exception.message)
                        }
                    }
                }
            } finally {
                ApiIdlingResource.decrement()
            }
        }
    }

    private fun currenciesRates() {
        val symbol = binding.currencySelectionInputLayout.text.toString() //Getting which item was selected
        val codes = currencyCodesLiveData.value!! //Getting the country name for the menu items

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                //Getting the rates from the API
                val result = currencyRepository.fetchCurrencyRates(symbol)

                //Switching to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Success -> {
                            updateRecyclerView(result.data, codes)
                        }

                        is Result.Failure -> {
                            showCurrenciesErrorDialog(result.exception.message)
                        }
                    }
                }
            } finally {
                ApiIdlingResource.decrement()
            }
        }
    }

    //Updating the dropdown menu with the currency codes
    private fun updateDropDownMenu(currencyCodes: List<String>) {
        val adapter = CurrencyArrayAdapter(
            this@MainActivity,
            R.layout.currency_item,
            currencyCodes
        )
        binding.currencySelectionInputLayout.setAdapter(adapter)

        binding.loadCurrencyState.visibility = View.GONE
        binding.selectCurrencyState.visibility = View.VISIBLE
    }

    //Updating the recycler view with the currency rates
    private fun updateRecyclerView(rates: Map<String, String>, codes: List<String>) {
        val ratesAdapter = CurrencyRatesAdapter(rates, codes)
        binding.currencyRatesState.adapter = ratesAdapter
        ratesAdapter.notifyDataSetChanged()

        binding.waitingResponseState.visibility = View.GONE
        binding.currencyRatesState.visibility = View.VISIBLE
    }

    //Creating the try/cancel dialog box at the start of the app
    private val showSymbolsErrorDialog: (String?) -> Unit = { errorMessage ->
        DialogBoxUtils.showSymbolsErrorDialog(this, errorMessage)
        { currenciesSymbols() }
    }

    //Creating the try/cancel dialog box to the dropdown items
    private val showCurrenciesErrorDialog: (String?) -> Unit = { errorMessage ->
        DialogBoxUtils.showCurrenciesErrorDialog(this, errorMessage)
        { currenciesRates() }
    }
}