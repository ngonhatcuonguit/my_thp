package com.cuongngo.core_project.ui.acb_app
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivityTheAcbBinding
import com.cuongngo.core_project.ui.home.HomeViewModel

class TheAcbActivity :AppBaseActivityMVVM<ActivityTheAcbBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()
    override fun inflateLayout(): Int = R.layout.activity_the_acb

    override fun setUp() {
        with(binding){
            clAppBar.ivBack.setOnClickListener {
                finish()
            }

        }
    }

    override fun setUpObserver() {
        //
    }
}