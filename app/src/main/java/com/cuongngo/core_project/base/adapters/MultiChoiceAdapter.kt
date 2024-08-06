package com.cuongngo.core_project.base.adapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.base.model.SelectableDataModel

abstract class MultiChoiceAdapter <DB: ViewDataBinding, VH: MultiChoiceViewHolder<DB>, DATA>(
    private val listData: List<SelectableDataModel<DATA>>,
    private val listDefaultData: List<DATA> = emptyList()
): RecyclerView.Adapter<VH>() {
    init {
        listDefaultData.onEach { defaultSelectedItem ->
            listData.find {
                it.data == defaultSelectedItem
            }?.isSelected = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return provideViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        val itemData = listData[position].data
        val isSelected = listData[position].isSelected
        bindItemView(binding, itemData, isSelected, position)
        holder.itemView.setOnClickListener {
            selectItemPosition(position)
        }
    }


    open fun selectItemPosition(selectedPosition: Int){
        listData[selectedPosition].isSelected = !listData[selectedPosition].isSelected
        val listSelected = listData.filter {
            it.isSelected
        }.map {
            it.data
        }
        onItemSelected(listSelected)
        notifyItemChanged(selectedPosition)
    }

    abstract fun bindItemView(binding: DB?, itemData: DATA, isSelected: Boolean, position: Int)

    abstract fun onItemSelected(selectedItems: List<DATA>)

    override fun getItemCount(): Int = listData.size

    abstract fun provideViewHolder(parent: ViewGroup, viewType: Int): VH
}

open class MultiChoiceViewHolder<DB: ViewDataBinding>(val view: View): RecyclerView.ViewHolder(view){
    val binding: DB?  = DataBindingUtil.bind(view)
}