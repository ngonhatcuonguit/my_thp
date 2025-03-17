package com.cuongngo.my_thp.ui.bottom_sheet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.adapters.MultiChoiceAdapter
import com.cuongngo.my_thp.base.adapters.MultiChoiceViewHolder
import com.cuongngo.my_thp.base.model.SelectableDataModel
import com.cuongngo.my_thp.data.database.roomdb.entity.Option
import com.cuongngo.my_thp.databinding.ItemSelectFieldValueBinding
import com.cuongngo.my_thp.utils.TFunc

class OptionMultiChoiceAdapter(
    private val listOption: List<Option>,
    private val listSelectedDefault: List<Option>,
    private val onChangeSelectItem: TFunc<List<Option>>
) : MultiChoiceAdapter<ItemSelectFieldValueBinding, MultiChoiceViewHolder<ItemSelectFieldValueBinding>, Option>(
    listData = listOption.map {
        SelectableDataModel(it)
    },
    listDefaultData = listSelectedDefault
) {
    override fun bindItemView(
        binding: ItemSelectFieldValueBinding?,
        itemData: Option,
        isSelected: Boolean,
        position: Int
    ) {
        binding?.value = itemData.value
        binding?.ivSelected?.isVisible = isSelected
        binding?.clSelected?.isVisible = isSelected
    }

    override fun onItemSelected(selectedItems: List<Option>) {
        onChangeSelectItem.invoke(selectedItems)
    }

    override fun provideViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultiChoiceViewHolder<ItemSelectFieldValueBinding> {
        return MultiChoiceOptionVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_field_value, parent, false)
        )
    }

}

class MultiChoiceOptionVH(view: View) : MultiChoiceViewHolder<ItemSelectFieldValueBinding>(view)