package com.cuongngo.core_project.ui.list_request

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.databinding.FragmentRequestPagerBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.list_request.adapter.CategoryPagerAdapter.Companion.ALL_REQUEST
import com.cuongngo.core_project.ui.list_request.adapter.CategoryPagerAdapter.Companion.COMPLETED
import com.cuongngo.core_project.ui.list_request.adapter.CategoryPagerAdapter.Companion.REQUEST_CATEGORY
import com.cuongngo.core_project.ui.list_request.adapter.CategoryPagerAdapter.Companion.SENT_REQUEST
import com.cuongngo.core_project.ui.list_request.adapter.RequestAdapter
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity.Companion.RESULT_DATA
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.cuongngo.core_project.utils.Constants.CategoryRequestDetail.Companion.EDIT

class ListRequestPagerFragment : BaseFragmentMVVM<FragmentRequestPagerBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    override fun inflateLayout() = R.layout.fragment_request_pager

    private val category by lazy { arguments?.getString(REQUEST_CATEGORY) ?: ALL_REQUEST }
    private var isCalledApi = false
    private lateinit var requestAdapter: RequestAdapter

    companion object {
        val TAG = ListRequestPagerFragment::class.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateData(category)
    }

    override fun setUp() {
        viewModel.getAllRequest()
        setupRecycleViewListRequest()
    }

    fun updateData(category: String) {
        when(category){
            ALL_REQUEST -> {}
            SENT_REQUEST -> {}
            COMPLETED -> {}
        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.allRequest) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {listAll ->
                    hideProgressDialog()
                    val listSent: List<RequestEntity>? = listAll.data?.filter { it.status == 1 }
                    val listCompleted: List<RequestEntity>? = listAll.data?.filter {
                        it.status == 2 || it.status == 3
                    }
                    when(category){
                        ALL_REQUEST -> {
                            requestAdapter.submitListRequest(listAll.data)
                        }
                        SENT_REQUEST -> {
                            requestAdapter.submitListRequest(listSent)
                        }
                        COMPLETED -> {
                            requestAdapter.submitListRequest(listCompleted)
                        }
                        else -> {
                            requestAdapter.submitListRequest(listAll.data)
                        }
                    }
                    WTF("listRequestPager2: ${listAll} --- $listSent ------$listCompleted")
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private val requestDetailResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val returnedRequest = it.data?.getSerializableExtra(RESULT_DATA) as RequestEntity?
                    ?: return@registerForActivityResult
                WTF("result_request $returnedRequest")
                requestAdapter.refreshItem(returnedRequest)
            }
        }

    private fun setupRecycleViewListRequest() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        requestAdapter = RequestAdapter(
            requireContext(),
            arrayListOf(),
            onItemClickListener = {
                requestDetailResult.launch(
                    RequestMasterDetailActivity.newIntent(
                        requireContext(),
                        category = EDIT,
                        request = it,
                        form = null
                    )
                )
            }
        )
        binding.rvRequest.apply {
            layoutManager = gridLayoutManager
            adapter = requestAdapter
        }
    }

}