package com.cuongngo.core_project.ui.login

import android.content.Intent
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivityLoginMethodBinding
import com.cuongngo.core_project.ui.MainActivity

class LoginMethodActivity : AppBaseActivityMVVM<ActivityLoginMethodBinding, UserViewModel>() {

    companion object {
        val TAG = LoginMethodActivity::class.simpleName
    }

    override val viewModel: UserViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_login_method

    override fun setUp() {
        with(binding) {
            clEmail.setOnClickListener {
                //
            }
            clLoginUserId.setOnClickListener {
                gotoLoginMSNV()
            }
            clLoginPhoneNumber.setOnClickListener {
                gotoLoginPhoneNumber()
            }
        }
    }
    private fun gotoLoginMSNV() {
        Intent(this, LoginByMSNVActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun gotoLoginPhoneNumber() {
        Intent(this, LoginByPhoneNumberActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun setUpObserver() {
        //
    }

    override fun onBackPressed() {
        //no back
    }

}