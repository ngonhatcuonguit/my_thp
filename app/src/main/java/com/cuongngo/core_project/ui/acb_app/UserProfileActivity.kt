package com.cuongngo.core_project.ui.acb_app

import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppBaseActivityMVVM<ActivityUserProfileBinding, GdViewModel>() {

    override val viewModel: GdViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_user_profile

    override fun setUp() {
        with(binding){

        }
    }

    override fun setUpObserver() {

    }
}