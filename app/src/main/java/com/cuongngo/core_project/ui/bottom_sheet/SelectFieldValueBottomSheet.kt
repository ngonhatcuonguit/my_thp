package com.cuongngo.core_project.ui.bottom_sheet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.bottom_sheet.FullHeightBottomSheet
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.databinding.FragmentSelectFieldValueBinding
import com.cuongngo.core_project.ui.bottom_sheet.adapter.OptionSingleChooseAdapter
import com.cuongngo.core_project.utils.TFunc

class SelectFieldValueBottomSheet(
    private val listOption: ArrayList<Option>
) : FullHeightBottomSheet<FragmentSelectFieldValueBinding>() {
    companion object {

        const val DEFAULT_OPTION = "DEFAULT_OPTION"
        const val LIST_OPTION = "LIST_OPTION"
        operator fun invoke(listOption: ArrayList<Option>, optionDefault: Option?): SelectFieldValueBottomSheet {
            return SelectFieldValueBottomSheet(listOption).apply {
                arguments = bundleOf(
                    DEFAULT_OPTION to optionDefault
                )
            }
        }
    }

    private var onOptionSelected: TFunc<Option?>? = null
    private val optionDefault by lazy {
        arguments?.getSerializable(DEFAULT_OPTION) as Option?
    }

    override fun inflateLayout(): Int = R.layout.fragment_select_field_value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.rcvOption.run {
            layoutManager = LinearLayoutManager(activity ?: return)
            adapter = OptionSingleChooseAdapter(
                listOption.map {
                    it
                },
                optionDefault
            ) {
                onOptionSelected?.invoke(it)
                dismiss()
            }
        }
    }

    fun setOnOptionSelected(onOptionSelected: TFunc<Option?>): SelectFieldValueBottomSheet {
        this.onOptionSelected = onOptionSelected
        return this
    }
}