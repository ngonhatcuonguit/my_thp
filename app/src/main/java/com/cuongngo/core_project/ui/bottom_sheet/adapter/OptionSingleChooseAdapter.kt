package com.cuongngo.core_project.ui.bottom_sheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.adapters.SingleChoiceAdapter
import com.cuongngo.core_project.base.adapters.SingleChoiceViewHolder
import com.cuongngo.core_project.base.model.SelectableDataModel
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.databinding.ItemSelectFieldValueBinding

class OptionSingleChooseAdapter(
    private val listOption: List<Option>,
    private val defaultOption: Option? = null,
    private val onOptionSelected: (Option) -> Unit
) : SingleChoiceAdapter<ItemSelectFieldValueBinding, SingleChoiceViewHolder<ItemSelectFieldValueBinding>, Option>(
    listData = listOption.map {
        SelectableDataModel(it)
    },
    defaultOption
) {
    override fun bindItemView(
        binding: ItemSelectFieldValueBinding?,
        itemData: Option,
        isSelected: Boolean,
        position: Int
    ) {
        binding?.value = itemData.value
        if (isSelected) {
            binding?.clSelected?.isVisible = true
            binding?.ivSelected?.isVisible = true
        } else {
            binding?.clSelected?.isVisible = false
            binding?.ivSelected?.isVisible = false
        }
    }

    override fun onItemSelected(selectedItem: Option) {
        onOptionSelected.invoke(selectedItem)
    }

    override fun provideViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SingleChoiceViewHolder<ItemSelectFieldValueBinding> {
        return SingleChoiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_field_value, parent, false)
        )
    }

}