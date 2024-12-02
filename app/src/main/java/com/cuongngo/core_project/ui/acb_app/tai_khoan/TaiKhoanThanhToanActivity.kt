package com.cuongngo.core_project.ui.acb_app.tai_khoan
import androidx.core.content.ContextCompat
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivityTaiKhoanThanhToanBinding
import com.cuongngo.core_project.ui.home.HomeViewModel

class TaiKhoanThanhToanActivity : AppBaseActivityMVVM<ActivityTaiKhoanThanhToanBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_tai_khoan_thanh_toan
    override fun setUp() {
        with(binding){
            loAppBar.ivBack.setOnClickListener {
                finish()
            }
            loAppBar.tvTitle.text = " Tài khoản thanh toán"
            loAppBar.tvTitle.setTextColor(
                ContextCompat.getColor(
                this@TaiKhoanThanhToanActivity,
                R.color.acb_black_text
            ))

        }
    }

    override fun setUpObserver() {
        //
    }
}