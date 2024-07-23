package com.cuongngo.core_project.ui.profile

import android.content.Intent
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.FragmentProfileBinding
import com.cuongngo.core_project.ui.search_form.ListFormActivity

class ProfileFragment : BaseFragmentMVVM<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.fragment_profile

    override fun setUp() {
        binding.btnLogOut.setOnClickListener {
//            startActivity(
//                Intent(context, RecordProcessActivity::class.java)
//            )
            startActivity(
                Intent(context, ListFormActivity::class.java)
            )
        }
    }

    override fun setUpObserver() {

    }

    companion object {
        val TAG = ProfileFragment::class.java.simpleName
    }

}