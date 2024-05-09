package com.cuongngo.core_project.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.cuongngo.core_project.AppPreferences
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.BaseActivity
import com.cuongngo.core_project.databinding.ActivitySplashBinding
import com.cuongngo.core_project.ui.MainActivity
import com.cuongngo.core_project.ui.onboard.OnBoardActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun inflateLayout(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLightStatusBar()
        setupSystemWindowInset()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            gotoMain()
            if (AppPreferences.isShownOnBoard()) {
                gotoMain()
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

    }

    override fun setUpObserver() {

    }
}