package com.cuongngo.my_thp.base.dialog_fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.dialog.AppBaseDialog
import com.cuongngo.my_thp.base.model.DialogModel
import com.cuongngo.my_thp.databinding.DialogConfirmDefaultBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ui.form_schema.RequestViewModel
import com.cuongngo.my_thp.utils.Func
import com.cuongngo.my_thp.utils.TFunc
import com.cuongngo.my_thp.utils.convertDpToPixel
import com.cuongngo.my_thp.utils.view.setMargins

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