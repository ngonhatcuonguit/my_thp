package com.cuongngo.core_project.ui.request_detail

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.databinding.ActivityRequestMasterBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.list_request.adapter.RequestAdapter
import com.cuongngo.core_project.ui.search_form.FormViewModel
import kotlin.random.Random

class RequestDetailMasterActivity : AppBaseActivityMVVM<ActivityRequestMasterBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_request_master


    companion object{
        val TAG = RequestDetailMasterActivity::class.java.simpleName
    }

    fun newIntent(
        context: Context,
        formCode: String,
        requestCode: String,
    ): Intent {
        return Intent(context, RequestDetailMasterActivity::class.java).apply {
            putExtra(RequestDetailActivity.FORM_CODE_KEY, formCode)
            putExtra(RequestDetailActivity.REQUEST_CODE_KEY, requestCode)
        }
    }

    private val formCode by lazy { intent.getStringExtra(RequestDetailActivity.FORM_CODE_KEY) ?: "" }
    private val requestCode by lazy { intent.getStringExtra(RequestDetailActivity.REQUEST_CODE_KEY) ?: "" }
    private var formEntity: FormEntity? = null
    private var addRequestCode: String? = null

    private lateinit var requestAdapter: RequestAdapter

    override fun setUp() {
        if (formCode.isEmpty()){
            viewModel.getRequestByCode(requestCode)
        }else{
            viewModel.getFormByCode(formCode)
        }
        setupRecycleViewListRequest()
        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.form) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    it.data?.let { form ->
                        formEntity = form
                        addRequestCode = randomString(10)
                        viewModel.upsertRequest(
                            RequestEntity(
                                requestID = Random.nextLong(1, 1000),
                                requestName = randomString(40),
                                formCode = formCode,
                                requestCode = addRequestCode ?:"",
                                formValue = form.formSchema,
                                requestStatus = Random.nextInt(1, 6)
                            )
                        )
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
        observeLiveDataChanged(viewModel.requestId){
            it.onResultReceived(
                onLoading = {
                    //
                },
                onSuccess = {
                    addRequestCode?.let { code -> viewModel.getRequestByCode(code) }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
        observeLiveDataChanged(viewModel.listRequest) {
            it.onResultReceived(
                onLoading = {
                    //
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data.let { listRequest ->
                        binding.apply {

                        }
                        requestAdapter.submitListRequest(listRequest)
                        WTF(RequestDetailActivity.TAG, "listRequest: ${listRequest}")
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }

    }

    private fun setupRecycleViewListRequest() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        requestAdapter = RequestAdapter(
            arrayListOf(),
            onItemClickListener = {
                startActivity(
                    RequestDetailActivity().newIntentDetail(
                        this,
                        it.requestID
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