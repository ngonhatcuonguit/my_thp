package com.cuongngo.core_project.ui.login

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ActiveDeviceDialog
import com.cuongngo.core_project.base.view.ProgressDialog
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivityLoginByUserIdBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.MainActivity
import com.cuongngo.core_project.utils.toast.showMessageCheckInternet
import com.cuongngo.core_project.utils.toast.showMessageToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                            password = binding.viewInputPassword.edtPassword.text.toString() ?: "",
                            device_code = AppPreferences.getDeviceInfo()?.serial ?: ""
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

                    if (viewModel.loginData?.device_is_active != true) {
                        activeDevice()
                    } else {
                        if ((viewModel.loginData?.token ?: "").isNotEmpty()) {
                            gotoMain()
                        } else {
                            showMessageToast(
                                this,
                                false,
                                contentFail = "Đã có lỗi xảy ra: Empty Token",
                                contentDone = ""
                            )
                        }
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
        observeLiveDataChanged(viewModel.activeDevice){
            it.onResultReceived(
                onLoading = {
                    processActiveDevice.show()
                },
                onSuccess = {
                    Handler(Looper.getMainLooper()).postDelayed({
                        processActiveDevice.hide()
                        if(it.data?.status == "success"){
                            viewModel.loginData = viewModel.loginData?.copy(
                                device_is_active = true
                            )
                            saveUserData(viewModel.loginData)
                            gotoMain()
                        }else{
                            showMessageToast(
                                this,
                                false,
                                contentFail = "Đã có lỗi xảy ra",
                                contentDone = ""
                            )
                        }
                    }, 3000L)
                },
                onError = {
                    Handler(Looper.getMainLooper()).postDelayed({
                        processActiveDevice.hide()
                    }, 3000L)
                    showMessageToast(
                        this,
                        false,
                        contentFail = "Đã có lỗi xảy ra",
                        contentDone = ""
                    )
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