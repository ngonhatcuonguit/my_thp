package com.cuongngo.core_project.ui.search_form.form_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.databinding.ItemFormHorizontalBinding
import com.cuongngo.core_project.utils.convertDpToPixel

class FormHorizontalAdapter(
    context: Context,
    listForm: ArrayList<FormEntity>,
    private val onItemClickListener: ((FormEntity) -> Unit)? = null
) : RecyclerView.Adapter<FormHorizontalAdapter.FormHorizontalViewHolder>() {

    private val listForm = listForm
    private val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormHorizontalViewHolder {
        return FormHorizontalViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_form_horizontal, parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: FormHorizontalViewHolder, position: Int) {
        val binding = holder.itemHorizontalForm
        var form = listForm[position]
        binding.form = form
        binding.root.elevation = convertDpToPixel(8F, context)
        binding.tvFormCode.text = "Mã form: ${form.form_code}"
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(form) ?: return@setOnClickListener
        }
        if (form.category.isNullOrEmpty()){
            binding.tvStatus.visibility = View.INVISIBLE
        }else{
            binding.tvStatus.visibility = View.VISIBLE
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

    class FormHorizontalViewHolder(
        val itemHorizontalForm: ItemFormHorizontalBinding
    ) : RecyclerView.ViewHolder(itemHorizontalForm.root)

}