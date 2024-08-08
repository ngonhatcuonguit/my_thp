package com.cuongngo.core_project.ui.dropdown

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.App
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.model.SelectableDataModel
import com.cuongngo.core_project.data.database.roomdb.entity.Option

/**
* @param DATA data model
*/
open class DropdownAdapter(
    private var listData: List<SelectableDataModel<Option>>,
    private var optionDefault: Option? = null,
    private val onOptionSelected: (Option) -> Unit
) : RecyclerView.Adapter<DropdownAdapter.OptionViewHolder>() {

    private var filterableListData: MutableList<SelectableDataModel<Option>> = mutableListOf()

    init {
        cloneListData()
    }
    private fun cloneListData() {
        listData.find { it.isSelected }?.isSelected = false
        listData.find {
            isSameItem(it.data, optionDefault)
        }?.isSelected = true
        filterableListData.clear()
        filterableListData.addAll(listData)
    }

    open fun isSameItem(oldItem: Option, newItem: Option?): Boolean {
        return oldItem == newItem
    }

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOptionTitle: TextView = itemView.findViewById(R.id.tv_dropdown_name)
        val ivOptionChecked: ImageView = itemView.findViewById(R.id.iv_dropdown_selected)
        fun bind(data: SelectableDataModel<Option>) {
            tvOptionTitle.text = data.data.value
            if (data.isSelected){
                tvOptionTitle.setTextColor(App.getResources().getColor(R.color.blue_accent))
                tvOptionTitle.setTypeface(tvOptionTitle.typeface, Typeface.BOLD)
            }else{
                tvOptionTitle.setTypeface(tvOptionTitle.typeface, Typeface.NORMAL)

            }
            ivOptionChecked.visibility =
                if (data.isSelected) View.VISIBLE else View.INVISIBLE
            itemView.setOnClickListener {
                onOptionSelected.invoke(data.data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dropdown, parent, false)
        )
    }

    override fun getItemCount(): Int = filterableListData.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(filterableListData[position])
    }

}