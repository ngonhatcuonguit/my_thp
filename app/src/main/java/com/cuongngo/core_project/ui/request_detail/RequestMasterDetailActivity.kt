package com.cuongngo.core_project.ui.request_detail

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmAddRequestDialog
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.view.date_time_picker.DatePickerDialog
import com.cuongngo.core_project.base.view.date_time_picker.Listener
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.enum.FieldType
import com.cuongngo.core_project.data.database.roomdb.entity.Body
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.ProcessStep
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.randomBoolean
import com.cuongngo.core_project.data.database.roomdb.entity.randomDate
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.databinding.ActivityRequestMasterBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.bottom_sheet.MultiChoiceOptionBottomSheet
import com.cuongngo.core_project.ui.bottom_sheet.SearchUserBottomSheet
import com.cuongngo.core_project.ui.bottom_sheet.SingleChoiceOptionBottomSheet
import com.cuongngo.core_project.ui.dropdown.onShowPopupOption
import com.cuongngo.core_project.ui.form_schema.RequestViewModel
import com.cuongngo.core_project.ui.request_detail.SheetDetailActivity.Companion.RESULT_BODY_DATA
import com.cuongngo.core_project.ui.request_detail.adapter.FormHeaderAdapter
import com.cuongngo.core_project.ui.request_detail.adapter.RequestProcessStepAdapter
import com.cuongngo.core_project.ui.request_detail.adapter.SheetAdapter
import com.cuongngo.core_project.ui.user_thp.adapter.UserAddedAdapter
import com.cuongngo.core_project.utils.Constants
import com.cuongngo.core_project.utils.Constants.CategoryRequestDetail.Companion.ADD
import com.cuongngo.core_project.utils.date.getCurrentDateTime
import com.cuongngo.core_project.utils.date.getCurrentHourOfDay
import com.cuongngo.core_project.utils.date.getCurrentMinuteOfHour
import com.cuongngo.core_project.utils.getScreenHeight
import com.cuongngo.core_project.utils.toast.showMessageSaveData
import com.google.gson.Gson
import java.io.IOException
import java.util.Calendar
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
    private var isOnBack: Boolean? = false

    private lateinit var sheetAdapter: SheetAdapter
    private lateinit var requestProcessStepAdapter: RequestProcessStepAdapter
    private lateinit var formHeaderAdapter: FormHeaderAdapter
    private lateinit var informerAdapter: UserAddedAdapter
    private var date: Calendar = Calendar.getInstance()

    override fun onBackPressed() {
        updateRequest()
        isOnBack = true
    }

    override fun setUp() {
        when (category) {
            ADD -> {
                viewModel.apply {
                    formEntity = intent.getSerializableExtra(FORM_DATA_KEY) as FormEntity ?: null
                    addRequestCode = randomString(10)
                    viewModel.insertRequest(
                        RequestEntity(
                            requestID = Random.nextLong(1, 1000),
                            formCode = formEntity?.form_code ?: "",
                            requestCode = addRequestCode ?: "",
                            formName = formEntity?.name + "-" + getCurrentDateTime(),
                            listHeader = formEntity?.list_header,
                            listBody = formEntity?.list_body,
                            processSteps = formEntity?.process_steps,
                            requestStatus = Random.nextInt(1, 6)
                        )
                    )
                }
                setupShowDefaultInfo(viewModel.requestEntity)
            }

            else -> {
                viewModel.apply {
                    requestEntity =
                        intent.getSerializableExtra(REQUEST_DATA_KEY) as RequestEntity ?: null
                    requestEntity?.formCode?.let { viewModel.getFormByCode(it) }
                    viewModel.requestEntity = requestEntity
                    logEntityToFile()
                    binding.tvRequestTitle.text = requestEntity?.requestName ?: "Tạo yêu cầu mới"
                    WTF("log_json_Entity: ${readLogFile()}")

                    setupShowDefaultInfo(viewModel.requestEntity)
                }
            }
        }

        with(binding) {
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
            layoutSubmitButton.btnSecond.setOnClickListener {
                updateRequest()
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
                    binding.tvRequestTitle.text = request?.requestName ?: "Tạo yêu cầu mới"
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
        observeLiveDataChanged(viewModel.requestUpdateListSheet) {
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

        observeLiveDataChanged(viewModel.updateRequest) {
            it.onResultReceived(onLoading = {
                showProgressDialog()
            }, onSuccess = {
                WTF("updateRQ ----Ok ${viewModel.requestEntity}")
                hideProgressDialog()
                if (isOnBack == true) {
                    val resultIntent = Intent().apply {
                        putExtra(RESULT_DATA, viewModel.requestEntity)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    super.onBackPressed()
                }
                showMessageSaveData(this, done = true)
            }, onError = {
                hideProgressDialog()
                showMessageSaveData(this, done = false)
            })
        }

    }

    private fun setupShowDefaultInfo(requestEntity: RequestEntity?) {
        with(binding) {
            requestEntity?.requestName?.let {
                edtRequestName.edtValue.setText(requestEntity.requestName.toString())
            }
            requestEntity?.requestDescription?.let {
                edtRequestDescription.edtValue.setText(requestEntity.requestDescription.toString())
            }
        }
        setupRecycleViewListSheet(requestEntity?.listBody ?: arrayListOf())
        setupRecycleViewListProcessStep(requestEntity?.processSteps ?: arrayListOf())
        setupRecycleViewFormHeader(requestEntity?.listHeader ?: arrayListOf())
        setupRecyclerViewInformer(requestEntity?.informer?.toMutableList() ?: arrayListOf())

        if (informerAdapter.itemCount == 1) {
            binding.layoutInformer.tvHint.text = "Tìm kiếm user"
            binding.layoutInformer.rvListAdded.isVisible = false
        } else {
            binding.layoutInformer.rvListAdded.isVisible = true
            binding.layoutInformer.tvHint.text = ""
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


    private val requestDetailBodyResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val returnedRequest = it.data?.getSerializableExtra(RESULT_BODY_DATA) as RequestEntity?
                    ?: return@registerForActivityResult
                viewModel.requestEntity = returnedRequest
                sheetAdapter.submitListSheet(viewModel.requestEntity!!.listBody)
            }
        }

    private fun setupRecycleViewListSheet(listSheet: List<Body>?) {
        val gridLayoutManager = GridLayoutManager(this, 2)
        sheetAdapter = SheetAdapter(
            this,
            listSheet ?: arrayListOf(),
            onItemClickListener = {
                cacheData()
                requestDetailBodyResult.launch(
                    SheetDetailActivity.newIntent(
                        this,
                        body = it,
                        requestEntity = viewModel.requestEntity
                    )
                )
            })
        binding.rvRequestSheet.apply {
            layoutManager = gridLayoutManager
            adapter = sheetAdapter
        }
    }

    private fun setupRecycleViewListProcessStep(listProcessStep: List<ProcessStep>?) {
        val gridLayoutManager = GridLayoutManager(this, 1)
        requestProcessStepAdapter = RequestProcessStepAdapter(
            this,
            listProcessStep ?: arrayListOf(),
            supportFragmentManager,
            onItemClickListener = {
                //test
            },
            onChangeProcessStep = {
                // change list Process
            }
        )
        binding.rvProcessStep.apply {
            layoutManager = gridLayoutManager
            adapter = requestProcessStepAdapter
        }
    }

    private fun setupRecycleViewFormHeader(listHeader: List<Field>?) {
        val gridLayoutManager = GridLayoutManager(this, 1)
        formHeaderAdapter = FormHeaderAdapter(
            listHeader ?: arrayListOf(),
            onItemClickListener = { field ->
                val defaultValue = field.options?.find {
                    field.value == it.value
                }
                when (field.type) {
                    "textarea" -> {
                        setupShowDialogChangeValue(field)
                    }

                    "date" -> {
                        showDatePickerDialog(field)
                    }

                    "time" -> {
                        showTimePickerDialog(field)
                    }

                    FieldType.SELECT.fileType, FieldType.RADIO_GROUP.fileType -> {
                        WTF("testOptions ${field.options}")
                        field.options?.let { options ->
                            onShowPopupOption(
                                this,
                                view = formHeaderAdapter.getItemRootView(field),
                                listOption = options,
                                optionDefault = defaultValue,
                                onSelectedListener = {
                                    handleChangeValueHeader(
                                        field,
                                        it.value
                                    )
                                })
                        }
                    }

                    "checkbox-group" -> {
                        showMultiChoiceBottomSheet(field)
                    }

                    "radio-group" -> {
                        showSingleChoiceBottomSheet(field)
                    }

                    else -> {
                        setupShowDialogChangeValue(field)
                    }
                }
            })
        binding.rvHeader.apply {
            layoutManager = gridLayoutManager
            adapter = formHeaderAdapter
        }
    }

    private fun setupRecyclerViewInformer(listInformer: List<UserTHPEntity>?) {
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        informerAdapter = UserAddedAdapter(
            this,
            listInformer ?: arrayListOf(),
            onItemSelected = {
                //show tool tip
            },
            onAddListener = {
                showSearchUserBottomSheet()
            },
            onRemoveListener = {
                informerAdapter.onRemoveItem(it)
                viewModel.requestEntity?.informer?.toMutableList()?.remove(it)
                if (informerAdapter.itemCount == 1) {
                    binding.layoutInformer.tvHint.text = "Tìm kiếm user"
                    binding.layoutInformer.rvListAdded.isVisible = false
                } else {
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

    private fun updateRequest() {
        with(binding) {
            var requestName = edtRequestName.edtValue.text.toString()
            var requestDescription = edtRequestDescription.edtValue.text.toString()
            var listInformer = informerAdapter.getListInformer() ?: null
            var listProcessStep = requestProcessStepAdapter.getListProcessStep()
            var listHeader = formHeaderAdapter.getListHeader()
            var listBody = sheetAdapter.getListBody()
            var currentRequest = viewModel.requestEntity?.copy(
                requestName = requestName,
                requestDescription = requestDescription,
                informer = listInformer,
                processSteps = listProcessStep,
                listHeader = listHeader,
                listBody = listBody,

                )
            viewModel.requestEntity = currentRequest
            viewModel.requestEntity?.let {
                viewModel.updateRequest(it)
            }
        }
    }

    private fun cacheData() {
        with(binding) {
            var requestName = edtRequestName.edtValue.text.toString()
            var requestDescription = edtRequestDescription.edtValue.text.toString()
            var listInformer = informerAdapter.getListInformer() ?: null
            var listProcessStep = requestProcessStepAdapter.getListProcessStep()
            var listHeader = formHeaderAdapter.getListHeader()
            var listBody = sheetAdapter.getListBody()
            var currentRequest = viewModel.requestEntity?.copy(
                requestName = requestName,
                requestDescription = requestDescription,
                informer = listInformer,
                processSteps = listProcessStep,
                listHeader = listHeader,
                listBody = listBody,

                )
            viewModel.requestEntity = currentRequest
        }
    }

    private fun showSearchUserBottomSheet() {
        SearchUserBottomSheet(
            requestData = viewModel.requestEntity,
            heightValue = (getScreenHeight() * 0.95).toInt()
        ).setOnUserSelected {
            it?.let { data ->
                if (viewModel.requestEntity?.informer?.contains(data) != true) {
                    viewModel.requestEntity?.informer?.toMutableList()?.add(data)
                    informerAdapter.onAddNew(data)
                    WTF("addUser ${viewModel.requestEntity?.informer}")
                } else {
                    //show warning
                }
            }
            if (informerAdapter.itemCount == 1) {
                binding.layoutInformer.tvHint.text = "Tìm kiếm user"
                binding.layoutInformer.rvListAdded.isVisible = false
            } else {
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
                content = "Bạn muốn thêm 1 sheet mới cho yêu cầu: ${form.form_code} - ${form.name} \nVui lòng nhập tên sheet hoặc tần suất vào bên dưới",
                leftButtonTitle = "Huỷ bỏ",
                edtTitle = "Tên sheet hoặc tần suất",
                edtHint = "Nhập tên sheet/tần suất",
                rightButtonTitle = "Thêm",
                isSingle = false
            ),
            viewModel
        ).apply {
            onRightButtonClick { sheetName ->
                var listRQ = viewModel.requestEntity?.listBody?.toMutableList() ?: mutableListOf()
                listRQ.add(
                    Body(
                        id = Random.nextLong(1, 1000),
                        type = listOf("Sheet", "Tần suất", "Nhiều tờ", "Other").random(),
                        list_field = form.list_body?.firstOrNull()?.list_field,
                        is_done = false,
                        name = sheetName,
                        form_code = form.form_code,
                        form_name = form.name,
                        request_code = viewModel.requestEntity?.requestCode,
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

    private fun showSingleChoiceBottomSheet(field: Field) {
        val defaultValue = field.options?.find {
            field.value == it.value
        }
        SingleChoiceOptionBottomSheet(
            field = field,
            optionDefault = defaultValue,
            heightValue = (getScreenHeight() * 0.85).toInt()
        ).setOnOptionSelected {
            handleChangeValueHeader(field, it?.value)
        }.show(supportFragmentManager, SheetDetailActivity.TAG)
    }

    private fun showMultiChoiceBottomSheet(field: Field) {
        MultiChoiceOptionBottomSheet(
            listOption = field.options,
            listSelectedDefault = emptyList(),
            (getScreenHeight() * 0.95).toInt()
        ).onOptionSelected { listSelected ->
            var displayText = ""
            listSelected?.forEach {
                displayText = if (displayText.isEmpty()) {
                    "${it.value}"
                } else {
                    "$displayText, ${it.value}"
                }
            }
            handleChangeValueHeader(field, displayText)
        }.show(supportFragmentManager, SheetDetailActivity.TAG)
    }

    private fun showDatePickerDialog(field: Field) {
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.YEAR, 5)
        val dateAdd: Calendar = Calendar.getInstance()
        dateAdd.add(Calendar.MINUTE, 5)
        DatePickerDialog(
            calendar = dateAdd,
            context = this,
            listener = object : Listener {
                override fun onDateSelected(calendar: Calendar) {
                    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                    val monthOfYear = calendar.get(Calendar.MONTH) + 1
                    val year = calendar.get(Calendar.YEAR)

                    val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                    val monthStr = if (monthOfYear < 10) "0${monthOfYear}" else "$monthOfYear"
                    handleChangeValueHeader(field, "$dayStr/$monthStr/$year")
                }
            },
            maxDate = cal.timeInMillis,
            minDate = dateAdd.timeInMillis,
            isCancelable = true
        ).show()
    }

    private fun showTimePickerDialog(field: Field) {
        val dateAdd: Calendar = Calendar.getInstance()
        dateAdd.add(Calendar.MINUTE, 10)
        TimePickerDialog(
            this,
            { _, hour, minute ->
                date.set(Calendar.HOUR_OF_DAY, hour)
                date.set(Calendar.MINUTE, minute)
                handleChangeValueHeader(field, "${hour.toString()}:${minute.toString()}")
            },
            dateAdd.get(Calendar.HOUR_OF_DAY) ?: getCurrentHourOfDay(),
            dateAdd.get(Calendar.MINUTE) ?: getCurrentMinuteOfHour(),
            true
        ).show()
    }

    private fun setupShowDialogChangeValue(field: Field) {
        var edtText = ""
        if (!field.value.isNullOrEmpty()) {
            edtText = field.value.toString()
        }
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = field.label.toString() ?: "Sửa dổi thông tin",
                subTitle = "subtitle",
                content = "Vui lòng nhập thông tin vào bên dưới và xác nhận để lưu vào biểu mẫu của bạn!",
                edtValue = edtText,
                edtHint = "Vui lòng nhập ${field.label.toString()}",
                edtTitle = "Nhập ${field.label.toString()}",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Lưu thông tin",
                isSingle = false
            )
        ).apply {
            onRightButtonClick {
                handleChangeValueHeader(field, it)
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    private fun handleChangeValueHeader(field: Field, newValue: String?) {
        val fieldData = viewModel.requestEntity?.listHeader?.find { it.id == field.id }
        val fieldIndex = viewModel.requestEntity?.listHeader?.indexOf(fieldData) ?: return
        fieldData?.value = newValue
        fieldData?.let { formHeaderAdapter.onChangeValueField(it, fieldIndex) }
    }


}