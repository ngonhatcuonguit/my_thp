package com.cuongngo.core_project.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.BaseActivity
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivitySplashBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.ui.MainActivity
import com.cuongngo.core_project.ui.login.LoginMethodActivity
import com.cuongngo.core_project.ui.onboard.OnBoardActivity
import com.cuongngo.core_project.utils.toast.showMessageCheckInternet

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
//            gotoLoginMethod()
            if (AppPreferences.isShownOnBoard()) {
                if (AppPreferences.getUserAccessToken().isNullOrEmpty()) {
                    gotoLoginMethod()
                } else {
                    gotoMain()
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
    private fun gotoLoginMethod() {
        Intent(this, LoginMethodActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
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