package com.cuongngo.my_thp.base.adapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.my_thp.base.model.SelectableDataModel

/**
 * @param DB view data binding from layout xml
 * @param VH view holder which extend from SingleChoiceViewHolder
 * @param DATA data model
 */
abstract class SingleChoiceAdapter<DB : ViewDataBinding, VH : SingleChoiceViewHolder<DB>, DATA>(
    private var listData: List<SelectableDataModel<DATA>>,
    private val defaultItemData: DATA?
) : RecyclerView.Adapter<VH>() {
    private var filterableListData: MutableList<SelectableDataModel<DATA>> = mutableListOf()

    init {
        cloneListData()
    }

    private fun cloneListData() {
        listData.find {
            isSameItem(it.data, defaultItemData)
        }?.isSelected = true
        filterableListData.clear()
        filterableListData.addAll(listData)
    }

    open fun isSameItem(oldItem: DATA, newItem: DATA?): Boolean {
        return oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return provideViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        val itemData = filterableListData[position].data
        val isSelected = filterableListData[position].isSelected
        bindItemView(binding, itemData, isSelected, position)
        holder.itemView.setOnClickListener {
            selectItemPosition(position)
        }
    }


    open fun selectItemPosition(selectedPosition: Int) {

        if (!filterableListData[selectedPosition].isSelected) {
            filterableListData[selectedPosition].isSelected = true
            onItemSelected(filterableListData[selectedPosition].data)
            notifyItemChanged(selectedPosition)
        }

        filterableListData.onEachIndexed { index, item ->
            if (index != selectedPosition && item.isSelected) {
                item.isSelected = false
                notifyItemChanged(index)
            }
        }
    }

    private fun invalidateBeforeSearching() {
        listData.onEach { sourceItem ->
            sourceItem.isSelected = filterableListData.find {
                it.data == sourceItem.data
            }?.isSelected ?: false
        }
    }

    open fun filterData(trueWhen: (DATA) -> Boolean) {
        invalidateBeforeSearching()
        filterableListData.clear()
        filterableListData.addAll(
            listData.filter {
                trueWhen.invoke(it.data)
            }
        )
        notifyDataSetChanged()
    }

    open fun submitNewList(newData: List<DATA>) {
        listData = newData.map { SelectableDataModel(it) }
        cloneListData()
        notifyDataSetChanged()
    }

    abstract fun bindItemView(binding: DB?, itemData: DATA, isSelected: Boolean, position: Int)

    abstract fun onItemSelected(selectedItem: DATA)

    override fun getItemCount(): Int = filterableListData.size

    abstract fun provideViewHolder(parent: ViewGroup, viewType: Int): VH
}

open class SingleChoiceViewHolder<DB : ViewDataBinding>(private val view: View) : RecyclerView.ViewHolder(view) {
    val binding: DB? = DataBindingUtil.bind(view)
}