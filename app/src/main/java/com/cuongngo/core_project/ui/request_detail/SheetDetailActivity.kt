package com.cuongngo.core_project.ui.request_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Sheet
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivitySheetDetailBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.add_request.adapter.FieldAdapter
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.cuongngo.core_project.utils.Constants
import io.reactivex.disposables.Disposable

class SheetDetailActivity : AppBaseActivityMVVM<ActivitySheetDetailBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_sheet_detail

    companion object {
        val TAG = SheetDetailActivity::class.java.simpleName
        const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
        const val SHEET_DATA_KEY = "SHEET_DATA_KEY"
    }

    fun newIntent(
        context: Context,
        requestCode: String,
        sheet: Sheet
    ): Intent {
        return Intent(context, SheetDetailActivity::class.java).apply {
            putExtra(REQUEST_CODE_KEY, requestCode)
            putExtra(SHEET_DATA_KEY, sheet)
        }
    }

    private lateinit var fieldAdapter: FieldAdapter

    private val requestCode by lazy { intent.getStringExtra(REQUEST_CODE_KEY) ?: "" }
    private val sheet by lazy { intent.getSerializableExtra(SHEET_DATA_KEY) as Sheet}
    private var request: RequestEntity? = null

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null
    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableLightStatusBar()
    }

    override fun setUp() {
        viewModel.getRequestByCode(requestCode)
        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
            tvFormTitle.text = sheet.formName.toString()
        }
        setupRecycleViewListField()
        fieldAdapter.submitListField(sheet.listField)
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
                setupShowDialogChangeValue(it)
            }
        )
        binding.rvListField.apply {
            layoutManager = gridLayoutManager
            isScrollContainer = false
            adapter = fieldAdapter
        }
    }

    private fun setupShowDialogChangeValue(field: Field) {
        var fieldData = field
        var edtText = ""
        if(!field.value.isNullOrEmpty()){
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
                fieldAdapter.onChangeValueFile(
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


}