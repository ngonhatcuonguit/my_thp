package com.cuongngo.core_project.base.dialog_fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog.AppBaseDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.DialogConfirmDefaultBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.utils.Func
import com.cuongngo.core_project.utils.convertDpToPixel
import com.cuongngo.core_project.utils.view.setMargins

class ConfirmDialog(
    private val dialogData: DialogModel
) : AppBaseDialog<DialogConfirmDefaultBinding>() {

    private var onLeftButtonClick: Func? = null
    private var onRightButtonClick: Func? = null

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
                    onRightButtonClick?.invoke()
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

    fun onLeftButtonClick(func: Func?): ConfirmDialog {
        this.onLeftButtonClick = func
        return this
    }

    fun onRightButtonClick(func: Func?): ConfirmDialog {
        this.onRightButtonClick = func
        return this
    }


//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return object : BottomSheetDialog(requireContext(), theme){
//            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//                val view: View? = currentFocus
//                val ret = super.dispatchTouchEvent(ev)
//                if (view is EditText) {
//                    currentFocus?.let {
//                        val w: View = it
//                        val scrcoords = IntArray(2)
//                        w.getLocationOnScreen(scrcoords)
//                        val x: Float = ev.rawX + w.left - scrcoords[0]
//                        val y: Float = ev.rawY + w.top - scrcoords[1]
//                        if (ev.action == MotionEvent.ACTION_UP
//                            && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
//                        ) {
//                            view.let {
//                                val inputMethodManager = requireContext().getSystemService(
//                                    Context.INPUT_METHOD_SERVICE
//                                ) as InputMethodManager
//                                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
//                            }
//                            when(currentFocus?.id){
//                                R.id.edt_content_feedback -> {
//                                    validateContent()
//                                }
//                            }
//                        }
//                    }
//                }
//                return ret
//            }
//        }
//    }


}