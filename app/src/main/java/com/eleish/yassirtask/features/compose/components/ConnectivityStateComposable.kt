package com.eleish.yassirtask.features.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.eleish.yassirtask.core.isNetworkAvailable
import com.eleish.yassirtask.core.observeNetworkAvailabilityAsFlow

@Composable
fun connectivityState(): State<Boolean> {
    val context = LocalContext.current

    return produceState(initialValue = context.isNetworkAvailable()) {
        context.observeNetworkAvailabilityAsFlow().collect { value = it }
    }
}