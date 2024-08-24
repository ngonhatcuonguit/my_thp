package com.cuongngo.core_project.ui.add_request.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.App.Companion.getDrawableResource
import com.cuongngo.core_project.R
import com.cuongngo.core_project.common.enum.FieldType
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.databinding.ItemFieldBinding

class FieldAdapter(
    listField: ArrayList<Field>,
    private val onItemClickListener: ((Field) -> Unit)? = null,
    private val onChangeValueListener: ((Field) -> Unit)? = null
) : RecyclerView.Adapter<FieldAdapter.FieldViewHolder>() {

    private var listField = listField

    override fun getItemCount(): Int {
        return listField.size
    }

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        val binding = holder.itemField
        var field = listField[position]
        binding.field = field
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(field) ?: return@setOnClickListener
        }
        binding.ivEditValue.setOnClickListener {
            onChangeValueListener?.invoke(field) ?: return@setOnClickListener
        }
        binding.tvValue.setOnClickListener {
            onChangeValueListener?.invoke(field) ?: return@setOnClickListener
        }
        with(binding) {

            if (field.type == "header"){
                clContainerField.isVisible = false
                tvHeaderGroup.isVisible = true
                root.setBackgroundResource(R.drawable.shape_primary_stroke_black)
            }else{
                if (field.value.isNullOrEmpty()) {
                    tvValue.text= field.value
                    ivEditValue.isVisible = true
                    tvValue.isVisible = false
                    root.setBackgroundResource(R.drawable.shape_item_field)
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
                    tvValue.text= field.value
                    ivEditValue.isVisible = false
                    tvValue.isVisible = true
                    root.setBackgroundResource(R.drawable.shape_item_field)
                    when (field.type) {
                        "text", "textarea"  -> {
                            tvValue.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawableResource(R.drawable.ic_edit_value), null)
                        }

                        "date" -> {
                            tvValue.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawableResource(R.drawable.ic_date), null)
                        }

                        "time" -> {
                            tvValue.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawableResource(R.drawable.ic_clock), null)
                        }

                        FieldType.SELECT.fileType, FieldType.RADIO_GROUP.fileType, "checkbox-group",  "radio-group" -> {
                            tvValue.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawableResource(R.drawable.ic_arrow_down_gray), null)
                        }

                        else -> {
                            tvValue.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawableResource(R.drawable.ic_edit_value), null)
                        }
                    }
                }
            }
        }

    }

    fun getItemRootView(field: Field): View {
        val position = listField.indexOf(listField.find { it.id == field.id })
        return recyclerView?.findViewHolderForAdapterPosition(position)?.itemView!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        return FieldViewHolder(
            ItemFieldBinding.inflate(
                LayoutInflater.from(parent.context),
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

//    fun onChangeValueField(field: Field, newValue: String?) {
//        val data = listField.find { it.id == field.id }
//        val index = listField.indexOf(data)
//        data?.value = newValue
//        data?.let { listField.set(index, it) }
//        notifyItemChanged(index)
//        WTF("fieldDataChange ${data?.value.toString()}")
//    }
    fun onChangeValueField(field: Field, index: Int) {
        field.let { listField.set(index, it) }
        notifyItemChanged(index)
    }

    fun getListField(): List<Field>{
        return listField
    }

}