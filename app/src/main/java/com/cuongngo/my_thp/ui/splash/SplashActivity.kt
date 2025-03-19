package com.cuongngo.my_thp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.BaseActivity
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.databinding.ActivitySplashBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.services.THPApi
import com.cuongngo.my_thp.ui.MainActivity
import com.cuongngo.my_thp.ui.login.LoginMethodActivity
import com.cuongngo.my_thp.ui.onboard.OnBoardActivity
import com.cuongngo.my_thp.utils.toast.showMessageCheckInternet

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun inflateLayout(): Int = R.layout.activity_splash

    private val deviceInfo = getDeviceInfo(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        THPApi.updateToken(AppPreferences.getUserAccessToken() ?: "")
        enableLightStatusBar()
        setupSystemWindowInset()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
//            gotoListGK()
            if (AppPreferences.isShownOnBoard()) {
                if (AppPreferences.getUserInfo()?.token?.isNotEmpty() == true) {
                    gotoMain()
                } else {
                    gotoLogin()
                }
            } else gotoOnBoard()
            finish()
        }, 2000)
    }

    private fun gotoMain() {
        Intent(applicationContext, MainActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun gotoOnBoard() {
        Intent(applicationContext, OnBoardActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun setupSystemWindowInset() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onBackPressed() {
        //do nothing
    }

    override fun setUp() {
        // Log the device information
        AppPreferences.saveDeviceInfo(deviceInfo)
        checkNetworkAvailable()
        WTF("DeviceInfo ${AppPreferences.getDeviceInfo()}")
    }

    override fun setUpObserver() {

    }

    private fun checkNetworkAvailable() {
        if (!isNetworkAvailable(this)) {
            showMessageCheckInternet(this, false)
        } else {
            showMessageCheckInternet(this, true)
        }
    }

}