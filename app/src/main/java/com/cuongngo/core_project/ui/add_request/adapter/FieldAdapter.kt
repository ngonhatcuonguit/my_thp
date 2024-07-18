package com.cuongngo.core_project.ui.add_request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.databinding.ItemFieldBinding

class FieldAdapter(
    listField: ArrayList<Field>,
    private val onItemClickListener: ((Field) -> Unit)? = null,
    private val onChangeValueListener: ((Field) -> Unit)? = null
): RecyclerView.Adapter<FieldAdapter.FieldViewHolder>() {

    private val listField = listField

    override fun getItemCount(): Int {
        return listField.size
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        val binding = holder.itemField
        var field = listField[position]
        binding.field = field
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(field) ?: return@setOnClickListener
        }
        binding.tvValue.setOnClickListener{
            onChangeValueListener?.invoke(field) ?: return@setOnClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        return FieldViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_field,
                parent,
                false
            )
        )
    }

    fun submitListField(fields: List<Field>?) {
        if (fields != null) {
            this.listField.clear()
            this.listField.addAll(fields)
            notifyDataSetChanged()
        }
    }

    class FieldViewHolder(
        val itemField: ItemFieldBinding
    ) : RecyclerView.ViewHolder(itemField.root)

}