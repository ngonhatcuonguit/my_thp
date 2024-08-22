package com.cuongngo.core_project.ui.list_request.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.databinding.ItemRequestHorizontalBinding
import com.cuongngo.core_project.utils.convertDpToPixel

class RequestHorizontalAdapter(
    context: Context,
    listRequest: ArrayList<RequestEntity>,
    private val onItemClickListener: ((RequestEntity) -> Unit)? = null
) : RecyclerView.Adapter<RequestHorizontalAdapter.FormViewHolder>() {

    private val listRequest = listRequest
    private val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        return FormViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_request_horizontal,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val binding = holder.itemRequest
        var request = listRequest[position]
        binding.request = request
        binding.root.elevation = convertDpToPixel(8F, context)
        binding.tvRequestCode.text = "Mã Y/Cầu: ${request.requestCode}"
        if (request.requestName.isNullOrEmpty()){
            binding.tvRequestName.text = request.formName
        }else{
            binding.tvRequestName.text = request.requestName
        }
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(request) ?: return@setOnClickListener
        }
    }

    override fun getItemCount(): Int {
        return listRequest.size
    }


    fun submitListForm(forms: List<RequestEntity>?) {
        if (forms != null) {
            this.listRequest.clear()
            this.listRequest.addAll(forms)
            notifyDataSetChanged()
        }
    }

    class FormViewHolder(
        val itemRequest: ItemRequestHorizontalBinding
    ) : RecyclerView.ViewHolder(itemRequest.root)

}