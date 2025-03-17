package com.cuongngo.my_thp.ui.sync_data

import android.os.Build
import androidx.annotation.RequiresApi
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.databinding.ActivitySyncDataBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ext.observeLiveDataChanged
import com.cuongngo.my_thp.services.network.onResultReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySyncData : AppBaseActivityMVVM<ActivitySyncDataBinding, SyncDataViewModel>() {

    override val viewModel: SyncDataViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_sync_data

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setUp() {
        with(binding) {

            layoutAppBar.ivBack.setOnClickListener {
                onBackPressed()
            }

            clSyncAllForm.setOnClickListener {
                if(isNetworkAvailable(this@ActivitySyncData)){
                    viewModel.getAllFormRemote()
                }else{
                    showDialogWarning(
                        content = "Thiết bị chưa được kết nối mạng. Vui lòng kết nối mạng trước khi đồng bộ dữ liệu!",
                        btnLeftContent = "Đã hiểu",
                        supportFragmentManager = supportFragmentManager
                    )
                }
            }
            clSyncAllUser.setOnClickListener {
                viewModel.getAllUserRemote(true)
                if(isNetworkAvailable(this@ActivitySyncData)){
                    viewModel.getAllUserRemote(true)
                }else{
                    showDialogWarning(
                        content = "Thiết bị chưa được kết nối mạng. Vui lòng kết nối mạng trước khi đồng bộ dữ liệu!",
                        btnLeftContent = "Đã hiểu",
                        supportFragmentManager = supportFragmentManager
                    )
                }
            }
            clSyncAllData.setOnClickListener {

            }

        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.listFormRemote) {
            it.onResultReceived(
                onLoading = {
                    processSyncDialog.show()
                },
                onSuccess = {
                    WTF("testSyncData ----getListOK ${it.data?.data}")
                    if (!it.data?.data.isNullOrEmpty()) {
                        viewModel.listGetFormRemote = it.data?.data
                        viewModel.formatFormTable()
                    }
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false, it.errorCode, "Có lỗi xảy ra")
                }
            )
        }
        observeLiveDataChanged(viewModel.formatFormTable) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testSyncData ----clearOK")
                    viewModel.upsertListForm(viewModel.listGetFormRemote ?: emptyList())
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false, it.errorCode, "Có lỗi xảy ra")
                }
            )
        }
        observeLiveDataChanged(viewModel.upsertListFormToLocal) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testSyncData ----upsertOK")
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3900)
                        processSyncDialog.hide()
                        setupShowDialogResult(true, showContent = "Dữ liệu từ hệ thống của THP đã được đồng bộ về thiết bị của bạn")
                    }
//                    HomeFragment().viewModel.getAllForm()
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false, it.errorCode)
                }
            )
        }

        //------
        observeLiveDataChanged(viewModel.getListUser) {
            it.onResultReceived(
                onLoading = {
                    processSyncDialog.show()
                },
                onSuccess = {
                    WTF("testSyncData ----getListOK ${it.data?.data}")
                    if (!it.data?.data.isNullOrEmpty()) {
                        viewModel.listGetUserRemote = it.data?.data
                        viewModel.formatUserTable()
                    }
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false, it.errorCode)
                }
            )
        }
        observeLiveDataChanged(viewModel.formatUserTable) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testSyncData ----clearOK")
                    viewModel.addListUser(viewModel.listGetUserRemote ?: emptyList())
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false, it.errorCode)
                }
            )
        }
        observeLiveDataChanged(viewModel.upsertListUserToLocal) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testSyncData ----upsertOK")
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3900)
                        processSyncDialog.hide()
                        setupShowDialogResult(true)
                    }
//                    HomeFragment().viewModel.getAllForm()
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false, it.errorCode)
                }
            )
        }
    }

}