package com.cuongngo.core_project.ui.bottom_sheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.adapters.SingleChoiceAdapter
import com.cuongngo.core_project.base.adapters.SingleChoiceViewHolder
import com.cuongngo.core_project.base.model.SelectableDataModel
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.databinding.ItemSelectFieldValueBinding
import com.cuongngo.core_project.utils.TFunc

class UserSingleChoiceAdapter(
    listUser: List<UserTHPEntity>,
    defaultUser: UserTHPEntity? = null,
    private val onUserSelected: TFunc<UserTHPEntity>
) : SingleChoiceAdapter<ItemSelectFieldValueBinding, SingleChoiceViewHolder<ItemSelectFieldValueBinding>, UserTHPEntity>(
    listData = listUser.map {
        SelectableDataModel(it)
    },
    defaultUser
) {
    override fun bindItemView(
        binding: ItemSelectFieldValueBinding?,
        itemData: UserTHPEntity,
        isSelected: Boolean,
        position: Int
    ) {
        binding?.value = itemData.name
        binding?.clSelected?.isVisible = isSelected
        binding?.ivSelected?.isVisible = isSelected
    }

    override fun onItemSelected(selectedItem: UserTHPEntity) {
        onUserSelected.invoke(selectedItem)
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