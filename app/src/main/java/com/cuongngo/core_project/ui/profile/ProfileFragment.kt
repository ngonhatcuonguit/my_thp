package com.cuongngo.core_project.ui.profile

import android.content.Intent
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.data.local.AppPreferences.KEY_NAME
import com.cuongngo.core_project.databinding.FragmentProfileBinding
import com.cuongngo.core_project.ui.login.LoginMethodActivity
import com.cuongngo.core_project.ui.sync_data.ActivitySyncData

class ProfileFragment : BaseFragmentMVVM<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.fragment_profile

    override fun setUp() {
        with(binding){
            AppPreferences.getACBInfo(KEY_NAME).let {
                tvName.text = it
            }

//            tvSyncData.setOnClickListener {
////                gotoSyncData()
//            }
//            ivBgSyncData.setOnClickListener {
////                gotoSyncData()
//            }
//            btnLogOut.setOnClickListener {
////                clearUserData()
////                gotoLoginMethod()
//            }
//            tvMember.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivMember
//                )
//            }
//            tvChangePassword.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivChangePassword
//                )
//            }
//            tvNotify.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivNotify
//                )
//            }
//            tvLanguage.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivLanguage
//                )
//            }
//            tvCountry.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivCountry
//                )
//            }
//            tvClearCache.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivClearCache
//                )
//            }
//            tvLegal.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivLegal
//                )
//            }
//            tvHelp.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Feature Coming Soon",
//                    ivHelp
//                )
//            }
//            tvAbout.setOnClickListener {
//                setupTooltip(
//                    requireContext(),
//                    "Version ${BuildConfig.VERSION_NAME}\nContact: CuongNgo IT Digital Technical Specialist",
//                    ivAbout
//                )
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