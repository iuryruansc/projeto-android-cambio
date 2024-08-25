package com.currencyview.common.util

import android.app.Activity
import com.betrye.currencyview.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogBoxUtils {

    // Showing the error dialog box
    fun errorDialogBuilder(activity: Activity, errorMessage: String?,retryAction: () -> Unit) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.error_title)
            .setMessage(errorMessage)
            .setCancelable(false)
            .setPositiveButton(R.string.error_retry) { dialog, _ ->
                dialog.dismiss()
                retryAction()
            }
            .setNegativeButton(R.string.error_cancel) { dialog, _ ->
                dialog.dismiss()
                activity.finish()
            }
            .show()
    }
}
