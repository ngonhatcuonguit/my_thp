package com.cuongngo.core_project.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.ui.home.HomeFragment
import com.cuongngo.core_project.utils.number.formatNumberWithDots

abstract class BaseFragmentMVVM<DB: ViewDataBinding, VM: BaseViewModel>: BaseFragment<DB>() {
    abstract val viewModel: VM

    open fun onViewCreatedX(view: View, savedInstanceState: Bundle?){}

    open fun onCreateViewX(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?){}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.onCreate()
        onCreateViewX(inflater, container, savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedX(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

//    protected fun isUserLoggedIn() : Boolean = AppPreferences.getUserAccessToken().isNotEmpty()

    open fun showLoading(){

    }

    open fun hideLoading(){

    }

    fun setupShowDialogChangeValue(key: String?, view: TextView) {
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
                    HomeFragment.KEY_NICK_NAME -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_NICK_NAME, it)
                        view.text = it.toString()
                    }

                    HomeFragment.KEY_NAME -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_NAME, it)
                        view.text = it.toString()
                    }

                    HomeFragment.KEY_HO_VA_TEN -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_HO_VA_TEN, it)
                        view.text = it.toString()
                    }

                    HomeFragment.KEY_TONG_SD -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_TONG_SD, it)
                        view.text = formatNumberWithDots(it.toString())
                    }

                    HomeFragment.KEY_SD_KHA_DUNG -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_SD_KHA_DUNG, it)
                        view.text = formatNumberWithDots(it.toString())
                    }

                    HomeFragment.KEY_SD_THUC -> {
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

                    }
                }
                dismiss()
            }
            onLeftButtonClick {
                hideKeyboard()
                dismiss()
            }
        }
        confirmDialog.show(childFragmentManager, ConfirmDialog.TAG)
    }
}