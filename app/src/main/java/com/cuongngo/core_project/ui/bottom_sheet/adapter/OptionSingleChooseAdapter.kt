package com.cuongngo.core_project.ui.bottom_sheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
): SingleChoiceAdapter<ItemSelectFieldValueBinding, SingleChoiceViewHolder<ItemSelectFieldValueBinding>, Option>(
    listData = listOption.map {
        SelectableDataModel(it)
    },
    defaultOption
) {
    override fun bindItemView(binding: ItemSelectFieldValueBinding?, itemData: Option, isSelected: Boolean, position: Int) {
        //
    }

    override fun onItemSelected(selectedItem: Option) {
        //
    }

    override fun provideViewHolder(parent: ViewGroup, viewType: Int): SingleChoiceViewHolder<ItemSelectFieldValueBinding> {
        return SingleChoiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_field_value, parent, false)        )
    }

}