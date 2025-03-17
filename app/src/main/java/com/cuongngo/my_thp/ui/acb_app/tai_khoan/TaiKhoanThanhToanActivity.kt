package com.cuongngo.my_thp.ui.acb_app.tai_khoan

import android.content.Intent
import androidx.core.content.ContextCompat
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_SD_KHA_DUNG
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_SD_THUC
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_STK
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_TONG_SD
import com.cuongngo.my_thp.databinding.ActivityTaiKhoanThanhToanBinding
import com.cuongngo.my_thp.ui.acb_app.lich_su_gd.LsGdActivity
import com.cuongngo.my_thp.ui.home.HomeViewModel
import com.cuongngo.my_thp.utils.number.formatNumberWithDots

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
                tvSoDu.text = formatNumberWithDots(AppPreferences.getACBInfo(KEY_TONG_SD))
            }
            if (AppPreferences.getACBInfo(KEY_SD_KHA_DUNG).isNotEmpty()) {
                tvTienKhaDung.text = formatNumberWithDots(AppPreferences.getACBInfo(KEY_SD_KHA_DUNG))
            }
            if (AppPreferences.getACBInfo(KEY_STK).isNotEmpty()) {
                tvStk.text = AppPreferences.getACBInfo(KEY_STK)
            }
            if (AppPreferences.getACBInfo(KEY_SD_THUC).isNotEmpty()) {
                tvTienThuc.text = formatNumberWithDots(AppPreferences.getACBInfo(KEY_SD_THUC))
            }

            tvSoDu.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_TONG_SD, tvSoDu)
                true
            }
            tvStk.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_STK, tvStk)
                true
            }
            tvTienKhaDung.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_SD_KHA_DUNG, tvTienKhaDung)
                true
            }
            tvTienThuc.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_SD_THUC, tvTienThuc)
                true
            }


            clLichSu.setOnClickListener {
                Intent(applicationContext, LsGdActivity::class.java).apply {
                    startActivity(this)
                }
            }

        }
    }

    override fun setUpObserver() {
        //
    }
}