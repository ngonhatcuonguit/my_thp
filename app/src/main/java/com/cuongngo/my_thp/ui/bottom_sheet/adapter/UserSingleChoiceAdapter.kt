package com.cuongngo.my_thp.ui.bottom_sheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.adapters.SingleChoiceAdapter
import com.cuongngo.my_thp.base.adapters.SingleChoiceViewHolder
import com.cuongngo.my_thp.base.model.SelectableDataModel
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.my_thp.databinding.ItemSelectFieldValueBinding
import com.cuongngo.my_thp.utils.TFunc

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
        var positionName = if (!itemData.position_name.isNullOrEmpty()){
            " - " + itemData.position_name
        }else{
            ""
        }
        binding?.value = itemData.first_name + " " + itemData.last_name + " - " + itemData.initial + positionName
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