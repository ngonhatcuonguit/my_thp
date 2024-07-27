package com.cuongngo.core_project.ui.request_detail

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
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
import com.cuongngo.core_project.ui.request_detail.adapter.FormHeaderAdapter
import com.cuongngo.core_project.ui.request_detail.adapter.RequestProcessStepAdapter
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
    private lateinit var requestProcessStepAdapter: RequestProcessStepAdapter
    private lateinit var formHeaderAdapter: FormHeaderAdapter

    override fun setUp() {
        if (formCode.isEmpty()){
            viewModel.getRequestByCode(requestCode)
        }else{
            viewModel.getFormByCode(formCode)
        }
        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
            edtInformer.tvTitle.text = "Informer"
            edtRequestName.tvTitle.text = "Tên yêu cầu/ Request Name"
            edtRequestDescription.tvTitle.text = "Mô tả yêu cầu/ Request  description"

            edtRequestName.edtValue.hint = "Nhập tên yêu cầu"
            edtRequestDescription.edtValue.hint = "Nhập mô tả yêu cầu"
            edtInformer.edtValue.hint = "Nhập email của informer"
        }

        //setup rcv
        setupRecycleViewListRequest()
        setupRecycleViewListProcessStep()
        setupRecycleViewFormHeader()
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
                                formHeader = form.formHeaderSchema,
                                formBody = form.formBodySchema,
                                processStep = form.processStep,
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
                        requestProcessStepAdapter.submitListProcessStep(listRequest?.firstOrNull()?.processStep)
                        formHeaderAdapter.submitListFormHeader(listRequest?.firstOrNull()?.formHeader)
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
        binding.rvRequestSheet.apply {
            layoutManager = gridLayoutManager
            adapter = requestAdapter
        }
    }
    private fun setupRecycleViewListProcessStep() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        requestProcessStepAdapter = RequestProcessStepAdapter(
            arrayListOf(),
            onItemClickListener = {
                // update data
            }
        )
        binding.rvProcessStep.apply {
            layoutManager = gridLayoutManager
            adapter = requestProcessStepAdapter
        }
    }
    private fun setupRecycleViewFormHeader() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        formHeaderAdapter = FormHeaderAdapter(
            arrayListOf(),
            onItemClickListener = {
                // action
            }
        )
        binding.rvHeader.apply {
            layoutManager = gridLayoutManager
            adapter = formHeaderAdapter
        }
    }

}