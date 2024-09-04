package com.cuongngo.core_project.ui.bottom_sheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.bottom_sheet.FullHeightBottomSheet
import com.cuongngo.core_project.base.model.WrapperModel
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.databinding.FragmentSelectFieldValueBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.nullableCast
import com.cuongngo.core_project.ui.bottom_sheet.SingleChoiceOptionBottomSheet.Companion.FIELD_DATA
import com.cuongngo.core_project.ui.bottom_sheet.adapter.OptionMultiChoiceAdapter
import com.cuongngo.core_project.utils.TFunc
import com.cuongngo.core_project.utils.getScreenHeight
import com.cuongngo.core_project.utils.getScreenWidth

class MultiChoiceOptionBottomSheet : FullHeightBottomSheet<FragmentSelectFieldValueBinding>() {

    companion object {
        const val DEFAULT_LIST_OPTION = "DEFAULT_LIST_USER"
        const val LIST_OPTION_DATA = "LIST_OPTION_DATA"
        const val BOTTOM_SHEET_HEIGHT_VALUE = "BOTTOM_SHEET_HEIGHT_VALUE"
        var DEFAULT_HEIGHT = (getScreenHeight() * 0.95).toInt()
        operator fun invoke(
            listOption: List<Option>?,
            listSelectedDefault: List<Option>?,
            field: Field,
            heightValue: Int? = DEFAULT_HEIGHT
        ): MultiChoiceOptionBottomSheet {
            return MultiChoiceOptionBottomSheet().apply {
                arguments = bundleOf(
                    LIST_OPTION_DATA to WrapperModel(listOption),
                    DEFAULT_LIST_OPTION to WrapperModel(listSelectedDefault),
                    BOTTOM_SHEET_HEIGHT_VALUE to heightValue,
                    FIELD_DATA to field,
                )
            }
        }
    }

    private var onConfirmSelected: TFunc<List<Option>?>? = null
    private var heightValue: Int = DEFAULT_HEIGHT
    private var requestEntity: RequestEntity? = null
    private var listOptionSelected: List<Option>? = null
    private var fieldData : Field? = null

    private val listOptionSelectedDefault by lazy {
        (arguments?.getSerializable(DEFAULT_LIST_OPTION)
            .nullableCast<WrapperModel<List<Option>>>())?.data ?: emptyList()
    }
    private val listOption by lazy {
        (arguments?.getSerializable(LIST_OPTION_DATA)
            .nullableCast<WrapperModel<List<Option>>>())?.data ?: emptyList()
    }

    override fun inflateLayout(): Int = R.layout.fragment_select_field_value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            heightValue = it.getInt(BOTTOM_SHEET_HEIGHT_VALUE) ?: DEFAULT_HEIGHT
            fieldData = it.getSerializable(FIELD_DATA) as Field?
        }
        this.listOptionSelected = listOptionSelectedDefault
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupHeightRecycleView()
        with(binding){
            tvTitle.text = fieldData?.label ?: "Chọn thông tin"
            containerBottom.visibility = View.VISIBLE
            tvPlaceholder.text = fieldData?.placeholder
            WTF("testPlaceHolder ${fieldData?.placeholder}")
            btnClose.setOnClickListener {
                onConfirmSelected?.invoke(listOptionSelected)
                dismiss()
            }
            btnConfirm.setOnClickListener {
                onConfirmSelected?.invoke(listOptionSelected)
                dismiss()
            }
        }


        //rcv
        binding.rcvOption.run {
            layoutManager = LinearLayoutManager(activity ?: return)
            adapter =
                OptionMultiChoiceAdapter(
                    listOption,
                    listOptionSelectedDefault
                ) { listSelected ->
                    listOptionSelected = listSelected
                }
        }
    }

    private fun setupHeightRecycleView() {
        binding.root.layoutParams.run {
            width = getScreenWidth()
            height = heightValue
        }
    }

    fun onOptionSelected(onOptionSelected: TFunc<List<Option>?>): MultiChoiceOptionBottomSheet {
        this.onConfirmSelected = onOptionSelected
        return this
    }

}