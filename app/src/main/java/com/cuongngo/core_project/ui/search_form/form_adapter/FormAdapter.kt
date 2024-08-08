package com.cuongngo.core_project.ui.search_form.form_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.databinding.ItemFormBinding
import com.cuongngo.core_project.utils.convertDpToPixel

class FormAdapter(
    context: Context,
    listForm: ArrayList<FormEntity>,
    private val onItemClickListener: ((FormEntity) -> Unit)? = null
) : RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    private val listForm = listForm
    private val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        return FormViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_form,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val binding = holder.itemForm
        var form = listForm[position]
        binding.form = form
        binding.root.elevation = convertDpToPixel(8F, context)
        binding.tvFormCode.text = "Mã form: ${form.formCode}"
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(form) ?: return@setOnClickListener
        }
    }

    override fun getItemCount(): Int {
        return listForm.size
    }


    fun submitListForm(forms: List<FormEntity>?) {
        if (forms != null) {
            this.listForm.clear()
            this.listForm.addAll(forms)
            notifyDataSetChanged()
        }
    }

    class FormViewHolder(
        val itemForm: ItemFormBinding
    ) : RecyclerView.ViewHolder(itemForm.root)

}