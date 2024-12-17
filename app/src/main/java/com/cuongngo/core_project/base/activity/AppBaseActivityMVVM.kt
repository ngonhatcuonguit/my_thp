package com.cuongngo.core_project.base.activity

import android.os.Bundle
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.data.local.AppPreferences.KEY_HO_VA_TEN
import com.cuongngo.core_project.data.local.AppPreferences.KEY_NAME
import com.cuongngo.core_project.data.local.AppPreferences.KEY_NICK_NAME
import com.cuongngo.core_project.data.local.AppPreferences.KEY_SD_KHA_DUNG
import com.cuongngo.core_project.data.local.AppPreferences.KEY_SD_THUC
import com.cuongngo.core_project.data.local.AppPreferences.KEY_TONG_SD
import com.cuongngo.core_project.ui.home.HomeFragment
import com.cuongngo.core_project.utils.number.formatNumberWithDots

abstract class AppBaseActivityMVVM<DB: ViewDataBinding, VM: BaseViewModel>: BaseActivity<DB>() {

    abstract val viewModel: VM

    open fun onCreateX(savedInstanceState: Bundle?){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
        onCreateX(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    open fun showLoading(){

    }

    open fun hideLoading(){

    }

    fun setupShowDialogChangeValue(key: String, view: TextView) {
        var edtText = view.text.toString() ?: ""
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = "Sửa dổi thông tin",
                subTitle = "subtitle",
                content = "Vui lòng nhập thông tin vào bên dưới và xác nhận để lưu!",
                edtValue = edtText,
                edtHint = "Vui lòng nhập thông tin",
                edtTitle = "Nhập thông tin thay đổi",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Lưu thông tin",
                isSingle = false,
                typeInput = "textarea"
            )
        ).apply {
            onRightButtonClick {
                hideKeyboard()
                //luu thong tin
                when (key) {
                    KEY_NICK_NAME -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_NICK_NAME, it)
                        view.text = it.toString()
                    }

                    KEY_NAME -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_NAME, it)
                        view.text = it.toString()
                    }

                    KEY_HO_VA_TEN -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_HO_VA_TEN, it)
                        view.text = it.toString()
                    }

                    KEY_TONG_SD -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_TONG_SD, it)
                        view.text = formatNumberWithDots(it.toString())
                    }

                    KEY_SD_KHA_DUNG -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_SD_KHA_DUNG, it)
                        view.text = formatNumberWithDots(it.toString())
                    }

                    KEY_SD_THUC -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_SD_THUC, it)
                        view.text = formatNumberWithDots(it.toString())
                    }

                    HomeFragment.KEY_DIEM -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_DIEM, it)
                        view.text = formatNumberWithDots(it.toString())
                    }

                    HomeFragment.KEY_THE -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_THE, it)
                        view.text = it.toString()
                    }

                    HomeFragment.KEY_STK -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_STK, it)
                        view.text = it.toString()
                    }

                    else -> {
                        AppPreferences.setACBInfo(key, it)
                        view.text = it.toString()
                    }
                }
                dismiss()
            }
            onLeftButtonClick {
                hideKeyboard()
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }
}