package com.cuongngo.my_thp.services.network.invoker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.cuongngo.my_thp.App
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.utils.toast.showMessageCheckInternet
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(
    private var context: Context
) : Interceptor {
    private val applicationContext = context.applicationContext
    private var toast: Toast? = null
    private val handler = Handler(Looper.getMainLooper())
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isNetworkAvailable(context)) {
            showMessageCheckInternet(context, false)
        }
        return chain.proceed(chain.request())
    }

    /**
     *  Check internet available
     * */
    //check internet connect
    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    )
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun showErrorToast() {
        handler.post {
            val context = App.getInstance()
            if (toast == null) {
                toast = Toast.makeText(
                    context,
                    context.getString(R.string.no_internet_connection),
                    Toast.LENGTH_LONG
                )
            }
            toast?.setText(context.getString(R.string.no_internet_connection))
            toast?.show()
        }
    }

}