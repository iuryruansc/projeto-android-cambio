package com.betrybe.currencyview.ui.utils

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {

    //Creating the try/cancel dialog box to the dropdown items
    fun showCurrenciesErrorDialog(activity: Activity, errorMessage: String?, retryAction: () -> Unit) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Erro")
            .setMessage("Falha ao carregar conversões da moeda escolhida... $errorMessage")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                retryAction()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                activity.finish()
            }
            .show()
    }

    //Creating the try/cancel dialog box at the start of the app
    fun showSymbolsErrorDialog(activity: Activity, errorMessage: String?, retryAction: () -> Unit) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Error")
            .setMessage("Failed to load currencies... $errorMessage")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                retryAction()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                activity.finish()
            }
            .show()
    }

    //Calculating the delay time to each try
    fun pow(base: Long, exponent: Int): Long {
        if (exponent == 0) return 1
        var result = base
        for (i in 2..exponent) {
            result *= base
        }
        return result
    }
}