package com.cuongngo.my_thp.ui.acb_app

import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_CCCD
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_DIA_CHI
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_EMAIL
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_FB
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_GIOI_TINH
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_HET_HAN_CCCD
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_NAME
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_NGAY_CCCD
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_NGUON_THU_NHAP
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_NOI_CCCD
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_SDT
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_THU_NHAP
import com.cuongngo.my_thp.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppBaseActivityMVVM<ActivityUserProfileBinding, GdViewModel>() {

    override val viewModel: GdViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_user_profile

    override fun setUp() {
        with(binding) {

            if (AppPreferences.getACBInfo(KEY_NAME).isNotEmpty()) {
                tvName.text = AppPreferences.getACBInfo(KEY_NAME)
            }
            tvName.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_NAME, tvName)
                true
            }

            if (AppPreferences.getACBInfo(KEY_CCCD).isNotEmpty()) {
                tvCccd.text = AppPreferences.getACBInfo(KEY_CCCD)
            }

            tvCccd.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_CCCD, tvCccd)
                true
            }

            if (AppPreferences.getACBInfo(KEY_NGAY_CCCD).isNotEmpty()) {
                tvNgayCap.text = AppPreferences.getACBInfo(KEY_NGAY_CCCD)
            }
            tvNgayCap.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_NGAY_CCCD, tvNgayCap)
                true
            }

            if (AppPreferences.getACBInfo(KEY_NOI_CCCD).isNotEmpty()) {
                tvNoiCap.text = AppPreferences.getACBInfo(KEY_NOI_CCCD)
            }
            tvNoiCap.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_NOI_CCCD, tvNoiCap)
                true
            }

            if (AppPreferences.getACBInfo(KEY_HET_HAN_CCCD).isNotEmpty()) {
                tvNgayHetHan.text = AppPreferences.getACBInfo(KEY_HET_HAN_CCCD)
            }
            tvNgayHetHan.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_HET_HAN_CCCD, tvNgayHetHan)
                true
            }

            if (AppPreferences.getACBInfo(KEY_GIOI_TINH).isNotEmpty()) {
                tvGioiTinh.text = AppPreferences.getACBInfo(KEY_GIOI_TINH)
            }
            tvGioiTinh.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_GIOI_TINH, tvGioiTinh)
                true
            }
            if (AppPreferences.getACBInfo(KEY_SDT).isNotEmpty()) {
                tvSdt.text = AppPreferences.getACBInfo(KEY_SDT)
            }
            tvSdt.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_SDT, tvSdt)
                true
            }
            if (AppPreferences.getACBInfo(KEY_EMAIL).isNotEmpty()) {
                tvEmail.text = AppPreferences.getACBInfo(KEY_EMAIL)
            }
            tvEmail.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_EMAIL, tvEmail)
                true
            }
            if (AppPreferences.getACBInfo(KEY_DIA_CHI).isNotEmpty()) {
                tvDc.text = AppPreferences.getACBInfo(KEY_DIA_CHI)
            }
            tvDc.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_DIA_CHI, tvDc)
                true
            }
            if (AppPreferences.getACBInfo(KEY_THU_NHAP).isNotEmpty()) {
                tvThuNhap.text = AppPreferences.getACBInfo(KEY_THU_NHAP)
            }
            tvThuNhap.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_THU_NHAP, tvThuNhap)
                true
            }
            if (AppPreferences.getACBInfo(KEY_NGUON_THU_NHAP).isNotEmpty()) {
                tvNguonThuNhap.text = AppPreferences.getACBInfo(KEY_NGUON_THU_NHAP)
            }
            tvNguonThuNhap.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_NGUON_THU_NHAP, tvNguonThuNhap)
                true
            }

            if (AppPreferences.getACBInfo(KEY_FB).isNotEmpty()) {
                tvFb.text = AppPreferences.getACBInfo(KEY_FB)
            }
            tvFb.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_FB, tvFb)
                true
            }

        }
    }

    override fun setUpObserver() {

    }
}