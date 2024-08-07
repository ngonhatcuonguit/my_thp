package com.cuongngo.core_project.ui.dropdown

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.Option

class DropdownAdapter(
    private val listOption: ArrayList<Option>,
    private val onOptionSelected: (Option) -> Unit
) : RecyclerView.Adapter<DropdownAdapter.OptionViewHolder>() {


    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOptionTitle: TextView = itemView.findViewById(R.id.tv_dropdown_name)
        private val ivOptionChecked: ImageView = itemView.findViewById(R.id.iv_dropdown_selected)
        fun bind(option: Option) {
            tvOptionTitle.text = option.value
            ivOptionChecked.visibility =
                if (option.isSelect == true) View.VISIBLE else View.INVISIBLE
            itemView.setOnClickListener {
                onOptionSelected.invoke(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dropdown, parent, false)
        )
    }

    override fun getItemCount(): Int = listOption.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(listOption[position])
    }

}