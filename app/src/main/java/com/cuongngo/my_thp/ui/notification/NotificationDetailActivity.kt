package com.cuongngo.my_thp.ui.notification

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.databinding.ActivityNotificationDetailBinding
import com.cuongngo.my_thp.ui.login.UserViewModel
import com.cuongngo.my_thp.ui.notification.data.NotificationResponse

class NotificationDetailActivity : AppBaseActivityMVVM<ActivityNotificationDetailBinding, UserViewModel>(){

    override val viewModel: UserViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_notification_detail

    companion object {
        val TAG = NotificationDetailActivity::class.java.simpleName
        const val NOTI_DATA_KEY = "NOTI_DATA_KEY"
        fun newIntent(
            context: Context,
            noti: NotificationResponse?
        ): Intent {
            return Intent(context, NotificationDetailActivity::class.java).apply {
                putExtra(NOTI_DATA_KEY, noti)
            }
        }
    }



    override fun setUp() {
        binding.noti = intent.getSerializableExtra(NOTI_DATA_KEY) as NotificationResponse ?: null
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