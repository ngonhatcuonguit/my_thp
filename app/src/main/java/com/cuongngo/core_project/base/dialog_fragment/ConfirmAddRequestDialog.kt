package com.cuongngo.core_project.base.dialog_fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog.AppBaseDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.DialogConfirmDefaultBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ui.form_schema.RequestViewModel
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.cuongngo.core_project.utils.Func
import com.cuongngo.core_project.utils.TFunc
import com.cuongngo.core_project.utils.convertDpToPixel
import com.cuongngo.core_project.utils.view.setMargins
import org.kodein.di.android.x.kodein

class ConfirmAddRequestDialog(
    private val dialogData: DialogModel,
    private val viewModel: RequestViewModel
) : AppBaseDialog<DialogConfirmDefaultBinding>() {

    private var onLeftButtonClick: Func? = null
    private var onRightButtonClick: TFunc<String>? = null

    companion object {
        val TAG = ConfirmAddRequestDialog::class.simpleName
        const val KEY_DIALOG_DATA = "KEY_DIALOG_DATA"
    }

    override fun onStart() {
        super.onStart()
        setupDialog()
    }

    private fun setupDialog() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.setMargins(
            convertDpToPixel(40f, requireContext()).toInt(),
            0,
            convertDpToPixel(40f, requireContext()).toInt(),
            0
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun setUp() {
        binding.data = dialogData
        WTF("dialogData: $dialogData")
        with(binding) {
            if (dialogData.edtTitle != null) {
                edtSheetName.root.isVisible = true
                dialogData.edtTitle?.let {
                    edtSheetName.tvTitle.text = it
                }
                dialogData.edtHint?.let {
                    edtSheetName.edtValue.hint = it
                }
                dialogData.edtValue?.let {
                    edtSheetName.edtValue.setText(it)
                }
            } else {
                edtSheetName.root.isVisible = false
            }

            btnLeft.setOnClickListener {
                onLeftButtonClick?.invoke()
            }
            if (dialogData.isSingle == false) {
                binding.btnRight.isVisible = true
                btnRight.setOnClickListener {
//                    viewModel.edtSheetName = edtSheetName.edtValue.text.toString()
//                    WTF("testSheetName ${viewModel.edtSheetName} ${edtSheetName.edtValue.text.toString()}")
                    if (edtSheetName.edtValue.text.toString().isNullOrEmpty()) {
                        edtSheetName.tvValidate.isVisible = true
                        edtSheetName.tvValidate.text = "Vui lòng nhập tên sheet hoặc tên tần suất"
                    } else {
                        onRightButtonClick?.invoke(edtSheetName.edtValue.text.toString())
                    }
                }
            } else {
                binding.btnRight.isVisible = false
            }
        }
    }

    override fun setUpObserver() {
        //
    }

    override fun inflateLayout() = R.layout.dialog_confirm_default

    fun onLeftButtonClick(func: Func?): ConfirmAddRequestDialog {
        this.onLeftButtonClick = func
        return this
    }

    fun onRightButtonClick(func: TFunc<String>?): ConfirmAddRequestDialog {
        this.onRightButtonClick = func
        return this
    }
}