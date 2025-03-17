package com.cuongngo.my_thp.ui.list_request.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.data.database.roomdb.entity.RequestEntity
import com.cuongngo.my_thp.databinding.ItemRequestHorizontalBinding
import com.cuongngo.my_thp.utils.convertDpToPixel
import com.cuongngo.my_thp.utils.status.RequestStatusVNBinding
import com.cuongngo.my_thp.utils.status.SetBackgroundRequestStatus

class RequestHorizontalAdapter(
    context: Context,
    listRequest: ArrayList<RequestEntity>,
    private val onItemClickListener: ((RequestEntity) -> Unit)? = null
) : RecyclerView.Adapter<RequestHorizontalAdapter.RequestViewHolder>() {

    private val listRequest = listRequest
    private val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_request_horizontal,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val binding = holder.itemRequest
        var request = listRequest[position]
        with(binding){
            ivPushData.setOnClickListener {
                flProgressBar.isVisible = true
                binding.ivPushData.isVisible = false
                onItemClickListener?.invoke(request)
            }
            flProgressBar.setOnClickListener {
//                flProgressBar.isVisible = false
//                ivPushData.isVisible = true
            }
            request = request
            root.elevation = convertDpToPixel(8F, context)
            tvRequestCode.text = "Mã: ${request.requestCode}"
            if (request.requestName.isNullOrEmpty()){
                binding.tvRequestName.text = request.formName
            }else{
                tvRequestName.text = request.requestName
            }
            tvStatus.text = RequestStatusVNBinding(request.status)
            SetBackgroundRequestStatus(tvStatus,request.status)
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

    fun refreshItem(requestEntity: RequestEntity?) {
        if (requestEntity != null) {
            val oldData = listRequest.find {
                requestEntity.requestID == it.requestID
            }
            val index = listRequest.indexOf(oldData)
            listRequest[index] = requestEntity
            notifyItemChanged(index)
        }
    }
    fun refreshRemoveItem(requestEntity: RequestEntity?) {
            if (requestEntity != null) {
                val oldData = listRequest.find {
                    requestEntity.requestCode == it.requestCode
                }
                val index = listRequest.indexOf(oldData)
                listRequest[index] = requestEntity
                listRequest.remove(requestEntity)
                notifyItemRemoved(index)
            }
        }

    class RequestViewHolder(
        val itemRequest: ItemRequestHorizontalBinding
    ) : RecyclerView.ViewHolder(itemRequest.root)

}