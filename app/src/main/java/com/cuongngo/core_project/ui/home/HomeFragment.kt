package com.cuongngo.core_project.ui.home

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.FragmentHomeBinding
import com.cuongngo.core_project.ui.acb_app.TheAcbActivity
import com.cuongngo.core_project.ui.acb_app.tai_khoan.TaiKhoanThanhToanActivity
import java.util.Calendar

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()
    override fun inflateLayout() = R.layout.fragment_home

    override fun setUp() {
        binding.apply {
//            tvName.text =
//                "Hello, ${AppPreferences.getUserInfo()?.first_name ?: ""} ${AppPreferences.getUserInfo()?.last_name ?: ""}"

            tvHello.text = getPartOfDay()
            if (AppPreferences.getACBInfo(KEY_NICK_NAME).isNotEmpty()){
                tvAvatar.text = AppPreferences.getACBInfo(KEY_NICK_NAME)
            }

            if (AppPreferences.getACBInfo(KEY_DIEM).isNotEmpty()){
                tvDiem.text = AppPreferences.getACBInfo(KEY_DIEM)
            }

            if (AppPreferences.getACBInfo(KEY_SD_KHA_DUNG).isNotEmpty()){
                tvSoDu.text = AppPreferences.getACBInfo(KEY_SD_KHA_DUNG)
            }

            if (AppPreferences.getACBInfo(KEY_NAME).isNotEmpty()){
                tvUserName.text = AppPreferences.getACBInfo(KEY_NAME)
            }

            tvUserName.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_NAME, tvUserName)
                true
            }
            tvAvatar.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_NICK_NAME, tvAvatar)
                true
            }

            clSoDu.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_SD_KHA_DUNG, tvTien)
                true
            }

            clDiem.setOnLongClickListener {
                setupShowDialogChangeValue(KEY_DIEM, tvDiem)
                true
            }

            clThe.setOnClickListener {
                Intent(context, TheAcbActivity::class.java).apply {
                    startActivity(this)
                }
            }
            clTaiKhoan.setOnClickListener {
                Intent(context, TaiKhoanThanhToanActivity::class.java).apply {
                    startActivity(this)
                }
            }

        }
    }
    private fun getPartOfDay(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when {
            hour in 5..11 -> "Chào buổi sáng"
            hour in 12..17 -> "Chào buổi chiều"
            else -> "Chào buổi tối"
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
                when(key){
                    KEY_NICK_NAME->{
                        AppPreferences.setACBInfo(KEY_NICK_NAME, it)
                    }
                    KEY_NAME -> {
                        AppPreferences.setACBInfo(KEY_NAME, it)
                    }
                    KEY_HO_VA_TEN -> {
                        AppPreferences.setACBInfo(KEY_HO_VA_TEN, it)
                    }
                    KEY_TONG_SD -> {
                        AppPreferences.setACBInfo(KEY_TONG_SD, it)
                    }
                    KEY_SD_KHA_DUNG -> {
                        AppPreferences.setACBInfo(KEY_SD_KHA_DUNG, it)
                    }
                    KEY_SD_THUC -> {
                        AppPreferences.setACBInfo(KEY_SD_THUC, it)
                    }
                    KEY_DIEM -> {
                        AppPreferences.setACBInfo(KEY_DIEM, it)
                    }
                    KEY_THE -> {
                        AppPreferences.setACBInfo(KEY_THE, it)
                    }
                    KEY_STK -> {
                        AppPreferences.setACBInfo(KEY_STK, it)
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
        confirmDialog.show(childFragmentManager, ConfirmDialog.TAG)
    }
    override fun setUpObserver() {

    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
        const val KEY_NICK_NAME = "KEY_NICK_NAME"
        const val KEY_NAME = "KEY_NAME"
        const val KEY_HO_VA_TEN = "KEY_HO_VA_TEN"
        const val KEY_SD_KHA_DUNG = "KEY_SO_DU_KHA_DUNG"
        const val KEY_SD_THUC = "KEY_SD_THUC"
        const val KEY_TONG_SD = "KEY_TONG_SD"
        const val KEY_DIEM = "KEY_DIEM"
        const val KEY_STK = "KEY_STK"
        const val KEY_THE = "KEY_THE"
    }

}