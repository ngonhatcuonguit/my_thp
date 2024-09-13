package com.cuongngo.core_project.ui.request_detail.adapter

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.App
import com.cuongngo.core_project.R
import com.cuongngo.core_project.common.enum.FieldType
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.databinding.ItemFormHeaderBinding

class FormHeaderAdapter(
    listFormHeader: List<Field>,
    private val onItemClickListener: ((Field) -> Unit)? = null
) : RecyclerView.Adapter<FormHeaderAdapter.FormHeaderViewHolder>() {

    private val listFormHeader = listFormHeader.toMutableList()

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

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
        binding.tvContent.setOnClickListener {
            onItemClickListener?.invoke(field) ?: return@setOnClickListener
        }
        binding.ivEditValue.setOnClickListener {
            onItemClickListener?.invoke(field) ?: return@setOnClickListener
        }
        with(binding){

            if(field.type == "header" || field.type == "header2" || field.type == "header3"){
                tvTitle.isVisible = false
                tvContent.isVisible = false
                ivEditValue.isVisible = false
                tvHeader.isVisible = true
                viewSpace.isVisible = false
                when(field.type){
                    "header" -> {
                        tvHeader.setPaddingRelative(0, 16, 0, 0)
                        tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        tvHeader.setTypeface(null, Typeface.BOLD)
                    }
                    "header2" -> {
                        tvHeader.setPaddingRelative(5, 8, 0, 0)
                        tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                        tvHeader.setTypeface(null, Typeface.BOLD_ITALIC)

                    }
                    "header3" -> {
                        tvHeader.setPaddingRelative(8, 5, 0, 0)
                        tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                        tvHeader.setTypeface(null, Typeface.NORMAL)

                    }
                    else -> {
                        tvHeader.setPaddingRelative(0, 16, 0, 0)
                        tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1f)
                        tvHeader.setTypeface(null, Typeface.BOLD)
                    }
                }
            }else{
                viewSpace.isVisible = true
                tvHeader.isVisible = false
                tvTitle.isVisible = true
                if (field.value.isNullOrEmpty()) {
                    tvContent.text= field.value
                    ivEditValue.isVisible = true
                    tvContent.visibility = View.INVISIBLE
                    when (field.type) {
                        "text", "textarea" -> {
                            ivEditValue.setImageResource(R.drawable.ic_edit_value)
                        }

                        "date" -> {
                            ivEditValue.setImageResource(R.drawable.ic_date)
                        }

                        "time" -> {
                            ivEditValue.setImageResource(R.drawable.ic_clock)
                        }

                        FieldType.SELECT.fileType, FieldType.RADIO_GROUP.fileType, "checkbox-group",  "radio-group" -> {
                            ivEditValue.setImageResource(R.drawable.ic_arrow_down_gray)
                        }

                        else -> {
                            ivEditValue.setImageResource(R.drawable.ic_edit_value)
                        }
                    }

                } else {
                    tvContent.text= field.value
                    ivEditValue.isVisible = false
                    tvContent.visibility = View.VISIBLE
                    when (field.type) {
                        "text", "textarea"  -> {
                            tvContent.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                App.getDrawableResource(R.drawable.ic_edit_value), null)
                        }

                        "date" -> {
                            tvContent.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                App.getDrawableResource(R.drawable.ic_date), null)
                        }

                        "time" -> {
                            tvContent.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                App.getDrawableResource(R.drawable.ic_clock), null)
                        }

                        FieldType.SELECT.fileType, FieldType.RADIO_GROUP.fileType, "checkbox-group",  "radio-group" -> {
                            tvContent.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                App.getDrawableResource(R.drawable.ic_arrow_down_gray), null)
                        }

                        else -> {
                            tvContent.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                App.getDrawableResource(R.drawable.ic_edit_value), null)
                        }
                    }
                }
            }
        }
    }

    fun submitListFormHeader(listFormHeader: List<Field>?){
        if (!listFormHeader.isNullOrEmpty()){
            this.listFormHeader.clear()
            this.listFormHeader.addAll(listFormHeader)
            notifyDataSetChanged()
        }
    }

    fun getItemRootView(field: Field): View {
        val position = listFormHeader.indexOf(listFormHeader.find { it.id == field.id })
        return recyclerView?.findViewHolderForAdapterPosition(position)?.itemView!!
    }

    fun onChangeValueField(field: Field, index: Int) {
        field.let { listFormHeader.set(index, it) }
        notifyItemChanged(index)
    }

    fun getListHeader(): List<Field>{
        return listFormHeader
    }

}