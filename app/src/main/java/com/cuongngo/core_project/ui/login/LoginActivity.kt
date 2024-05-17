package com.cuongngo.core_project.ui.login

import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivityLoginBinding
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived

class LoginActivity : AppBaseActivityMVVM<ActivityLoginBinding, UserViewModel>() {

    companion object {
        val TAG = LoginActivity::class.java.simpleName
    }

    override val viewModel: UserViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_login

    override fun setUp() {
        binding.bthLogin.setOnClickListener {
            viewModel.login(
                user_name = binding.edtEmail.text.toString() ?: "",
                password = binding.edtPassword.text.toString() ?: "",
                grant_type = "password"
            )
        }

    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.login){
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    if ((it.data?.access_token ?: "").isNotEmpty()){
                        AppPreferences.setUserAccessToken(it.data?.access_token.toString())
                    }
                    finish()
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }


}