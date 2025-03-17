package com.cuongngo.my_thp.ui.bottom_sheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.adapters.SingleChoiceAdapter
import com.cuongngo.my_thp.base.adapters.SingleChoiceViewHolder
import com.cuongngo.my_thp.base.model.SelectableDataModel
import com.cuongngo.my_thp.data.database.roomdb.entity.Option
import com.cuongngo.my_thp.databinding.ItemSelectFieldValueBinding

class OptionSingleChooseAdapter(
    listOption: List<Option>,
    defaultOption: Option? = null,
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
        binding?.clSelected?.isVisible = isSelected
        binding?.ivSelected?.isVisible = isSelected
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