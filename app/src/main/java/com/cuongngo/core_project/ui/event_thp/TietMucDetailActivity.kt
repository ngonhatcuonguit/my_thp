package com.cuongngo.core_project.ui.event_thp

import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivityTietMucDetailBinding
import com.cuongngo.core_project.ui.login.UserViewModel

class TietMucDetailActivity : AppBaseActivityMVVM<ActivityTietMucDetailBinding, UserViewModel>() {

    override val viewModel: UserViewModel by kodeinViewModel()

    companion object {
        val TAG = DanhSachGKActivity::class.java.simpleName
    }

    override fun inflateLayout(): Int = R.layout.activity_tiet_muc_detail

    override fun setUp() {
        with(binding){

        }
    }

    override fun setUpObserver() {
//        observeLiveDataChanged(viewModel.listExaminer) {
//            it.onResultReceived(
//                onLoading = {
//                    showProgressDialog()
//                },
//                onSuccess = {
//                    hideProgressDialog()
//                    it.data?.data?.listGK?.let { it1 -> setupRecycleView(it1) }
//                    WTF("listGK ${it.data?.data}")
//                },
//                onError = {
//                    hideProgressDialog()
//                }
//            )
//        }
    }

    override fun onBackPressed() {
        //
    }
}