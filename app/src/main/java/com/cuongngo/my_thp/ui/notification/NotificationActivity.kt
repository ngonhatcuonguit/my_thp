package com.cuongngo.my_thp.ui.notification

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.my_thp.databinding.ActivityNotificationBinding
import com.cuongngo.my_thp.services.network.onResultReceived
import com.cuongngo.my_thp.ui.login.UserViewModel
import com.cuongngo.my_thp.ui.notification.adapter.NotiAdapter
import io.reactivex.disposables.Disposable

class NotificationActivity : AppBaseActivityMVVM<ActivityNotificationBinding, UserViewModel>() {
    override val viewModel: UserViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_notification

    companion object {
        val TAG = NotificationActivity::class.java.simpleName
    }

    private lateinit var notiAdapter: NotiAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null
    override fun setUp() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.acb_primary_2nd)
        viewModel.getNotify()
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
        }
        setupRecycleView()
    }

    override fun setUpObserver() {
        viewModel.notify.observe(this) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    if (it.data?.data.isNullOrEmpty()) {
                        if (viewModel.page == 0){
                            binding.rvListNoti.isVisible = false
                            binding.layoutEmptyList.isVisible = true
                        }
                    }else{
                        notiAdapter.submitListNoti(it.data?.data)
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private fun setupRecycleView() {

        val gridLayoutManager = GridLayoutManager(this, 1)

//        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int) {
//                viewModel.getListNoti(page)
//            }
//        }

        notiAdapter = NotiAdapter(
            this,
            arrayListOf(),
            onItemClickListener = {
                NotificationDetailActivity.newIntent(this, it).apply {
                    startActivity(this)
                }
            }
        )

        binding.rvListNoti.apply {
            adapter = notiAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }

    }


}