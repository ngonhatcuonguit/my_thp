package com.cuongngo.my_thp.ui.profile

import android.content.Intent
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.fragment.BaseFragmentMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.data.local.AppPreferences.KEY_NAME
import com.cuongngo.my_thp.databinding.FragmentProfileBinding
import com.cuongngo.my_thp.ui.acb_app.UserProfileActivity
import com.cuongngo.my_thp.ui.login.LoginMethodActivity
import com.cuongngo.my_thp.ui.sync_data.ActivitySyncData

class ProfileFragment : BaseFragmentMVVM<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.fragment_profile

    override fun setUp() {
        with(binding) {
//            AppPreferences.getACBInfo(KEY_NAME).let {
//                tvName.text = it
//            }
//
//            clUserInfor.setOnClickListener {
//                Intent(context, UserProfileActivity::class.java).apply {
//                    startActivity(this)
//                }
//            }

        }
    }

    private fun gotoLoginMethod() {
        Intent(requireContext(), LoginMethodActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun gotoSyncData() {
        Intent(requireContext(), ActivitySyncData::class.java).apply {
            startActivity(this)
        }
    }

    override fun setUpObserver() {

    }

    companion object {
        val TAG = ProfileFragment::class.java.simpleName
    }

    override fun onResume() {
        AppPreferences.getACBInfo(KEY_NAME).let {
            binding.tvName.text = it
        }
        super.onResume()
    }

}