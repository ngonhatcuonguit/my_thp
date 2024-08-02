package com.cuongngo.core_project.ui.request_detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmAddRequestDialog
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Sheet
import com.cuongngo.core_project.data.database.roomdb.entity.randomBoolean
import com.cuongngo.core_project.data.database.roomdb.entity.randomDate
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivityRequestMasterBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.request_detail.adapter.FormHeaderAdapter
import com.cuongngo.core_project.ui.request_detail.adapter.RequestProcessStepAdapter
import com.cuongngo.core_project.ui.request_detail.adapter.SheetAdapter
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.cuongngo.core_project.utils.Constants.CategoryRequestDetail.Companion.ADD
import kotlin.random.Random

class RequestMasterDetailActivity :
    AppBaseActivityMVVM<ActivityRequestMasterBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()
    override fun inflateLayout(): Int = R.layout.activity_request_master

    companion object {
        val TAG = RequestMasterDetailActivity::class.java.simpleName
        const val FORM_DATA_KEY = "FORM_DATA_KEY"
        const val REQUEST_DATA_KEY = "REQUEST_DATA_KEY"
        const val CATEGORY_KEY = "CATEGORY_KEY"

        fun newIntent(
            context: Context,
            category: String?,
            form: FormEntity?,
            request: RequestEntity?
        ): Intent {
            return Intent(context, RequestMasterDetailActivity::class.java).apply {
                putExtra(FORM_DATA_KEY, form)
                putExtra(REQUEST_DATA_KEY, request)
                putExtra(CATEGORY_KEY, category)
            }
        }
    }

    private val category by lazy { intent.getStringExtra(CATEGORY_KEY) ?: "" }
    private var addRequestCode: String? = null
    private var requestEntity: RequestEntity? = null
    private var formEntity: FormEntity? = null

    private lateinit var sheetAdapter: SheetAdapter
    private lateinit var requestProcessStepAdapter: RequestProcessStepAdapter
    private lateinit var formHeaderAdapter: FormHeaderAdapter

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }

    override fun setUp() {
        //setup rcv
        setupRecycleViewListSheet()
        setupRecycleViewListProcessStep()
        setupRecycleViewFormHeader()

        when (category) {
            ADD -> {
                formEntity = intent.getSerializableExtra(FORM_DATA_KEY) as FormEntity ?: null
                addRequestCode = randomString(10)
                viewModel.insertRequest(
                    RequestEntity(
                        requestID = Random.nextLong(1, 1000),
                        requestName = randomString(40),
                        formCode = formEntity?.formCode ?: "",
                        requestCode = addRequestCode ?: "",
                        formHeader = formEntity?.listHeader,
                        listSheet = formEntity?.listSheet,
                        processStep = formEntity?.processStep,
                        requestStatus = Random.nextInt(1, 6)
                    )
                )
                WTF("addRQ ${formEntity?.listSheet?.firstOrNull()?.listField}")
            }

            else -> {
                requestEntity =
                    intent.getSerializableExtra(REQUEST_DATA_KEY) as RequestEntity ?: null
                requestEntity?.formCode?.let { viewModel.getFormByCode(it) }
                sheetAdapter.submitListSheet(requestEntity?.listSheet)
                requestProcessStepAdapter.submitListProcessStep(requestEntity?.processStep)
                formHeaderAdapter.submitListFormHeader(requestEntity?.formHeader)
                WTF("viewRQ ${requestEntity?.listSheet?.size}")
            }
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
            flAddNew.setOnClickListener {
                formEntity?.let { form ->
                    setupShowDialogConfirm(form)
                }
            }
        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.form) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    it.data?.let { form ->
                        this.formEntity = form
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
        observeLiveDataChanged(viewModel.requestId) {
            it.onResultReceived(onLoading = {
                showProgressDialog()
            }, onSuccess = {
                addRequestCode?.let { code -> viewModel.getRequestByCode(code) }
                WTF("listRequest1: ${addRequestCode}")
            }, onError = {
                hideProgressDialog()
            })
        }
        observeLiveDataChanged(viewModel.request) {
            it.onResultReceived(onLoading = {
                //
            }, onSuccess = {
                it.data.let { request ->
                    if (requestEntity != null) {
                        sheetAdapter.submitListSheet(request?.listSheet)
                    } else {
                        requestEntity = request
                        sheetAdapter.submitListSheet(request?.listSheet)
                        requestProcessStepAdapter.submitListProcessStep(request?.processStep)
                        formHeaderAdapter.submitListFormHeader(request?.formHeader)
                    }
                }
                hideProgressDialog()
            }, onError = {
                hideProgressDialog()
            })
        }
        observeLiveDataChanged(viewModel.requestUpdate) {
            it.onResultReceived(onLoading = {
                showProgressDialog()
            }, onSuccess = {
                it.data.let { id ->
                    requestEntity?.requestCode?.let { code -> viewModel.getRequestByCode(code) }
                }
            }, onError = {
                hideProgressDialog()
            })
        }
    }

    private fun setupRecycleViewListSheet() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        sheetAdapter = SheetAdapter(
            arrayListOf(),
            onItemClickListener = {
                startActivity(
                    SheetDetailActivity().newIntent(
                        this,
                        it.requestCode ?: "",
                        it
                    )
                )
            })
        binding.rvRequestSheet.apply {
            layoutManager = gridLayoutManager
            adapter = sheetAdapter
        }
    }

    private fun setupRecycleViewListProcessStep() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        requestProcessStepAdapter = RequestProcessStepAdapter(
            arrayListOf(),
            onItemClickListener = {
                // update data
            })
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
            })
        binding.rvHeader.apply {
            layoutManager = gridLayoutManager
            adapter = formHeaderAdapter
        }
    }

    private fun upsertRequest() {
        //test update value
        requestEntity?.let {
            viewModel.upsertRequest(it)
        }
    }

    private fun setupShowDialogConfirm(form: FormEntity) {
        val confirmAddRequestDialog = ConfirmAddRequestDialog(
            DialogModel(
                title = "Thêm sheet mới",
                subTitle = form.title,
                content = "Bạn muốn thêm 1 sheet mới cho yêu cầu: ${form.formCode} - ${form.name} \nVui lòng nhập tên sheet hoặc tần suất vào bên dưới",
                leftButtonTitle = "Huỷ bỏ",
                edtTitle = "Tên sheet hoặc tần suất",
                edtHint = "Nhập tên sheet/tần suất",
                rightButtonTitle = "Thêm",
                isSingle = false
            )
        ).apply {
            onRightButtonClick {
                var listRQ = requestEntity?.listSheet?.toMutableList() ?: mutableListOf()
                listRQ.add(
                    Sheet(
                        id = Random.nextLong(1, 1000),
                        type = listOf("Sheet", "Tần suất", "Nhiều tờ", "Other").random(),
                        listField = form.listSheet?.firstOrNull()?.listField,
                        isDone = false,
                        name = AppPreferences.getDialogEdtValue(),
                        formCode = form.formCode,
                        formName = form.name,
                        requestCode = requestEntity?.requestCode,
                        created = randomDate(),
                        updated = randomDate(),
                        deleted = if (randomBoolean()) randomDate() else null,
                    )
                )
                requestEntity?.requestID?.let {
                    viewModel.updateListSheet(
                        requestID = it,
                        listSheet = listRQ
                    )
                }
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmAddRequestDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

}