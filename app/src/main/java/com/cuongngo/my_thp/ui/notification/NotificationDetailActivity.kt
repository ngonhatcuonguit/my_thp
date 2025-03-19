package com.cuongngo.my_thp.ui.notification

import androidx.core.content.ContextCompat
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.databinding.ActivityNotificationDetailBinding
import com.cuongngo.my_thp.ui.login.UserViewModel

class NotificationDetailActivity : AppBaseActivityMVVM<ActivityNotificationDetailBinding, UserViewModel>(){

    override val viewModel: UserViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_notification_detail

    override fun setUp() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.acb_primary_2nd)
        binding.apply{
            ivBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun setUpObserver() {

    }



}