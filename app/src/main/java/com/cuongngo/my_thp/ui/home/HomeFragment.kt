package com.cuongngo.my_thp.ui.home

import android.content.Intent
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.fragment.BaseFragmentMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.databinding.FragmentHomeBinding
import com.cuongngo.my_thp.ui.acb_app.TheAcbActivity
import com.cuongngo.my_thp.ui.acb_app.tai_khoan.TaiKhoanThanhToanActivity
import com.cuongngo.my_thp.ui.notification.NotificationActivity
import com.cuongngo.my_thp.utils.number.formatNumberWithDots
import java.util.Calendar

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()
    override fun inflateLayout() = R.layout.fragment_home

    override fun setUp() {

        binding.apply {

            tvHello.text = getPartOfDay()

            clNoti.setOnClickListener{
                startActivity(Intent(requireContext(), NotificationActivity::class.java))
            }

            clSoDu.setOnClickListener {
                startActivity(Intent(requireContext(), NotificationActivity::class.java))
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