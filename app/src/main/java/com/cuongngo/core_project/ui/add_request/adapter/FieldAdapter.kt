package com.cuongngo.core_project.ui.add_request.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.databinding.ItemFieldBinding
import com.cuongngo.core_project.ext.WTF

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
            if (field.value.isNullOrEmpty()) {
                tvValue.text= field.value
                ivEditValue.isVisible = true
                tvValue.isVisible = false
                when (field.type) {
                    "text" -> {
                        ivEditValue.setImageResource(R.drawable.ic_edit_value)
                    }

                    "date" -> {
                        ivEditValue.setImageResource(R.drawable.ic_date)
                    }

                    "time" -> {
                        ivEditValue.setImageResource(R.drawable.ic_clock)
                    }

                    "select" -> {
                        ivEditValue.setImageResource(R.drawable.ic_arrow_down_gray)
                    }

                    "checkbox" -> {
                        ivEditValue.setImageResource(R.drawable.ic_arrow_down_gray)
                    }

                    else -> {
                        ivEditValue.setImageResource(R.drawable.ic_edit_value)
                    }
                }

            } else {
                ivEditValue.isVisible = false
                tvValue.isVisible = true
                tvValue.text= field.value
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

    fun onChangeValueField(field: Field, newValue: String?) {
        val data = listField.find { it.id == field.id }
        val index = listField.indexOf(data)
        data?.value = newValue
        data?.let { listField.set(index, it) }
        notifyItemChanged(index)
        WTF("fieldDataChange ${data?.value.toString()}")
    }
    fun onChangeValueFieldV2(field: Field, index: Int) {
        field.let { listField.set(index, it) }
        notifyItemChanged(index)
    }

}