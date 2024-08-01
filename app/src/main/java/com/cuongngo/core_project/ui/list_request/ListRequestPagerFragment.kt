package com.cuongngo.core_project.ui.list_request

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.FragmentRequestPagerBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.list_request.adapter.CatrgoryPagerAdapter.Companion.MY_REQUEST
import com.cuongngo.core_project.ui.list_request.adapter.RequestAdapter
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.cuongngo.core_project.utils.Constants.CategoryRequestDetail.Companion.EDIT

class ListRequestPagerFragment : BaseFragmentMVVM<FragmentRequestPagerBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    override fun inflateLayout() = R.layout.fragment_request_pager

    private var category: String = MY_REQUEST
    private var isCalledApi = false
    private lateinit var requestAdapter: RequestAdapter

    companion object {
        val TAG = ListRequestPagerFragment::class.simpleName
        const val requestCodeForResult = 12345
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
        //
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.allRequest) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data.let { listRequest ->
                        requestAdapter.submitListRequest(listRequest)
                        WTF("listRequest: ${listRequest}")
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    //test
    private val requestDetailResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                WTF("test_result")
                viewModel.getAllRequest()
            }
        }

    private fun setupRecycleViewListRequest() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        requestAdapter = RequestAdapter(
            arrayListOf(),
            //test
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