package com.cuongngo.core_project.ui.request_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.data.database.roomdb.entity.Body
import com.cuongngo.core_project.databinding.ItemSheetBinding

class SheetAdapter(
    listBody: ArrayList<Body>,
    private val onItemClickListener: ((Body) -> Unit)? = null
): RecyclerView.Adapter<SheetAdapter.SheetViewHolder>() {

    private val listSheet = listBody
    class SheetViewHolder(
        val item: ItemSheetBinding
    ): RecyclerView.ViewHolder(item.root)

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
        binding.sheet = sheet
        binding.root.setOnClickListener{
            onItemClickListener?.invoke(sheet) ?: return@setOnClickListener
        }
    }

    fun submitListSheet(listBody: List<Body>?){
        if (listBody != null){
            this.listSheet.clear()
            this.listSheet.addAll(listBody)
            notifyDataSetChanged()
        }
    }

}