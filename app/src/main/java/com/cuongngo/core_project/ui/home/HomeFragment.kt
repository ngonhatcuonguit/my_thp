package com.cuongngo.core_project.ui.home

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestData
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.convertRequestEntityToString
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomHeaderList
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomProcessStep
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomSheet
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.FragmentHomeBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.response.news.News
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.list_request.adapter.RequestHorizontalAdapter
import com.cuongngo.core_project.ui.request_detail.PushRequestCodeBody
import com.cuongngo.core_project.ui.request_detail.RequestBodyPush
import com.cuongngo.core_project.ui.request_detail.RequestCodeResult
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.ui.search_form.form_adapter.FormHorizontalAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerHelper
import com.cuongngo.core_project.utils.Constants
import com.cuongngo.core_project.utils.toast.showMessageOnSyncDataSuccess
import com.cuongngo.core_project.utils.toast.showMessageToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.Disposable
import kotlin.random.Random

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()

    override fun inflateLayout() = R.layout.fragment_home

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null

    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    private lateinit var formHorizontalAdapter: FormHorizontalAdapter
    private lateinit var requestHorizontalAdapter: RequestHorizontalAdapter

    override fun setUp() {
        syncForm()
        syncUser()
        viewModel.getAllRequest()
        setupTopViewPager(viewModel.listDefaultHotNews)
        setupRcvFavouriteForm()
        setupRcvSyncRequest()
        binding.apply {
            tvName.text =
                "Hello, ${AppPreferences.getUserInfo()?.first_name ?: ""} ${AppPreferences.getUserInfo()?.last_name ?: ""}"
            tvFavouriteTitle.setOnClickListener {
                viewModel.getAllForm()
            }
            tvSyncRequestTitle.setOnClickListener {
                viewModel.getListSyncRequest()
            }
        }
    }

    val headers = generateRandomHeaderList(6)
    val sheets = List(1) { generateRandomSheet() }
    val processSteps = List(2) { generateRandomProcessStep() }
    fun addForm() {
        viewModel.insertForm(
            FormEntity(
                formID = Random.nextLong(1, 1000),
                name = listOf(
                    "HRM form test",
                    "Factory form test",
                    "Parameter form test",
                    "Office form",
                    "Other form"
                ).random(),
                status = listOf(1, 2, 3, 4, 5).random().toString(),
                form_code = randomString(10),
                schema_code = randomString(10),
                category = listOf("HRM", "Factory", "Parameter", "Office", "Other").random(),
                schema_name = randomString(10),
                list_body = sheets,
                list_header = headers,
                process_steps = processSteps
            )
        )

    }

    override fun onResume() {
//        syncForm()
        viewModel.getListSyncRequest()
        super.onResume()
    }

    private fun syncUser() = if (AppPreferences.getCountRecordLocalUser() == 0) {
        viewModel.getListUser(true)
    } else {
        viewModel.getListUser(false)
    }
    private fun syncForm() = if(AppPreferences.getCountRecordLocalForm() == 0){
        viewModel.getListRemoteForm(true)
    }else{
        viewModel.getListRemoteForm(false)
    }

    private val sliderRunnable = Runnable {
        binding.vpTopViewpager.currentItem += 1
    }

    override fun setUpObserver() {

        observeLiveDataChanged(viewModel.listFormRemote){
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testAPiForm countRecord ${it.data?.data}")
                    if (it.data?.data?.isNotEmpty() == true){
                        viewModel.upsertListForm(it.data?.data ?: arrayListOf())
                    }
                },
                onError = {
                    processSyncDialog.hide()
                    showMessageOnSyncDataSuccess(requireContext(), false)
                }
            )
        }

        observeLiveDataChanged(viewModel.upsertListFormToLocal){
            it.onResultReceived(
                onLoading = {
                    processSyncDialog.show()
                },
                onSuccess = {
                    processSyncDialog.hide()
                    WTF("testAPiForm upsert-OK")
                    showMessageOnSyncDataSuccess(requireContext(), true)
                    viewModel.getCountFormRecord()
                },
                onError = {
                    processSyncDialog.hide()
                    showMessageOnSyncDataSuccess(requireContext(), false)
                }
            )
        }
        observeLiveDataChanged(viewModel.checkCountFormRecord){
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testAPiForm countRecord ${it.data}")
                    AppPreferences.setCountRecordLocalForm(it.data ?:0)
                    viewModel.getAllForm()
                },
                onError = {}
            )
        }

        //sync user

        observeLiveDataChanged(viewModel.getListUser) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testApiUser getRemote ${it.data?.data}")
                    if (it.data?.data?.isEmpty() != true) {
                        it.data?.data.let {
                            viewModel.addListUser(
                                it ?: arrayListOf()
                            )
                        }
                    }
                },
                onError = {
                    processSyncDialog.hide()
                    setupShowDialogResult(false, it.errorCode)
                    showMessageOnSyncDataSuccess(requireContext(), false)
                }
            )
        }

        observeLiveDataChanged(viewModel.checkUserTable) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testApiUser count ${it.data}")
                    AppPreferences.setCountRecordLocalUser(it.data ?: 0)
                },
                onError = {}
            )
        }

        observeLiveDataChanged(viewModel.insertListUserToLocal) {
            it.onResultReceived(
                onLoading = {
                    processSyncDialog.show()
                },
                onSuccess = {
                    processSyncDialog.hide()
                    showMessageOnSyncDataSuccess(requireContext(), true)
                    WTF("testApiUser -----------------sync ok")
                    viewModel.getCountUserLocal()
                },
                onError = {
                    processSyncDialog.hide()
                    showMessageOnSyncDataSuccess(requireContext(), false)
                }
            )
        }

        observeLiveDataChanged(viewModel.allForm) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.let { listForm ->
                        formHorizontalAdapter.submitListForm(listForm)
                    }
//                    if ((it.data?.size ?: 0) < 3){
//                        addForm()
//                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
        observeLiveDataChanged(viewModel.listRequestUpLoad) {
            it.onResultReceived(
                onLoading = {
                            //show loading in section
                },
                onSuccess = {
                    it.data?.let { listRequest ->
                        requestHorizontalAdapter.submitListForm(listRequest)
                    }
                },
                onError = {
                    //
                }
            )
        }
        observeLiveDataChanged(viewModel.allRequest) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    it.data?.let { allRequest ->
                       val listRequestCode = allRequest.map { it.requestCode }
                       viewModel.getRequestStatus(
                           PushRequestCodeBody(
                               requests = listRequestCode
                           )
                       )
                    }
                },
                onError = {
                    //
                }
            )
        }
        observeLiveDataChanged(viewModel.getRequestStatus) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    it.data?.data?.let { listUpdateStatus ->
                       viewModel.updateRequestStatuses(convertToPairs(listUpdateStatus))
                    }
                },
                onError = {
                    //
                }
            )
        }
        observeLiveDataChanged(viewModel.updateRequestStatuses) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("Update")
                },
                onError = {
                    //
                }
            )
        }

        observeLiveDataChanged(viewModel.formId) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    hideProgressDialog()
                    viewModel.getAllForm()
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }

        viewModel.listUploadRequest.observe(viewLifecycleOwner) { requestList ->
            // Handle the updated list here
            if (requestList.isEmpty()) {
                // Handle empty list scenario
            } else {
                // Handle non-empty list, call api, update UI, etc.
                viewModel.pushRequest(
                    listOf(
                        viewModel.listUpload.first().let {
                            RequestBodyPush(
                                device_code = AppPreferences.getDeviceInfo()?.id ?: "",
                                json_data = convertRequestEntityToString(it.copy(
                                    createdBy = UserTHPEntity(
                                        personal_number = AppPreferences.getUserInfo()?.employee_sap_number,
                                        initial =  AppPreferences.getUserInfo()?.employee_number,
                                        first_name = AppPreferences.getUserInfo()?.first_name,
                                        last_name = AppPreferences.getUserInfo()?.last_name,
                                        email = AppPreferences.getUserInfo()?.email,
                                        position_name = AppPreferences.getUserInfo()?.position_name,
                                        organization_number = AppPreferences.getUserInfo()?.organization_id
                                    ),
                                ) ?: return@observe),
                                request_code = it.requestCode,
                                version = it.version?.plus(1.0F).toString(),
                                status = 0,
                                process_id = it.process_id.toString(),
                                form_structure_id = it.formID.toLong(),
                            )
                        }
                    )
                )
            }
        }

        observeLiveDataChanged(viewModel.pushRequest) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = { result ->
                    if (viewModel.listUpload.isNotEmpty() && result.data != null) {
                        val item = viewModel.listUpload.find {
                            it.requestCode == result.data.data?.firstOrNull()?.request_code
                        }
                        item?.let { requestMatch ->
                            viewModel.updateSyncStatus(
                                requestMatch.requestCode,
                                is_sync = true,
                                status = requestMatch.status ?: 0
                            )
                            requestHorizontalAdapter.refreshRemoveItem(requestMatch)
                            viewModel.updateListUploadRequest(requestMatch, false)
                        }
                        showMessageToast(
                            requireContext(),
                            true,
                            "Upload yêu cầu thành công!",
                            ""
                        )
                    }
                },
                onError = {
                    setupShowDialogResult(false, it.errorCode)
                }
            )
        }

    }

    fun convertToPairs(list: List<RequestCodeResult>): List<Pair<String, Int>> {
        return list.map { result ->
            Pair(result.id ?: "", result.status ?: 0)
        }
    }

    private fun setupTopViewPager(listHotNew: List<News>?) {
        if (listHotNew?.isNotEmpty() == true) {
            ViewPagerHelper(
                viewPager2 = binding.vpTopViewpager,
                defaultPos = 0,
                viewPagerAdapter = ViewPagerAdapter(
                    data = listHotNew ?: return,
                    viewPager2 = binding.vpTopViewpager,
                    onItemClick = {
                        //set event on click
                    }
                ),
                onPageChanged = {}
            ).autoScroll(lifecycleScope, 3000).execute()
        }
    }

    private fun setupShowDialogConfirm(form: FormEntity) {
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = "Tạo yêu cầu mới",
                subTitle = form.title,
                content = "Bạn muốn tạo một yêu cầu mới với mẫu form: \n${form.form_code} - ${form.name}",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Tạo yêu cầu",
                isSingle = false
            )
        ).apply {
            onRightButtonClick {
                startActivity(
                    RequestMasterDetailActivity.newIntent(
                        requireContext(),
                        category = Constants.CategoryRequestDetail.ADD,
                        request = null,
                        form = form
                    )
                )
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    private fun setupRcvFavouriteForm() {
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        formHorizontalAdapter = FormHorizontalAdapter(
            requireContext(),
            arrayListOf(),
            onItemClickListener = {
                setupShowDialogConfirm(it)
            }
        )
//        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                viewModel.loadMoreSearch(totalPages)
//            }
//        }
        binding.rcvListPopularForm.apply {
            adapter = formHorizontalAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }
    }

    private fun setupRcvSyncRequest() {
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        requestHorizontalAdapter = RequestHorizontalAdapter(
            requireContext(),
            arrayListOf(),
            onItemClickListener = {
                if (!viewModel.listUpload.contains(it)) {
                    viewModel.updateListUploadRequest(it, true)
                } else return@RequestHorizontalAdapter
//                requestHorizontalAdapter.refreshItem(it)
            }
        )
//        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                viewModel.loadMoreSearch(totalPages)
//            }
//        }
        binding.rcvListSyncRequest.apply {
            adapter = requestHorizontalAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }
    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

}