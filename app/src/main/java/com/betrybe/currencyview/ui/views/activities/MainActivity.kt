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
import com.betrybe.currencyview.ui.utils.RetryHelper
import com.betrybe.currencyview.ui.utils.DialogUtils
import com.betrybe.currencyview.ui.adapters.CurrencyArrayAdapter
import com.betrybe.currencyview.ui.adapters.CurrencyRatesAdapter
import com.betrye.currencyview.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val apiService = ApiServiceClient.instance
    private val currencyCodesLiveData = MutableLiveData<List<String>>()

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

        //Function to get the dropdown menu items
        fetchCurrenciesSymbols()

        //Function do get the currency rates for the selected item
        mSelectionLayout.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->

            mSelectCurrencyState.visibility = View.GONE
            mWaitingResponseState.visibility = View.VISIBLE
            fetchCurrenciesRates()

        }
    }

    private fun fetchCurrenciesSymbols() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetryHelper.retry {
                    ApiIdlingResource.increment()
                    val responseCurrencySymbol = apiService.getSymbols()

                    if (responseCurrencySymbol.isSuccessful) {
                        val responseData = responseCurrencySymbol.body()

                        if (responseData != null) {
                            currencyCodesLiveData.postValue(responseData.symbols.values.toList())
                            val currencyCodes = responseData.symbols.keys.toList()

                            withContext(Dispatchers.Main) {
                                val adapter = CurrencyArrayAdapter(
                                    this@MainActivity,
                                    R.layout.currency_item,
                                    currencyCodes
                                )
                                mSelectionLayout.setAdapter(adapter)

                                mLoadCurrencyState.visibility = View.GONE
                                mSelectCurrencyState.visibility = View.VISIBLE
                            }
                        } else {
                            //Handle empty
                        }
                    } else {
                        throw IOException("API request failed")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    DialogUtils.showSymbolsErrorDialog(this@MainActivity, e.message) {
                        fetchCurrenciesSymbols()
                    }
                }
            } finally {
                ApiIdlingResource.decrement()
            }
        }
    }

    private fun fetchCurrenciesRates() {
        val symbol = mSelectionLayout.text.toString() //Getting which item was selected
        val codes = currencyCodesLiveData.value!! //Getting the country name for the selected code

        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetryHelper.retry {
                    ApiIdlingResource.increment()
                    val currencyRateResponse = apiService.getLatestRates(symbol)

                    if (currencyRateResponse.isSuccessful) {
                        val rates = currencyRateResponse.body()?.rates
                        withContext(Dispatchers.Main) {
                            if (!rates.isNullOrEmpty()) {
                                val ratesAdapter = CurrencyRatesAdapter(rates, codes)
                                mCurrencyRatesState.adapter = ratesAdapter
                                ratesAdapter.notifyDataSetChanged()

                                mWaitingResponseState.visibility = View.GONE
                                mCurrencyRatesState.visibility = View.VISIBLE
                            } else {
                                //Handle empty
                            }
                        }
                    } else {
                        throw IOException("API request failed")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    DialogUtils.showCurrenciesErrorDialog(this@MainActivity, e.message) {
                        fetchCurrenciesRates()
                    }
                }
            } finally {
                ApiIdlingResource.decrement()
            }
        }
    }
}