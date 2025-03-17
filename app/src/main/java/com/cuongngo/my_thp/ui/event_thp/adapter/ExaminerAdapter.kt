package com.cuongngo.my_thp.ui.event_thp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.adapters.SingleChoiceAdapter
import com.cuongngo.my_thp.base.adapters.SingleChoiceViewHolder
import com.cuongngo.my_thp.base.model.SelectableDataModel
import com.cuongngo.my_thp.databinding.ItemGkBinding
import com.cuongngo.my_thp.ui.event_thp.model.Examiner
import com.cuongngo.my_thp.utils.TFunc

class ExaminerAdapter(
    listGK: List<Examiner>,
    defaultGK: Examiner? = null,
    private val onGKSelected: TFunc<Examiner>
) : SingleChoiceAdapter<ItemGkBinding, SingleChoiceViewHolder<ItemGkBinding>, Examiner>(
    listData = listGK.map {
        SelectableDataModel(it)
    },
    defaultGK
) {
    override fun bindItemView(
        binding: ItemGkBinding?,
        itemData: Examiner,
        isSelected: Boolean,
        position: Int
    ) {
        binding?.value = itemData
    }

    override fun onItemSelected(selectedItem: Examiner) {
        onGKSelected.invoke(selectedItem)
    }

    override fun provideViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SingleChoiceViewHolder<ItemGkBinding> {
        return SingleChoiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_gk, parent, false)
        )
    }

}