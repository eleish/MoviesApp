package com.eleish.yassirtask.core

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showLongToast(@StringRes stringRes: Int) {
    showLongToast(getString(stringRes))
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}