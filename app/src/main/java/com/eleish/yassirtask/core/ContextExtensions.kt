package com.eleish.yassirtask.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import androidx.annotation.StringRes
import com.eleish.yassirtask.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun Context.showLongToast(@StringRes stringRes: Int) {
    showLongToast(getString(stringRes))
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

fun Context.observeNetworkAvailabilityAsFlow() = callbackFlow {
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    val connectivityManager =
        getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    var wasPreviouslyConnected = isNetworkAvailable()

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (wasPreviouslyConnected) {
                return
            }
            showLongToast(R.string.network_restored)
            trySend(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            showLongToast(R.string.no_internet_connection)
            wasPreviouslyConnected = false
            trySend(false)
        }
    }.also {
        connectivityManager.registerNetworkCallback(networkRequest, it)
    }

    awaitClose {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}