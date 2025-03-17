package com.cuongngo.my_thp.ui.request_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.data.database.roomdb.entity.Body
import com.cuongngo.my_thp.databinding.ItemSheetBinding
import com.cuongngo.my_thp.utils.convertDpToPixel

class SheetAdapter(
    context: Context,
    listBody: List<Body>,
    private val onItemClickListener: ((Body) -> Unit)? = null
) : RecyclerView.Adapter<SheetAdapter.SheetViewHolder>() {

    private val listSheet = listBody.toMutableList() ?: mutableListOf()
    private val context = context

    class SheetViewHolder(
        val item: ItemSheetBinding
    ) : RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetViewHolder {
        return SheetViewHolder(
            ItemSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listSheet.size

    override fun onBindViewHolder(holder: SheetViewHolder, position: Int) {
        val binding = holder.item
        var sheet = listSheet[position]
        var countHaveValue = 0
        var countHeader = 0
        sheet.list_field?.forEach {
            if(!it.value.isNullOrEmpty()){
                countHaveValue++
            }
            if (it.type == "header"){
                countHeader++
            }
        }
        if(countHaveValue == ((sheet.list_field?.size ?: 0) - countHeader) || sheet.is_done == true){
            binding.tvStatus.setBackgroundResource(R.drawable.shape_primary_radius6)
        }else{
            binding.tvStatus.setBackgroundResource(R.drawable.shape_yellow_radius6)
        }
        var countSize = (sheet.list_field?.size ?: 0) - countHeader
        binding.root.elevation = convertDpToPixel(8F, context)
        binding.sheet = sheet
        binding.tvStatus.text = "${countHaveValue.toString() ?: 0}/${countSize}"
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(sheet) ?: return@setOnClickListener
        }
    }

    fun submitListSheet(listBody: List<Body>?) {
        if (listBody != null) {
            this.listSheet.clear()
            this.listSheet.addAll(listBody)
            notifyDataSetChanged()
        }
    }

    fun getListBody(): List<Body> {
        return listSheet
    }

}