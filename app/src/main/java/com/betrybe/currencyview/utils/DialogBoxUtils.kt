package com.betrybe.currencyview.utils

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogBoxUtils {

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

    fun showCurrenciesErrorDialog(activity: Activity, errorMessage: String?, retryAction: () -> Unit) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Error")
            .setMessage("Failed to load the currency conversion... $errorMessage")
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