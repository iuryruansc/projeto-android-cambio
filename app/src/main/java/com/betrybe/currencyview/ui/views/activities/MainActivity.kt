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
import com.betrybe.currencyview.ui.adapters.CurrencyArrayAdapter
import com.betrybe.currencyview.ui.adapters.CurrencyRatesAdapter
import com.betrye.currencyview.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val mApiService = ApiServiceClient.instance
    private val mSelectionLayout: MaterialAutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val mLoadCurrencyState: MaterialTextView by lazy { findViewById(R.id.load_currency_state) }
    private val mSelectCurrencyState: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }
    private val mWaitingResponseState: FrameLayout by lazy { findViewById(R.id.waiting_response_state) }
    private val mCurrencyRatesState: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }
    private val currencyCodesLiveData = MutableLiveData<List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLoadCurrencyState.visibility = View.VISIBLE
        mCurrencyRatesState.layoutManager = LinearLayoutManager(this)

        fetchCurrenciesSymbols()


        mSelectionLayout.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->

            mSelectCurrencyState.visibility = View.GONE
            mWaitingResponseState.visibility = View.VISIBLE
            fetchCurrenciesRates()

        }
    }

    private fun fetchCurrenciesSymbols(retryCount: Int = 3) {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(retryCount) { attempt ->
                try {
                    ApiIdlingResource.increment()

                    val responseCurrencySymbol = mApiService.getSymbols()

                    if (responseCurrencySymbol.isSuccessful) {
                        val responseData = responseCurrencySymbol.body()!!
                        currencyCodesLiveData.postValue(responseData.symbols.values.toList())
                        val currencyCodes = responseData.symbols.keys.toList()

                        runOnUiThread {
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
                        //Handle error
                    }

                    ApiIdlingResource.decrement()
                    return@repeat
                } catch (e: HttpException) {
                    ApiIdlingResource.decrement()
                    //Handle error
                } catch (e: IOException) {
                    ApiIdlingResource.decrement()
                    if (attempt < retryCount - 1) {
                        val delayMillis = (pow(
                            2, (attempt + 1)
                                .coerceAtLeast(1)
                        ) * 1000L).coerceAtMost(5000L)
                        delay(delayMillis)
                    } else {
                        withContext(Dispatchers.Main) {
                            showSymbolsErrorDialog(e.message)
                        }
                    }
                }
            }
        }
    }

    private fun fetchCurrenciesRates(retryCount: Int = 3) {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(retryCount) { attempt ->
                try {
                    ApiIdlingResource.increment()

                    val symbol = mSelectionLayout.text.toString()
                    val responseCurrencyRate = mApiService.getLatestRates(symbol)

                    val codes = currencyCodesLiveData.value!!

                    if (responseCurrencyRate.isSuccessful) {
                        val rates = responseCurrencyRate.body()?.rates
                        runOnUiThread {
                            if (!rates.isNullOrEmpty()) {

                                val ratesAdapter = CurrencyRatesAdapter(rates, codes)
                                mCurrencyRatesState.adapter = ratesAdapter
                                ratesAdapter.notifyDataSetChanged()

                                mWaitingResponseState.visibility = View.GONE
                                mCurrencyRatesState.visibility = View.VISIBLE
                                return@runOnUiThread
                            } else {
                                //Handle empty
                            }
                        }
                    } else {
                        //Handle error
                    }
                    ApiIdlingResource.decrement()
                } catch (e: HttpException) {
                    ApiIdlingResource.decrement()
                    //Handle error
                } catch (e: IOException) {
                    ApiIdlingResource.decrement()
                    if (attempt < retryCount - 1) {
                        val delayMillis = (pow(
                            2, (attempt + 1)
                                .coerceAtLeast(1)
                        ) * 1000L).coerceAtMost(5000L)
                        delay(delayMillis)
                    } else {
                        withContext(Dispatchers.Main) {
                            showCurrenciesErrorDialog(e.message)
                        }
                    }
                }
            }
        }
    }

    private fun showCurrenciesErrorDialog(errorMessage: String?) {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle("Erro")
            .setMessage("Falha ao carregar conversões da moeda escolhida... $errorMessage")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                fetchCurrenciesRates()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }

    private fun showSymbolsErrorDialog(errorMessage: String?) {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle("Error")
            .setMessage("Failed to load currencies... $errorMessage")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                fetchCurrenciesSymbols()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }

    private fun pow(base: Long, exponent: Int): Long {
        if (exponent == 0) return 1
        var result = base
        for (i in 2..exponent) {
            result *= base
        }
        return result
    }
}