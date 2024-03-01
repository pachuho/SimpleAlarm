package com.pachuho.sleepAlarm.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(snackBarText: String, timeLength: Int? = null) {
    Snackbar.make(this, snackBarText, timeLength ?: Snackbar.LENGTH_SHORT).show()
}
