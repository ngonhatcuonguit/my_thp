package com.cuongngo.my_thp.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.databinding.ItemNotiBinding
import com.cuongngo.my_thp.ui.notification.data.NotificationResponse

class NotiAdapter(
    context: Context,
    listNoti: ArrayList<NotificationResponse>,
    private val onItemClickListener: ((NotificationResponse) -> Unit)? = null
) : RecyclerView.Adapter<NotiAdapter.NotiViewHolder>() {

    private var listNoti = listNoti
    private var context = context

    override fun getItemCount(): Int {
        return listNoti.size
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        val binding = holder.itemNoti
        var noti = listNoti[position]
        binding.noti = noti
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(noti) ?: return@setOnClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        return NotiViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_noti,
                parent,
                false
            )
        )

    }

    fun submitListNoti(page: Int?, notis: List<NotificationResponse>?) {
        if (notis != null) {
            if(page == 0){
                this.listNoti.clear()
                this.listNoti.addAll(notis)
            }else{
                this.listNoti.addAll(notis)
            }
            notifyDataSetChanged()
        }
    }

    class NotiViewHolder(val itemNoti: ItemNotiBinding) : RecyclerView.ViewHolder(itemNoti.root)


}