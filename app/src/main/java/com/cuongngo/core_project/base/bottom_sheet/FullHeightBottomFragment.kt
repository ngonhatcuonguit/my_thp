package com.cuongngo.core_project.base.bottom_sheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


/**
 * bottom sheet dialog height to wrap_content
 */
open class FullHeightBottomFragment : BottomSheetDialogFragment(), KodeinAware {

    override val kodein by kodein()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
                val view: View? = currentFocus
                val ret = super.dispatchTouchEvent(ev)
                if (view is EditText) {
                    currentFocus?.let {
                        val w: View = it
                        val scrcoords = IntArray(2)
                        w.getLocationOnScreen(scrcoords)
                        val x: Float = ev.rawX + w.left - scrcoords[0]
                        val y: Float = ev.rawY + w.top - scrcoords[1]
                        if (ev.action == MotionEvent.ACTION_UP
                            && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
                        ) {
                            view.let {
                                val inputMethodManager = requireContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                                ) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                            }
                        }
                    }
                }
                return ret
            }

            override fun onBackPressed() {
                super.onBackPressed()
                onDismiss()

            }
        }
    }

    open fun onDismiss() {}

    /**
     * override this method to set bottom sheet dialog height to wrap_content
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.peekHeight = sheet.height
                sheet.parent.parent.requestLayout()
            }
        }
    }

    protected fun setCancelable() {
        this.isCancelable = false
        dialog?.setOnKeyListener { dialog, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
            }
            true
        }

        // handle touching outside of the dialog
        val touchOutsideView =
            dialog?.window?.decorView?.findViewById<View>(R.id.touch_outside)
        touchOutsideView?.setOnClickListener { dialog?.dismiss() }
    }
}