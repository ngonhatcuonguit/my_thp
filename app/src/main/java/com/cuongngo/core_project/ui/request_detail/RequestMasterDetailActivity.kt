package com.cuongngo.core_project.ui.request_detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmAddRequestDialog
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Body
import com.cuongngo.core_project.data.database.roomdb.entity.randomBoolean
import com.cuongngo.core_project.data.database.roomdb.entity.randomDate
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.databinding.ActivityRequestMasterBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.bottom_sheet.MultiChoiceOptionBottomSheet
import com.cuongngo.core_project.ui.bottom_sheet.SearchUserBottomSheet
import com.cuongngo.core_project.ui.form_schema.RequestViewModel
import com.cuongngo.core_project.ui.request_detail.adapter.FormHeaderAdapter
import com.cuongngo.core_project.ui.request_detail.adapter.RequestProcessStepAdapter
import com.cuongngo.core_project.ui.request_detail.adapter.SheetAdapter
import com.cuongngo.core_project.ui.user_thp.adapter.UserAddedAdapter
import com.cuongngo.core_project.utils.Constants.CategoryRequestDetail.Companion.ADD
import com.cuongngo.core_project.utils.getScreenHeight
import com.google.gson.Gson
import java.io.IOException
import kotlin.random.Random

class RequestMasterDetailActivity :
    AppBaseActivityMVVM<ActivityRequestMasterBinding, RequestViewModel>() {

    override val viewModel: RequestViewModel by kodeinViewModel()
    override fun inflateLayout(): Int = R.layout.activity_request_master

    companion object {
        val TAG = RequestMasterDetailActivity::class.java.simpleName
        const val FORM_DATA_KEY = "FORM_DATA_KEY"
        const val REQUEST_DATA_KEY = "REQUEST_DATA_KEY"
        const val CATEGORY_KEY = "CATEGORY_KEY"
        const val RESULT_DATA = "RESULT_DATA"
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

    private lateinit var sheetAdapter: SheetAdapter
    private lateinit var requestProcessStepAdapter: RequestProcessStepAdapter
    private lateinit var formHeaderAdapter: FormHeaderAdapter
    private lateinit var informerAdapter: UserAddedAdapter

    override fun onBackPressed() {
        val resultIntent = Intent().apply {
            putExtra(RESULT_DATA, viewModel.requestEntity)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        super.onBackPressed()
    }

    override fun setUp() {
        //setup rcv
        setupRecycleViewListSheet()
        setupRecycleViewListProcessStep()
        setupRecycleViewFormHeader()
        setupRecyclerViewInformer()

        when (category) {
            ADD -> {
                viewModel.apply {
                    formEntity = intent.getSerializableExtra(FORM_DATA_KEY) as FormEntity ?: null
                    addRequestCode = randomString(10)
                    viewModel.insertRequest(
                        RequestEntity(
                            requestID = Random.nextLong(1, 1000),
                            requestName = listOf(
                                "HRM form test-IT-Ngô Nhật Cường-43950-Ngày 24/06/2024",
                                "Factory form test-IT-Ngô Nhật Cường-43950-Ngày 24/06/2024",
                                "Parameter form test-IT-Ngô Nhật Cường-43950-Ngày 24/06/2024",
                                "Office form-IT-Ngô Nhật Cường-43950-Ngày 24/06/2024",
                                "Other form-IT-Ngô Nhật Cường-43950-Ngày 24/06/2024"
                            ).random(),
                            formCode = formEntity?.formCode ?: "",
                            requestCode = addRequestCode ?: "",
                            listHeader = formEntity?.listHeader,
                            listBody = formEntity?.listBody,
                            processSteps = formEntity?.processSteps,
                            requestStatus = Random.nextInt(1, 6)
                        )
                    )
                }
            }

            else -> {
                viewModel.apply {
                    requestEntity =
                        intent.getSerializableExtra(REQUEST_DATA_KEY) as RequestEntity ?: null
                    requestEntity?.formCode?.let { viewModel.getFormByCode(it) }
                    sheetAdapter.submitListSheet(requestEntity?.listBody)
                    requestProcessStepAdapter.submitListProcessStep(requestEntity?.processSteps)
                    formHeaderAdapter.submitListFormHeader(requestEntity?.listHeader)
                    logEntityToFile()
                    WTF("log_json_Entity: ${readLogFile()}")
                }
            }
        }

        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
            layoutInformer.tvTitle.text = "Informer"
            layoutInformer.tvHint.setOnClickListener {
                //search user bottom sheet show
                showSearchUserBottomSheet()
            }

            edtRequestName.tvTitle.text = "Tên yêu cầu/ Request Name"
            edtRequestDescription.tvTitle.text = "Mô tả yêu cầu/ Request  description"

            edtRequestName.edtValue.hint = "Nhập tên yêu cầu"
            edtRequestDescription.edtValue.hint = "Nhập mô tả yêu cầu"
            flAddNew.setOnClickListener {
                viewModel.formEntity?.let { form ->
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
                        viewModel.formEntity = form
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
            }, onError = {
                hideProgressDialog()
            })
        }
        observeLiveDataChanged(viewModel.request) {
            it.onResultReceived(onLoading = {
                //
            }, onSuccess = {
                it.data.let { request ->
                    if (viewModel.requestEntity != null) {
                        viewModel.requestEntity = request
                        sheetAdapter.submitListSheet(request?.listBody)
                    } else {
                        viewModel.requestEntity = request
                        sheetAdapter.submitListSheet(request?.listBody)
                        requestProcessStepAdapter.submitListProcessStep(request?.processSteps)
                        formHeaderAdapter.submitListFormHeader(request?.listHeader)
                    }
                }
                logEntityToFile()
                WTF("log_json_Entity: ${readLogFile()}")
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
                    viewModel.requestEntity?.requestCode?.let { code ->
                        viewModel.getRequestByCode(
                            code
                        )
                    }
                }
            }, onError = {
                hideProgressDialog()
            })
        }
    }

    private fun logEntityToFile() {
        try {
            val json = Gson().toJson(viewModel.requestEntity)
            this.openFileOutput("log.txt", Context.MODE_PRIVATE).use { fos ->
                fos.write(json.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readLogFile(): String? {
        return try {
            this.openFileInput("log.txt").bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun setupRecycleViewListSheet() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        sheetAdapter = SheetAdapter(
            this,
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

    private fun setupRecyclerViewInformer(){
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        informerAdapter = UserAddedAdapter(
            arrayListOf(),
            onItemSelected = {
                // action show tooltip
            },
            onAddListener = {
                showSearchUserBottomSheet()
            },
            onRemoveListener = {
                informerAdapter.onRemoveItem(it)
                if (informerAdapter.itemCount == 1){
                    binding.layoutInformer.tvHint.text = "Tìm kiếm user"
                    binding.layoutInformer.rvListAdded.isVisible = false
                }else{
                    binding.layoutInformer.rvListAdded.isVisible = true
                    binding.layoutInformer.tvHint.text = ""
                }
            }
        )
        binding.layoutInformer.rvListAdded.apply {
            layoutManager = gridLayoutManager
            adapter = informerAdapter
        }
    }

    private fun upsertRequest() {
        //test update value
        viewModel.requestEntity?.let {
            viewModel.upsertRequest(it)
        }
    }

    private fun showSearchUserBottomSheet(){
        SearchUserBottomSheet(
            requestData = viewModel.requestEntity,
            heightValue = (getScreenHeight() * 0.95).toInt()
        ).setOnUserSelected {
            it?.let { data -> informerAdapter.onAddNew(data)}
            if (informerAdapter.itemCount == 1){
                binding.layoutInformer.tvHint.text = "Tìm kiếm user"
                binding.layoutInformer.rvListAdded.isVisible = false
            }else{
                binding.layoutInformer.rvListAdded.isVisible = true
                binding.layoutInformer.tvHint.text = ""
            }
        }.show(supportFragmentManager, TAG)
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
            ),
            viewModel
        ).apply {
            onRightButtonClick {
                var listRQ = viewModel.requestEntity?.listBody?.toMutableList() ?: mutableListOf()
                listRQ.add(
                    Body(
                        id = Random.nextLong(1, 1000),
                        type = listOf("Sheet", "Tần suất", "Nhiều tờ", "Other").random(),
                        listField = form.listBody?.firstOrNull()?.listField,
                        isDone = false,
                        name = viewModel.edtSheetName.toString() ?: return@onRightButtonClick,
                        formCode = form.formCode,
                        formName = form.name,
                        requestCode = viewModel.requestEntity?.requestCode,
                        created = randomDate(),
                        updated = randomDate(),
                        deleted = if (randomBoolean()) randomDate() else null,
                    )
                )
                viewModel.requestEntity?.requestID?.let {
                    viewModel.updateListSheet(
                        requestID = it,
                        listBody = listRQ
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