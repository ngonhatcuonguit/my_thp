package com.cuongngo.my_thp.ui.list_request.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.my_thp.data.database.roomdb.entity.RequestEntity
import com.cuongngo.my_thp.databinding.ItemRequestBinding
import com.cuongngo.my_thp.utils.convertDpToPixel
import com.cuongngo.my_thp.utils.status.RequestStatusVNBinding
import com.cuongngo.my_thp.utils.status.SetBackgroundRequestStatus

class RequestAdapter(
    context: Context,
    listRequest: ArrayList<RequestEntity>,
    private val onItemClickListener: ((RequestEntity) -> Unit)? = null
): RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    private val listRequest = listRequest
    private val context = context

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val binding = holder.itemRequest
        var request = listRequest[position]
        binding.root.elevation = convertDpToPixel(8F, context)
        binding.request = request
        if (request.requestName.isNullOrEmpty()){
            binding.tvRequestName.text = request.formName
        }else{
            binding.tvRequestName.text = request.requestName
        }
        binding.tvRequestCode.text = request.requestCode
        binding.tvStatus.text = RequestStatusVNBinding(request.status)
        SetBackgroundRequestStatus(binding.tvStatus,request.status)
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