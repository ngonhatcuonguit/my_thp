package com.cuongngo.core_project.base.bottom_sheet

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.cuongngo.core_project.R
import com.cuongngo.core_project.utils.getScreenHeight
import com.cuongngo.core_project.utils.getScreenWidth
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class FullHeightBottomSheet<DB: ViewDataBinding>: BottomSheetDialogFragment(), KodeinAware {
    override val kodein by kodein()

    lateinit var binding: DB

    private val progressDialog by lazy { createProgressDialog() }

    @LayoutRes
    abstract fun inflateLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme){
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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            inflateLayout(),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayoutParams()
    }


    private fun setupLayoutParams() {
        binding.root.layoutParams.run {
            width = getScreenWidth()
            height = (getScreenHeight().toFloat() * 0.95).toInt()
        }

        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.peekHeight = sheet.height
                sheet.parent.parent.requestLayout()
            }
        }

    }

    protected fun setCancelable(){
        this.isCancelable = false
        dialog?.setOnKeyListener { dialog, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
            }
            true
        }

        // handle touching outside of the dialog
        val touchOutsideView =
            dialog?.window?.decorView?.findViewById<View>(com.google.android.material.R.id.touch_outside)
        touchOutsideView?.setOnClickListener { dialog?.dismiss() }
    }

    private fun createProgressDialog(): AlertDialog? {
        if (activity == null)
            return null

        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_progress, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        val progressDialog = mBuilder.show()
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return progressDialog
    }

    open fun showProgressLoading(){
        progressDialog?.show()
    }

    open fun hideProgressLoading(){
        progressDialog?.dismiss()
    }
}