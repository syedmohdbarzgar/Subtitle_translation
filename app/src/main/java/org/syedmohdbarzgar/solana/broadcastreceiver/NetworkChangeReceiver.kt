package org.syedmohdbarzgar.solana.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import org.syedmohdbarzgar.solana.views.activities.NoConnectionActivity

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            // شبکه متصل است
            Log.d("NetworkChangeReceiver", "Connected to the internet")
        } else {
            // شبکه قطع است
            Log.d("NetworkChangeReceiver", "Disconnected from the internet")
            // ورود به اکتیویتی NoConnectionActivity
            val noConnectionIntent = Intent(context, NoConnectionActivity::class.java)
            noConnectionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // برای باز کردن اکتیویتی از خارج از اکتیویتی
            context?.startActivity(noConnectionIntent)
        }
    }

}