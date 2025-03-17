package com.cuongngo.my_thp.ui.request_detail

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.dialog_fragment.ConfirmDialog
import com.cuongngo.my_thp.base.model.DialogModel
import com.cuongngo.my_thp.base.view.date_time_picker.DatePickerDialog
import com.cuongngo.my_thp.base.view.date_time_picker.Listener
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.my_thp.common.enum.FieldType
import com.cuongngo.my_thp.data.database.roomdb.entity.Body
import com.cuongngo.my_thp.data.database.roomdb.entity.Field
import com.cuongngo.my_thp.data.database.roomdb.entity.RequestEntity
import com.cuongngo.my_thp.databinding.ActivitySheetDetailBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ext.observeLiveDataChanged
import com.cuongngo.my_thp.services.network.onResultReceived
import com.cuongngo.my_thp.ui.add_request.adapter.FieldAdapter
import com.cuongngo.my_thp.ui.bottom_sheet.MultiChoiceOptionBottomSheet
import com.cuongngo.my_thp.ui.bottom_sheet.SingleChoiceOptionBottomSheet
import com.cuongngo.my_thp.ui.form_schema.RequestViewModel
import com.cuongngo.my_thp.utils.date.getCurrentHourOfDay
import com.cuongngo.my_thp.utils.date.getCurrentMinuteOfHour
import com.cuongngo.my_thp.utils.getScreenHeight
import io.reactivex.disposables.Disposable
import java.util.Calendar

class SheetDetailActivity : AppBaseActivityMVVM<ActivitySheetDetailBinding, RequestViewModel>() {

    override val viewModel: RequestViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_sheet_detail

    companion object {
        val TAG = SheetDetailActivity::class.java.simpleName
        const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
        const val REQUEST_DATA_KEY = "REQUEST_DATA_KEY"
        const val SHEET_DATA_KEY = "SHEET_DATA_KEY"
        const val RESULT_BODY_DATA = "RESULT_BODY_DATA"

        fun newIntent(
            context: Context,
            body: Body,
            requestEntity: RequestEntity?
        ): Intent {
            return Intent(context, SheetDetailActivity::class.java).apply {
                putExtra(REQUEST_DATA_KEY, requestEntity)
                putExtra(SHEET_DATA_KEY, body)
            }
        }

    }

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null
    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true
    private var date: Calendar = Calendar.getInstance()

    private lateinit var fieldAdapter: FieldAdapter

    private val body by lazy { intent.getSerializableExtra(SHEET_DATA_KEY) as Body }
    private val requestEntity by lazy { intent.getSerializableExtra(RequestMasterDetailActivity.REQUEST_DATA_KEY) as RequestEntity }
    private var isDone = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableLightStatusBar()
    }

    override fun onBackPressed() {
        var lisFieldAfterChange = fieldAdapter.getListField()
        var currentData = viewModel.newRequestEntity?.listBody?.find {
            it.id == body.id && it.name == body.name
        }
        var index = viewModel.newRequestEntity?.listBody?.indexOf(currentData) ?: -1
        currentData?.list_field = lisFieldAfterChange
        viewModel.newRequestEntity?.listBody!!.toMutableList()[index] = currentData ?: body
        if (isDone) {
            viewModel.newRequestEntity?.listBody!!.toMutableList()[index].is_done = isDone ?: false
        } else {
            viewModel.newRequestEntity?.listBody!!.toMutableList()[index].is_done =
                isListDone(viewModel.newRequestEntity?.listBody!!.toMutableList()[index].list_field!!)
                    ?: false
        }
        WTF("testSheetDetail --${viewModel.newRequestEntity?.listBody!!.toMutableList()[index]}")

        val resultIntent = Intent().apply {
            putExtra(RESULT_BODY_DATA, viewModel.newRequestEntity)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        super.onBackPressed()
    }

    fun isListDone(listField: List<Field>): Boolean {
        val problematicFieldsCount = listField.count { field_data ->
            field_data.value.isNullOrBlank()
        } ?: 0
        return problematicFieldsCount <= 1
    }

    override fun setUp() {
        viewModel.newRequestEntity = requestEntity
        binding.request = requestEntity
        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
            tvFormTitle.text = body.form_name.toString()
            btnSaveDraft.setOnClickListener {
                isDone = true
                onBackPressed()
            }
        }
        setupRecycleViewListField()
        fieldAdapter.submitListField(body.list_field)
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.request) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.let { request ->
                        viewModel.newRequestEntity = request
                        WTF(TAG, "requestFormEntity: ${request.requestCode}")
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }

    }

    private fun setupRecycleViewListField() {
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        fieldAdapter = FieldAdapter(
            this,
            arrayListOf(),
            onItemClickListener = {
                //
            },
            onChangeValueListener = { fieldData ->
                val defaultValue = fieldData.options?.find {
                    fieldData.value == it.value
                }
                when (fieldData.type) {
                    "textarea" -> {
                        setupShowDialogChangeValue(fieldData)
                    }

                    "date" -> {
                        showDatePickerDialog(fieldData)
                    }

                    "time" -> {
                        showTimePickerDialog(fieldData)
                    }

                    "header" -> {
                        //
                    }

//                    FieldType.SELECT.fileType -> {
//                        fieldData.options?.let { options ->
//                            onShowPopupOption(
//                                this,
//                                view = fieldAdapter.getItemRootView(fieldData),
//                                listOption = options,
//                                optionDefault = defaultValue,
//                                onSelectedListener = {
//                                    handleChangeValueField(
//                                        fieldData,
//                                        it.value
//                                    )
//                                })
//                        }
//                    }

                    "checkbox-group" -> {
                        showMultiChoiceBottomSheet(fieldData)
                    }

                    FieldType.SELECT.fileType, FieldType.RADIO_GROUP.fileType -> {
                        showSingleChoiceBottomSheet(fieldData)
                    }

                    else -> {
                        setupShowDialogChangeValue(fieldData)
                    }
                }
            }
        )
        binding.rvListField.apply {
            layoutManager = gridLayoutManager
            isScrollContainer = false
            adapter = fieldAdapter
        }
    }


    private fun showSingleChoiceBottomSheet(field: Field) {
        val defaultValue = field.options?.find {
            field.value == it.value
        }
        SingleChoiceOptionBottomSheet(
            field = field,
            optionDefault = defaultValue,
            heightValue = (getScreenHeight() * 0.9).toInt()
        ).setOnOptionSelected {
            handleChangeValueField(field, it?.value)
        }.show(supportFragmentManager, TAG)
    }

    private fun showMultiChoiceBottomSheet(field: Field) {
        MultiChoiceOptionBottomSheet(
            listOption = field.options,
            listSelectedDefault = emptyList(),
            heightValue = (getScreenHeight() * 0.9).toInt(),
            field = field
        ).onOptionSelected { listSelected ->
            var displayText = ""
            listSelected?.forEach {
                displayText = if (displayText.isEmpty()) {
                    "${it.value}"
                } else {
                    "$displayText, ${it.value}"
                }
            }
            handleChangeValueField(field, displayText)
        }.show(supportFragmentManager, TAG)
    }

    private fun showDatePickerDialog(field: Field) {
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.YEAR, 5)
        val dateAdd: Calendar = Calendar.getInstance()
        dateAdd.add(Calendar.MINUTE, 5)
        val minDateCal: Calendar = Calendar.getInstance()
        minDateCal.add(Calendar.YEAR, -80)
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
                    handleChangeValueField(field, "$dayStr/$monthStr/$year")
                }
            },
            maxDate = cal.timeInMillis,
            minDate = minDateCal.timeInMillis,
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
                handleChangeValueField(field, "${hour.toString()}:${minute.toString()}")
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
                content = field.placeholder
                    ?: "Vui lòng nhập thông tin vào bên dưới và xác nhận để lưu vào biểu mẫu của bạn!",
                edtValue = edtText,
                edtHint = "Vui lòng nhập ${field.label.toString()}",
                edtTitle = "Nhập ${field.label.toString()}",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Lưu thông tin",
                isSingle = false,
                typeInput = field.type
            )
        ).apply {
            onRightButtonClick {
                hideKeyboard()
                handleChangeValueField(field, it)
                dismiss()
            }
            onLeftButtonClick {
                hideKeyboard()
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    private fun handleChangeValueField(field: Field, newValue: String?) {
        val fieldData = body.list_field?.find { it.id == field.id }
        val fieldIndex = body.list_field?.indexOf(fieldData) ?: return
        fieldData?.value = newValue
        fieldData?.let { fieldAdapter.onChangeValueField(it, fieldIndex) }
    }


}