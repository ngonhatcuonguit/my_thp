package com.cuongngo.core_project.ui.sync_data

import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivitySyncDataBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySyncData : AppBaseActivityMVVM<ActivitySyncDataBinding, SyncDataViewModel>() {

    override val viewModel: SyncDataViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_sync_data

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
                    setupShowDialogResult(false, it.errorCode)
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
                    setupShowDialogResult(false, it.errorCode)
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