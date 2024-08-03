package com.cuongngo.core_project.ui.list_request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.databinding.ItemRequestBinding
import com.cuongngo.core_project.ext.WTF

class RequestAdapter(
    listRequest: ArrayList<RequestEntity>,
    private val onItemClickListener: ((RequestEntity) -> Unit)? = null
): RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    private val listRequest = listRequest

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val binding = holder.itemRequest
        var request = listRequest[position]
        binding.request = request
        binding.tvRequestCode.text = "Mã yêu cầu: ${request.requestCode}"
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(request) ?: return@setOnClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(
            ItemRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listRequest.size

    fun submitListRequest(requests: List<RequestEntity>?) {
        if (requests != null) {
            this.listRequest.clear()
            this.listRequest.addAll(requests)
            notifyDataSetChanged()
        }
    }

    fun refreshItem(requestEntity: RequestEntity?){
        if (requestEntity != null){
            val oldData = listRequest.find {
                requestEntity.requestID == it.requestID
            }
            val index = listRequest.indexOf(oldData)
            listRequest[index] = requestEntity
            notifyItemChanged(index)
        }
    }

    class RequestViewHolder(
        val itemRequest: ItemRequestBinding
    ) : RecyclerView.ViewHolder(itemRequest.root)
}