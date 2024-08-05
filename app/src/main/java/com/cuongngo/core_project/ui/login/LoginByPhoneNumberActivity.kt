package com.cuongngo.core_project.ui.login

import android.content.Intent
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivityLoginPhoneNumberBinding
import com.cuongngo.core_project.ui.MainActivity

class LoginByPhoneNumberActivity :
    AppBaseActivityMVVM<ActivityLoginPhoneNumberBinding, UserViewModel>() {
    override val viewModel: UserViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_login_phone_number

    override fun setUp() {
        with(binding) {
            btnLogin.setOnClickListener {
                gotoMain()
            }
        }
    }

    private fun gotoMain() {
        Intent(this, MainActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
        }
    }

    override fun setUpObserver() {
        //
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}