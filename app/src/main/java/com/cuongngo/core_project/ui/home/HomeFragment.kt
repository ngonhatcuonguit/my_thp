package com.cuongngo.core_project.ui.home

import android.content.Intent
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.FragmentHomeBinding
import com.cuongngo.core_project.ui.acb_app.TheAcbActivity
import com.cuongngo.core_project.ui.acb_app.tai_khoan.TaiKhoanThanhToanActivity

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()
    override fun inflateLayout() = R.layout.fragment_home

    override fun setUp() {
        binding.apply {
//            tvName.text =
//                "Hello, ${AppPreferences.getUserInfo()?.first_name ?: ""} ${AppPreferences.getUserInfo()?.last_name ?: ""}"

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
    override fun setUpObserver() {

    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

}