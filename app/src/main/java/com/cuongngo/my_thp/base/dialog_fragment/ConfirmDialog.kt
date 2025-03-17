package com.cuongngo.my_thp.base.dialog_fragment

import android.content.DialogInterface
import android.text.InputType
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.dialog.AppBaseDialog
import com.cuongngo.my_thp.base.model.DialogModel
import com.cuongngo.my_thp.databinding.DialogConfirmDefaultBinding
import com.cuongngo.my_thp.utils.Func
import com.cuongngo.my_thp.utils.TFunc
import com.cuongngo.my_thp.utils.convertDpToPixel
import com.cuongngo.my_thp.utils.view.setMargins

class ConfirmDialog(
    private val dialogData: DialogModel,
    private val margins: Float? = 40f
) : AppBaseDialog<DialogConfirmDefaultBinding>() {

    private var onLeftButtonClick: Func? = null
    private var onRightButtonClick: TFunc<String>? = null

    companion object {
        val TAG = ConfirmDialog::class.simpleName
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
            convertDpToPixel(margins ?: 50f, requireContext()).toInt(),
            0,
            convertDpToPixel(margins ?: 50f, requireContext()).toInt(),
            0
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding.root.setOnTouchListener { _, event ->
            !binding.edtSheetName.edtValue.isFocused
            hideKeyboard()
            false
        }
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun setUp() {
        binding.data = dialogData
        with(binding) {
            if (dialogData.edtTitle != null) {
                edtSheetName.root.isVisible = true
                dialogData.edtValue?.let {
                    if (dialogData.typeInput == "number"){
                        edtSheetName.edtValue.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    }else{
                        edtSheetName.edtValue.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                    }

                }
                dialogData.edtTitle?.let {
                    edtSheetName.tvTitle.text = it
                }
                dialogData.edtHint?.let {
                    edtSheetName.edtValue.hint = it
                }
                dialogData.edtValue?.let {
                    edtSheetName.edtValue.setText(it)
                }
                edtSheetName.edtValue.requestFocus()
                if (!edtSheetName.edtValue.isFocused){
                    hideKeyboard()
                }else{
                    showKeyBoard()
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
                    onRightButtonClick?.invoke(binding.edtSheetName.edtValue.text.toString() ?:"")
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

    override fun dismiss() {
        binding.edtSheetName.edtValue.clearFocus()
        hideKeyboard()
        super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        binding.edtSheetName.edtValue.clearFocus()
        hideKeyboard()
        super.onDismiss(dialog)
    }


    fun onLeftButtonClick(func: Func?): ConfirmDialog {
        this.onLeftButtonClick = func
        return this
    }

    fun onRightButtonClick(func: TFunc<String>?): ConfirmDialog {
        this.onRightButtonClick = func
        return this
    }


}