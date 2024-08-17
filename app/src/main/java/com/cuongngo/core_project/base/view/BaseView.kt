package com.cuongngo.core_project.base.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.services.THPApi

interface BaseView {
    fun provideRootView(): View?

    fun provideContext(): Context?

    fun provideLoading(): ProgressDialog?

    fun setUp()

    fun saveUserData(loginResponse: LoginResponse?) {
        with(AppPreferences){
            setUserAccessToken(loginResponse?.token ?: "")
            saveUserInfo(loginResponse)
            THPApi.updateToken(loginResponse?.token ?: "")
        }
    }

    fun clearUserData(){
        with(AppPreferences){
            setUserAccessToken("")
            saveUserInfo(null)
            THPApi.updateToken("")
        }
    }

    fun setUpObserver()

    /**
     *  Show progress dialog
     * */
    fun showProgressDialog() {
        provideLoading()?.show()
    }


    /**
     *  Hide progress dialog
     * */
    fun hideProgressDialog() {
        provideLoading()?.hide()
    }

    /**
     *  Hide key board
     * */
    fun hideKeyboard() {
        provideRootView()?.let {
            val inputMethodManager = provideContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    /**
     *  Show key board
     * */
    fun showKeyBoard() {
        val imm = provideContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     *  Check internet available
     * */
    //check internet connect
    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
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

    /**
     *  Set dispatch touch event
     * */
    fun setupDispatchTouchEvent(rootView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(
            rootView
        ) { _, insets ->
            ViewCompat.onApplyWindowInsets(
                rootView,
                insets.replaceSystemWindowInsets(
                    insets.systemWindowInsetLeft, 0,
                    insets.systemWindowInsetRight, insets.systemWindowInsetBottom
                )
            )
        }
    }
}