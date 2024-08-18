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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val mApiService = ApiServiceClient.instance
    private val mSelectionLayout: MaterialAutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val mLoadCurrencyState: MaterialTextView by lazy {findViewById(R.id.load_currency_state)}
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

    private fun fetchCurrenciesSymbols() {
        CoroutineScope(Dispatchers.IO).launch {
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
            } catch (e: HttpException) {
                //Handle error
                ApiIdlingResource.decrement()
            } catch (e: IOException) {
                runOnUiThread {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Erro")
                        .setMessage("Falha ao carregar moedas... ${e.message}")
                        .setPositiveButton("Retry") {dialog, _ ->
                            dialog.dismiss()
                            fetchCurrenciesSymbols()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .show()
                }
                ApiIdlingResource.decrement()
            }
        }
    }

    private fun fetchCurrenciesRates() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                val symbol = mSelectionLayout.text.toString()
                val responseCurrencyRate = mApiService.getLatestRates(symbol)

                val codes = currencyCodesLiveData.value!!

                if (responseCurrencyRate.isSuccessful) {
                    val rates= responseCurrencyRate.body()?.rates
                    runOnUiThread {
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
                    //Handle error
                }

                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                //Handle error
                ApiIdlingResource.decrement()
            } catch (e: IOException) {
                runOnUiThread {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Erro")
                        .setMessage("Falha ao carregar conversões da moeda escolhida... ${e.message}")
                        .setPositiveButton("Retry") {dialog, _ ->
                            dialog.dismiss()
                            fetchCurrenciesSymbols()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .show()
                }
                ApiIdlingResource.decrement()
            }
        }
    }
}