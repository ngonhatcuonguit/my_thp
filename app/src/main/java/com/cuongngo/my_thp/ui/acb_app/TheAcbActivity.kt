package com.cuongngo.my_thp.ui.acb_app
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.databinding.ActivityTheAcbBinding
import com.cuongngo.my_thp.ui.home.HomeViewModel

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