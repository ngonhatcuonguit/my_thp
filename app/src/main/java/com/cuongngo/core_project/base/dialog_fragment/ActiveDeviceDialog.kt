package com.cuongngo.core_project.base.dialog_fragment

import android.view.ViewGroup
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog.AppBaseDialog
import com.cuongngo.core_project.databinding.DialogActiveDeviceBinding
import com.cuongngo.core_project.utils.Func
import com.cuongngo.core_project.utils.convertDpToPixel
import com.cuongngo.core_project.utils.view.setMargins

class ActiveDeviceDialog() : AppBaseDialog<DialogActiveDeviceBinding>() {

    private var onLeftButtonClick: Func? = null
    private var onRightButtonClick: Func? = null

    companion object {
        val TAG = ActiveDeviceDialog::class.simpleName
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
            convertDpToPixel(50f, requireContext()).toInt(),
            0,
            convertDpToPixel(50f, requireContext()).toInt(),
            0
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun setUp() {
        with(binding) {
            btnLeft.setOnClickListener {
                onLeftButtonClick?.invoke()
            }
            btnRight.setOnClickListener {
                onRightButtonClick?.invoke()
            }
        }
    }

    override fun setUpObserver() {
        //
    }

    override fun inflateLayout() = R.layout.dialog_active_device

    fun onLeftButtonClick(func: Func?): ActiveDeviceDialog {
        this.onLeftButtonClick = func
        return this
    }

    fun onRightButtonClick(func: Func?): ActiveDeviceDialog {
        this.onRightButtonClick = func
        return this
    }


}