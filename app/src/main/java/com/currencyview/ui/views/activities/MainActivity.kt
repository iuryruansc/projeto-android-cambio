package com.currencyview.ui.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.betrye.currencyview.R
import com.betrye.currencyview.databinding.ActivityMainBinding
import com.currencyview.common.util.DialogBoxUtils
import com.currencyview.ui.adapters.CurrencyArrayAdapter
import com.currencyview.ui.adapters.CurrencyRatesAdapter
import com.currencyview.ui.viewmodels.MainActivityViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private var selectedCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) //Inflating the layout
        setContentView(binding.root) //Setting the content view

        binding.loadCurrencyState.visibility = View.VISIBLE
        binding.currencyRatesState.layoutManager = LinearLayoutManager(this)

        viewModel.currenciesSymbols()

        //Observing the currency symbols live data from view model
        viewModel.currencySymbols.observe(this) { currencySymbols ->
            updateDropDownMenu(currencySymbols)
        }

        //Observing the currency rates live data from view model
        viewModel.currencyRates.observe(this) { currencyRates ->
            if (currencyRates != null) {
                updateRecyclerView(currencyRates, viewModel.currencyFullName.value!!)
            }
        }

        //Observing the error state flow from view model
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isErrorOccurred.collect { isErrorOccurred ->
                    if (isErrorOccurred) {
                        showErrorDialog()
                    }
                }
            }
        }

        //Apply the function do get the currency rates to the menu items
        binding.currencySelectionInputLayout.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                selectedCode = binding.currencySelectionInputLayout.text.toString()

                binding.selectCurrencyState.visibility = View.GONE
                binding.waitingResponseState.visibility = View.VISIBLE
                viewModel.currenciesRates(selectedCode)
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
        val ratesAdapter =
            CurrencyRatesAdapter(rates, codes)
        binding.currencyRatesState.adapter = ratesAdapter
        ratesAdapter.notifyDataSetChanged()

        binding.waitingResponseState.visibility = View.GONE
        binding.currencyRatesState.visibility = View.VISIBLE
    }

    //Creating the error dialog box
    private fun showErrorDialog() {
        DialogBoxUtils.errorDialogBuilder(this, viewModel.errorMessage.value)
        { viewModel.currenciesSymbols() }
    }
}
