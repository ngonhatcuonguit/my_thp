package com.cuongngo.core_project.ui.acb_app.tai_khoan

import android.content.Intent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.data.local.AppPreferences.KEY_SD_KHA_DUNG
import com.cuongngo.core_project.data.local.AppPreferences.KEY_STK
import com.cuongngo.core_project.data.local.AppPreferences.KEY_TONG_SD
import com.cuongngo.core_project.databinding.ActivityTaiKhoanThanhToanBinding
import com.cuongngo.core_project.ui.acb_app.lich_su_gd.LsGdActivity
import com.cuongngo.core_project.ui.home.HomeFragment
import com.cuongngo.core_project.ui.home.HomeViewModel

class TaiKhoanThanhToanActivity :
    AppBaseActivityMVVM<ActivityTaiKhoanThanhToanBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_tai_khoan_thanh_toan
    override fun setUp() {
        with(binding) {
            loAppBar.ivBack.setOnClickListener {
                finish()
            }
            loAppBar.tvTitle.text = " Tài khoản thanh toán"
            loAppBar.tvTitle.setTextColor(
                ContextCompat.getColor(
                    this@TaiKhoanThanhToanActivity,
                    R.color.acb_black_text
                )
            )

            if (AppPreferences.getACBInfo(KEY_TONG_SD).isNotEmpty()) {
                tvSoDu.text = AppPreferences.getACBInfo(KEY_TONG_SD)
            }
            if (AppPreferences.getACBInfo(KEY_SD_KHA_DUNG).isNotEmpty()) {
                tvTienKhaDung.text = AppPreferences.getACBInfo(KEY_SD_KHA_DUNG)
            }
            if (AppPreferences.getACBInfo(KEY_STK).isNotEmpty()) {
                tvStk.text = AppPreferences.getACBInfo(KEY_STK)
            }

            tvSoDu.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_TONG_SD, tvSoDu)
                true
            }
            tvStk.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_TONG_SD, tvSoDu)
                true
            }

            clLichSu.setOnClickListener {
                Intent(applicationContext, LsGdActivity::class.java).apply {
                    startActivity(this)
                }
            }

        }
    }

    private fun setupShowDialogChangeValue(key: String?, view: TextView) {
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
                    }

                    HomeFragment.KEY_NAME -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_NAME, it)
                    }

                    HomeFragment.KEY_HO_VA_TEN -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_HO_VA_TEN, it)
                    }

                    HomeFragment.KEY_TONG_SD -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_TONG_SD, it)
                    }

                    HomeFragment.KEY_SD_KHA_DUNG -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_SD_KHA_DUNG, it)
                    }

                    HomeFragment.KEY_SD_THUC -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_SD_THUC, it)
                    }

                    HomeFragment.KEY_DIEM -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_DIEM, it)
                    }

                    HomeFragment.KEY_THE -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_THE, it)
                    }

                    HomeFragment.KEY_STK -> {
                        AppPreferences.setACBInfo(HomeFragment.KEY_STK, it)
                    }

                    else -> {

                    }
                }
                view.text = it.toString()
                dismiss()
            }
            onLeftButtonClick {
                hideKeyboard()
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    override fun setUpObserver() {
        //
    }
}