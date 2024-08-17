package com.cuongngo.core_project.base.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.view.BaseView
import com.cuongngo.core_project.base.view.ProgressDialog
import com.cuongngo.core_project.ui.login.LoginMethodActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment<DB: ViewDataBinding>: Fragment(), KodeinAware, BaseView {
    open lateinit var binding: DB

    override val kodein by kodein()

    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }

    val processSyncDialog by lazy {
        ProgressDialog(
            requireContext(),
            view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_process_sync_data, null)
        )
    }
    @LayoutRes
    abstract fun inflateLayout(): Int

    override fun provideLoading() : ProgressDialog = progressDialog

    override fun provideContext(): Context? = requireContext()

    override fun provideRootView(): View? = view

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,inflateLayout(),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        setUpObserver()
    }

    open fun setupShowDialogResult(isSuccess: Boolean, errorCode: Int? = null) {
        var title = "Thành Công"
        var content = "Dữ liệu từ hệ thống của THP đã được đồng bộ về thiết bị của bạn"
        var btnContent = "Đồng ý"
        if (!isSuccess) {
            when(errorCode){
                401 -> {
                    title = "Phiên đăng nhập hết hạn"
                    content = "Phiên đăng nhập của bạn đã quá hạn, vui lòng đăng nhập lại!"
                    btnContent = "Đăng nhập"
                }

                404 -> {
                    title = "Lỗi 404"
                    content = "Đã có lỗi xảy ra, vui lòng kiểm tra lại!"
                    btnContent = "Đồng ý"
                }

                else -> {
                    title = "Lỗi $errorCode"
                    content = "Đã có lỗi xảy ra, vui lòng kiểm tra lại!"
                    btnContent = "Đồng ý"
                }
            }
        }
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = title,
                subTitle = "",
                content = content,
                leftButtonTitle = btnContent,
                rightButtonTitle = "",
                isSingle = true
            ),
            margins = 90f
        ).apply {
            onRightButtonClick {
                dismiss()
            }
            onLeftButtonClick {
                if (!isSuccess) {
                    when(errorCode){
                        401 -> {
                            gotoLoginMethod()
                        }

                        404 -> {
                            //
                        }

                        else -> {
                            //
                        }
                    }
                }else{
                    dismiss()
                }
            }
        }
        confirmDialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    private fun gotoLoginMethod() {
        Intent(requireContext(), LoginMethodActivity::class.java).apply {
        }.also {
            startActivity(it)
        }
    }
}