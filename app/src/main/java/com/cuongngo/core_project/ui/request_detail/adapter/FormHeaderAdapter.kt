package com.cuongngo.core_project.ui.request_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.database.roomdb.entity.ProcessStep
import com.cuongngo.core_project.databinding.ItemFormHeaderBinding

class FormHeaderAdapter(
    listFormHeader: ArrayList<Field>,
    private val onItemClickListener: ((Field) -> Unit)? = null
) : RecyclerView.Adapter<FormHeaderAdapter.FormHeaderViewHolder>() {

    private val listFormHeader = listFormHeader

    class FormHeaderViewHolder(
        val item: ItemFormHeaderBinding
    ) : RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormHeaderViewHolder {
        return FormHeaderViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_form_header,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listFormHeader.size

    override fun onBindViewHolder(holder: FormHeaderViewHolder, position: Int) {
        val binding = holder.item
        val field = listFormHeader[position]
        binding.field = field
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(field) ?: return@setOnClickListener
        }
    }

    fun submitListFormHeader(listFormHeader: List<Field>?){
        if (!listFormHeader.isNullOrEmpty()){
            this.listFormHeader.clear()
            this.listFormHeader.addAll(listFormHeader)
            notifyDataSetChanged()
        }
    }

}