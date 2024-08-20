package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiServiceClient
import com.betrybe.currencyview.data.repositories.CurrencyRepository
import com.betrybe.currencyview.ui.adapters.CurrencyArrayAdapter
import com.betrybe.currencyview.ui.adapters.CurrencyRatesAdapter
import com.betrybe.currencyview.utils.DialogBoxUtils
import com.betrybe.currencyview.utils.Result
import com.betrye.currencyview.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val apiService = ApiServiceClient.instance
    private val currencyCodesLiveData = MutableLiveData<List<String>>()
    private val currencyRepository = CurrencyRepository(apiService, currencyCodesLiveData)

    //Layout Views
    private val mSelectionLayout: MaterialAutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val mLoadCurrencyState: MaterialTextView by lazy { findViewById(R.id.load_currency_state) }
    private val mSelectCurrencyState: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }
    private val mWaitingResponseState: FrameLayout by lazy { findViewById(R.id.waiting_response_state) }
    private val mCurrencyRatesState: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLoadCurrencyState.visibility = View.VISIBLE
        mCurrencyRatesState.layoutManager = LinearLayoutManager(this)

        //Start the function to get the dropdown menu items
        currenciesSymbols()

        //Apply the function do get the currency rates to the menu items
        mSelectionLayout.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->

            mSelectCurrencyState.visibility = View.GONE
            mWaitingResponseState.visibility = View.VISIBLE
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
        val symbol = mSelectionLayout.text.toString() //Getting which item was selected
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
        mSelectionLayout.setAdapter(adapter)

        mLoadCurrencyState.visibility = View.GONE
        mSelectCurrencyState.visibility = View.VISIBLE
    }

    //Updating the recycler view with the currency rates
    private fun updateRecyclerView(rates: Map<String, String>, codes: List<String>) {
        val ratesAdapter = CurrencyRatesAdapter(rates, codes)
        mCurrencyRatesState.adapter = ratesAdapter
        ratesAdapter.notifyDataSetChanged()

        mWaitingResponseState.visibility = View.GONE
        mCurrencyRatesState.visibility = View.VISIBLE
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