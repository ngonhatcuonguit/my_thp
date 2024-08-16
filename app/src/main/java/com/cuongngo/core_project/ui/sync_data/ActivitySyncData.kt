package com.cuongngo.core_project.ui.sync_data

import android.view.LayoutInflater
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.view.ProgressDialog
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivitySyncDataBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySyncData : AppBaseActivityMVVM<ActivitySyncDataBinding, SyncDataViewModel>() {

    override val viewModel: SyncDataViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_sync_data

    private val processSyncDialog by lazy {
        ProgressDialog(
            this,
            view = LayoutInflater.from(this).inflate(R.layout.dialog_process_sync_data, null)
        )
    }

    override fun setUp() {
        with(binding) {

            clSyncAllForm.setOnClickListener {
                viewModel.getAllFormRemote()
            }
            clSyncAllUser.setOnClickListener {
                viewModel.getAllUserRemote(true)
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
                    setupShowDialogResult(false)
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
                    setupShowDialogResult(false)
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
                        setupShowDialogResult(true)
                    }
//                    HomeFragment().viewModel.getAllForm()
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false)
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
                    setupShowDialogResult(false)
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
                    setupShowDialogResult(false)
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
                    setupShowDialogResult(false)
                }
            )
        }
    }

    private fun setupShowDialogResult(isSuccess: Boolean) {
        var title = "Thành Công"
        var content = "Dữ liệu từ hệ thống của THP đã được đồng bộ về thiết bị của bạn"
        if (!isSuccess) {
            title = "Lỗi"
            content = "Đã có lỗi xảy ra, vui lòng kiểm tra lại!"
        }
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = title,
                subTitle = "",
                content = content,
                leftButtonTitle = "Đồng Ý",
                rightButtonTitle = "",
                isSingle = true
            ),
            margins = 90f
        ).apply {
            onRightButtonClick {
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

}