package com.cuongngo.core_project.ui.bottom_sheet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.bottom_sheet.FullHeightBottomSheet
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.databinding.FragmentSelectFieldValueBinding
import com.cuongngo.core_project.ui.bottom_sheet.adapter.OptionSingleChooseAdapter
import com.cuongngo.core_project.utils.TFunc
import com.cuongngo.core_project.utils.getScreenHeight
import com.cuongngo.core_project.utils.getScreenWidth

class SelectFieldValueBottomSheet : FullHeightBottomSheet<FragmentSelectFieldValueBinding>() {
    companion object {

        const val DEFAULT_OPTION = "DEFAULT_OPTION"
        const val FIELD_DATA = "FIELD_DATA"
        const val BOTTOM_SHEET_HEIGHT_VALUE = "BOTTOM_SHEET_HEIGHT_VALUE"
        var DEFAULT_HEIGHT = (getScreenHeight() * 0.8).toInt()
        operator fun invoke(field: Field, optionDefault: Option?, heightValue: Int? = DEFAULT_HEIGHT): SelectFieldValueBottomSheet {
            return SelectFieldValueBottomSheet().apply {
                arguments = bundleOf(
                    DEFAULT_OPTION to optionDefault,
                    FIELD_DATA to field,
                    BOTTOM_SHEET_HEIGHT_VALUE to heightValue
                )
            }
        }
    }

    private var onOptionSelected: TFunc<Option?>? = null
    private var heightValue: Int = DEFAULT_HEIGHT

    private var optionDefault: Option? = null
    private var fieldData : Field? = null
    override fun inflateLayout(): Int = R.layout.fragment_select_field_value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            heightValue = it.getInt(BOTTOM_SHEET_HEIGHT_VALUE) ?: DEFAULT_HEIGHT
            optionDefault = it.getSerializable(DEFAULT_OPTION) as Option?
            fieldData = it.getSerializable(FIELD_DATA) as Field?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupHeightRecycleView()
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.tvTitle.text = optionDefault?.label ?: "Chọn thông tin"
        binding.rcvOption.run {
            layoutManager = LinearLayoutManager(activity ?: return)
            adapter = fieldData?.options?.map {
                it
            }?.let {
                OptionSingleChooseAdapter(
                    it,
                    optionDefault
                ) {
                    onOptionSelected?.invoke(it)
                    dismiss()
                }
            }
        }
    }

    private fun setupHeightRecycleView() {
        binding.root.layoutParams.run {
            width = getScreenWidth()
            height = heightValue
        }
    }

    fun setOnOptionSelected(onOptionSelected: TFunc<Option?>): SelectFieldValueBottomSheet {
        this.onOptionSelected = onOptionSelected
        return this
    }
}