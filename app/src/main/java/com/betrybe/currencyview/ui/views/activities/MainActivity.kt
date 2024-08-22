package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.betrybe.currencyview.ui.adapters.CurrencyArrayAdapter
import com.betrybe.currencyview.ui.adapters.CurrencyRatesAdapter
import com.betrybe.currencyview.ui.viewmodels.MainActivityViewModel
import com.betrybe.currencyview.common.util.DialogBoxUtils
import com.betrye.currencyview.R
import com.betrye.currencyview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private var selectedSymbol: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) //Inflating the layout
        setContentView(binding.root) //Setting the content view

        binding.loadCurrencyState.visibility = View.VISIBLE
        binding.currencyRatesState.layoutManager = LinearLayoutManager(this)

        viewModel.currenciesSymbols()

        //Observing the currency symbols live data from view model
        viewModel.currencyCodesLiveData.observe(this) { currencyCodes ->
            updateDropDownMenu(currencyCodes)
        }

        //Observing the currency rates live data from view model
        viewModel.currencyRatesLiveData.observe(this) { currencyRates ->
           if (currencyRates != null ) {
               val codes = viewModel.currencyCodesLiveData.value!!
               updateRecyclerView(currencyRates, codes)
           }
        }

        //Observing the symbols error event from view model
        viewModel.symbolErrorLiveData.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showSymbolsErrorDialog(errorMessage)
            }
        }

        //Observing the currency rates error event from view model
        viewModel.currencyRatesErrorLiveData.observe(this) { errorMessage ->
            if ( errorMessage != null) {
                showCurrenciesErrorDialog(errorMessage)
            }
        }

        //Apply the function do get the currency rates to the menu items
        binding.currencySelectionInputLayout.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                selectedSymbol = binding.currencySelectionInputLayout.text.toString()

                binding.selectCurrencyState.visibility = View.GONE
                binding.waitingResponseState.visibility = View.VISIBLE
                viewModel.currenciesRates(selectedSymbol)
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
        { viewModel.currenciesSymbols() }
    }

    //Creating the try/cancel dialog box to the dropdown items
    private val showCurrenciesErrorDialog: (String?) -> Unit = { errorMessage ->
        DialogBoxUtils.showCurrenciesErrorDialog(this, errorMessage)
        { viewModel.currenciesRates(selectedSymbol) }
    }
}