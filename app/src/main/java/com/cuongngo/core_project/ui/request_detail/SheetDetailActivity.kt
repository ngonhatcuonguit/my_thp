package com.cuongngo.core_project.ui.request_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Body
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivitySheetDetailBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.add_request.adapter.FieldAdapter
import com.cuongngo.core_project.ui.bottom_sheet.SelectFieldValueBottomSheet
import com.cuongngo.core_project.ui.form_schema.RequestViewModel
import com.cuongngo.core_project.utils.getScreenHeight
import io.reactivex.disposables.Disposable

class SheetDetailActivity : AppBaseActivityMVVM<ActivitySheetDetailBinding, RequestViewModel>() {

    override val viewModel: RequestViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_sheet_detail

    companion object {
        val TAG = SheetDetailActivity::class.java.simpleName
        const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
        const val SHEET_DATA_KEY = "SHEET_DATA_KEY"
    }

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null
    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    fun newIntent(
        context: Context,
        requestCode: String,
        body: Body
    ): Intent {
        return Intent(context, SheetDetailActivity::class.java).apply {
            putExtra(REQUEST_CODE_KEY, requestCode)
            putExtra(SHEET_DATA_KEY, body)
        }
    }

    private lateinit var fieldAdapter: FieldAdapter

    private val body by lazy { intent.getSerializableExtra(SHEET_DATA_KEY) as Body }
    private val requestCode by lazy { intent.getStringExtra(REQUEST_CODE_KEY) ?: ""}
    private var request: RequestEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableLightStatusBar()
    }

    override fun setUp() {
        viewModel.getRequestByCode(requestCode)
        request = viewModel.requestEntity
        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
            tvFormTitle.text = body.formName.toString()
        }
        setupRecycleViewListField()
        fieldAdapter.submitListField(body.listField)
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
                        this.request = request
                        WTF(TAG, "requestForm: ${request.requestCode}")
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
            arrayListOf(),
            onItemClickListener = {
                //
            },
            onChangeValueListener = {
                if(it.type == "select"){
                    showBottomSheetOption(it)
                }else{
                    setupShowDialogChangeValue(it)
                }
            }
        )
        binding.rvListField.apply {
            layoutManager = gridLayoutManager
            isScrollContainer = false
            adapter = fieldAdapter
        }
    }

    private fun showBottomSheetOption(field: Field){
        val defaultValue = field.options?.find {
            field.value == it.value
        }
        SelectFieldValueBottomSheet(
            field = field,
            optionDefault = defaultValue,
            heightValue = (getScreenHeight() * 0.85).toInt()
        ).setOnOptionSelected {
            //handle fill & update value
        }.show(supportFragmentManager, TAG)
    }

    private fun setupShowDialogChangeValue(field: Field) {
        var fieldData = field
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
                fieldAdapter.onChangeValueField(
                    fieldData,
                    AppPreferences.getDialogData() ?: ""
                )
                handleChangeValueField(
                    fieldData,
                    AppPreferences.getDialogData() ?: ""
                )
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    private fun handleChangeValueField(field: Field, newValue: String?) {
        val sheetData = viewModel.requestEntity?.listBody?.find { it.id == body.id}
        val sheetIndex = viewModel.requestEntity?.listBody?.indexOf(sheetData) ?: return

        val fieldData = viewModel.requestEntity?.listBody?.get(sheetIndex)?.listField?.find {it.id == field.id}
        val fieldIndex = viewModel.requestEntity?.listBody?.get(sheetIndex)?.listField?.indexOf(fieldData)!!
        fieldData?.value = newValue

        fieldAdapter.notifyItemChanged(fieldIndex)
    }


}