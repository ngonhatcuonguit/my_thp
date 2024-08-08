package com.cuongngo.core_project.ui.bottom_sheet.adapter

import android.view.ViewGroup
import com.cuongngo.core_project.base.adapters.MultiChoiceAdapter
import com.cuongngo.core_project.base.adapters.MultiChoiceViewHolder
import com.cuongngo.core_project.base.model.SelectableDataModel
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.databinding.ItemSelectFieldValueBinding
import com.cuongngo.core_project.utils.TFunc

class UserMultiChoiceAdapter(
    private val listSelectedDefault: List<UserTHPEntity> = emptyList(),
    private val listUserResult: List<UserTHPEntity> = emptyList(),
    private val onChangeSelectItem: TFunc<List<UserTHPEntity>>
) : MultiChoiceAdapter<ItemSelectFieldValueBinding, MultiChoiceViewHolder<ItemSelectFieldValueBinding>, UserTHPEntity>(
    listData = listUserResult.map {
        SelectableDataModel(it)
    },
    listDefaultData = listSelectedDefault
) {
    override fun bindItemView(
        binding: ItemSelectFieldValueBinding?,
        itemData: UserTHPEntity,
        isSelected: Boolean,
        position: Int
    ) {
        //
    }

    override fun onItemSelected(selectedItems: List<UserTHPEntity>) {
        //
    }

    override fun provideViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultiChoiceViewHolder<ItemSelectFieldValueBinding> {
        //
    }

}