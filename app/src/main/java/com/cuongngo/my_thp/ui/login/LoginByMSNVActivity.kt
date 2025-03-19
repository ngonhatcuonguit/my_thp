package com.cuongngo.my_thp.ui.login

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.dialog_fragment.ActiveDeviceDialog
import com.cuongngo.my_thp.base.view.ProgressDialog
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.databinding.ActivityLoginByUserIdBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ext.observeLiveDataChanged
import com.cuongngo.my_thp.services.network.onResultReceived
import com.cuongngo.my_thp.ui.MainActivity
import com.cuongngo.my_thp.utils.toast.showMessageCheckInternet
import com.cuongngo.my_thp.utils.toast.showMessageToast

class LoginByMSNVActivity : AppBaseActivityMVVM<ActivityLoginByUserIdBinding, UserViewModel>() {

    override val viewModel: UserViewModel by kodeinViewModel()

    companion object {
        val TAG = LoginByMSNVActivity::class.java.simpleName
    }
    override fun inflateLayout(): Int = R.layout.activity_login_by_user_id

    val processActiveDevice by lazy {
        ProgressDialog(
            this,
            view = LayoutInflater.from(this).inflate(R.layout.dialog_process_active_device, null)
        )
    }

    override fun setUp() {
        with(binding){
            btnLogin.setOnClickListener {
                if (validate()){
                    if (isNetworkAvailable(this@LoginByMSNVActivity)) {
                        viewModel.login(
                            user_name = binding.viewInputUserId.edtUserId.text.toString() ?: "",
                            password = binding.viewInputPassword.edtPassword.text.toString() ?: ""
                        )
                    }else{
                        showMessageCheckInternet(this@LoginByMSNVActivity, false)
                    }
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
                    hideProgressDialog()
                    viewModel.loginData = it.data?.data
                    saveUserData(viewModel.loginData)
                    if(it.data?.response_status?.status == "success"){
                        if ((viewModel.loginData?.token ?: "").isNotEmpty()) {
                            gotoMain()
                            WTF("token: ${viewModel.loginData?.token}")
                        } else {
                            showMessageToast(
                                this,
                                false,
                                contentFail = "Đã có lỗi xảy ra: ${it.data.response_status.messages?.firstOrNull()?.message}",
                                contentDone = ""
                            )
                        }
                    }else{
                        showMessageToast(
                            this,
                            false,
                            contentFail = "${it.data?.response_status?.messages?.firstOrNull()?.message}",
                            contentDone = ""
                        )
                    }
                },
                onError = {
                    hideProgressDialog()
                    showMessageToast(
                        this,
                        false,
                        contentFail = "Đã có lỗi xảy ra: ${it.data?.response_status?.messages?.firstOrNull()?.message}",
                        contentDone = ""
                    )
                }
            )
        }
    }

    private fun validate(): Boolean{
        if (binding.viewInputUserId.edtUserId.text.isNullOrEmpty()){
            binding.viewInputUserId.tvValidate.isVisible = true
            binding.viewInputUserId.tvValidate.text = "Vui lòng nhập email"
            return false
        }else if (binding.viewInputPassword.edtPassword.text.isNullOrEmpty()){
            binding.viewInputPassword.tvValidate.isVisible = true
            binding.viewInputPassword.tvValidate.text = "Vui lòng nhập mật khẩu"
            binding.viewInputUserId.tvValidate.isVisible = false
            return false
        }else{
            binding.viewInputPassword.tvValidate.isVisible = false
            binding.viewInputUserId.tvValidate.isVisible = false
            return true
        }
    }

    private fun activeDevice(){
        val deviceData = AppPreferences.getDeviceInfo()
        val activeDeviceDialog = ActiveDeviceDialog().apply {
            onRightButtonClick {
                viewModel.activeDevice(
                    device_id = deviceData?.id ?: "",
                    manufacturer = deviceData?.manufacturer,
                    model = deviceData?.model,
                    brand = deviceData?.brand,
                    product = deviceData?.product,
                    os_version = deviceData?.osVersion,
                    apiLevel = deviceData?.apiLevel.toString(),
                    hardware = deviceData?.hardware,
                    user = deviceData?.user,
                    host = deviceData?.host,
                    display = deviceData?.display,
                    device = deviceData?.device
                )
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        activeDeviceDialog.show(supportFragmentManager, ActiveDeviceDialog.TAG)
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