package com.cuongngo.core_project.ui.login

import android.content.Intent
import androidx.core.view.isVisible
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivityLoginByUserIdBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.invoker.BaseInterceptor
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.MainActivity

class LoginByMSNVActivity : AppBaseActivityMVVM<ActivityLoginByUserIdBinding, UserViewModel>() {

    override val viewModel: UserViewModel by kodeinViewModel()

    companion object {
        val TAG = LoginByMSNVActivity::class.java.simpleName
    }
    override fun inflateLayout(): Int = R.layout.activity_login_by_user_id

    override fun setUp() {
        with(binding){
            btnLogin.setOnClickListener {
                if (validate()){
                    viewModel.login(
                        user_name = binding.viewInputUserId.edtUserId.text.toString() ?: "",
                        password = binding.viewInputPassword.edtPassword.text.toString() ?: "",
                        device_code = "123"
                    )
                }
            }
        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.login){
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    WTF("responseApi ${it.data}")
                    if ((it.data?.data?.token ?: "").isNotEmpty()){
                        saveUserData(it.data?.data)
                        gotoMain()
                    }else{
                        //check thử bị cái gì
                    }
                },
                onError = {
                    WTF("responseApi ${it.data}")
                    hideProgressDialog()
                }
            )
        }
    }

    private fun validate(): Boolean{
        if (binding.viewInputUserId.edtUserId.text.isNullOrEmpty()){
            binding.viewInputUserId.tvValidate.isVisible = true
            binding.viewInputUserId.tvValidate.text = "Vui lòng nhập mã số nhân viên"
            return false
        }else if (binding.viewInputPassword.edtPassword.text.isNullOrEmpty()){
            binding.viewInputUserId.tvValidate.isVisible = true
            binding.viewInputUserId.tvValidate.text = "Vui lòng nhập mật khẩu"
            return false
        }else{
            binding.viewInputUserId.tvValidate.isVisible = false
            return true
        }
    }

    private fun gotoMain() {
        Intent(this, MainActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}