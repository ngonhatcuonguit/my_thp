package com.cuongngo.core_project.ui.request_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.ProcessStep
import com.cuongngo.core_project.databinding.ItemInputProcessStepBinding


class RequestProcessStepAdapter(
    listProcessStep: ArrayList<ProcessStep>,
    private val onItemClickListener: ((ProcessStep) -> Unit)? = null
) : RecyclerView.Adapter<RequestProcessStepAdapter.RequestProcessStepViewHolder>() {

    private val listProcessStep = listProcessStep
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestProcessStepViewHolder {
        return RequestProcessStepViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_input_process_step,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RequestProcessStepViewHolder,
        position: Int
    ) {
        val binding = holder.itemStep
        var processStep = listProcessStep[position]
        binding.processStep = processStep
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(processStep) ?: return@setOnClickListener
        }
    }

    override fun getItemCount() = listProcessStep.size

    fun submitListProcessStep(listProcessStep: List<ProcessStep>?) {
        if (!listProcessStep.isNullOrEmpty()) {
            this.listProcessStep.clear()
            this.listProcessStep.addAll(listProcessStep)
            notifyDataSetChanged()
        }
    }

    class RequestProcessStepViewHolder(
        val itemStep: ItemInputProcessStepBinding
    ) : RecyclerView.ViewHolder(itemStep.root)


}