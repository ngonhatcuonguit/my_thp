package com.cuongngo.core_project.ui.request_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.ProcessStep
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.databinding.ItemInputProcessStepBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ui.bottom_sheet.SearchUserBottomSheet
import com.cuongngo.core_project.ui.user_thp.adapter.UserAddedAdapter
import com.cuongngo.core_project.utils.TFunc
import com.cuongngo.core_project.utils.getScreenHeight

class RequestProcessStepAdapter(
    private val context: Context,
    listProcessStep: ArrayList<ProcessStep>,
    private val fragmentManager: FragmentManager,
    private val onItemClickListener: TFunc<ProcessStep>? = null,
    private val onChangeProcessStep: TFunc<List<ProcessStep>>,
) : RecyclerView.Adapter<RequestProcessStepAdapter.RequestProcessStepViewHolder>() {

    private var listProcessStep = listProcessStep
    private lateinit var ownerAdapter: UserAddedAdapter
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
        with(binding) {
            tvTitleStep.text = processStep.name
            tvDurationStep.text = processStep.duration
        }
        holder.bind(processStep)
    }

    override fun getItemCount() = listProcessStep.size

    fun submitListProcessStep(listProcessStep: List<ProcessStep>?) {
        if (!listProcessStep.isNullOrEmpty()) {
            this.listProcessStep.clear()
            this.listProcessStep.addAll(listProcessStep)
            notifyDataSetChanged()
        }
    }

    inner class RequestProcessStepViewHolder(
        val itemStep: ItemInputProcessStepBinding
    ) : RecyclerView.ViewHolder(itemStep.root) {
        fun bind(processStep: ProcessStep) {
            with(itemStep) {
                if(processStep.owner.isNullOrEmpty()){
                    tvHintStep.setOnClickListener {
//                    onItemClickListener?.invoke(processStep)
                        showSearchUserBottomSheet()
                    }
                }else{

                }

                // Set up the nested RecyclerView
                val gridLayoutManager =
                    GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                var displayListOwner = listProcessStep[adapterPosition].owner?.toMutableList() ?: mutableListOf()
                var moreItem = UserTHPEntity(
                    personal_number = 0,
                    first_name = "Thêm"
                )
                if (displayListOwner.isNotEmpty() && !displayListOwner.contains(moreItem)){
                    displayListOwner.add(moreItem)
                }
                ownerAdapter = UserAddedAdapter(
                    context,
                    displayListOwner,
                    onItemSelected = {
                        //show tool tip
                    },
                    onAddListener = {
                        showSearchUserBottomSheet()
                    },
                    onRemoveListener = { data ->
                        processStep.let { stepData ->
                            var currentListOwner =
                                stepData.owner?.toMutableList() ?: mutableListOf()
                            currentListOwner.remove(data)
                            var currentStep = stepData
                            currentStep.owner = currentListOwner
                            listProcessStep.set(adapterPosition, currentStep)
                        }
                        notifyItemChanged(adapterPosition, listProcessStep[adapterPosition])
                        WTF("addOwner $listProcessStep")
                        if (ownerAdapter.itemCount == 1) {
                            itemStep.tvHintStep.text = "Tìm kiếm user"
                            itemStep.rvListUserStep.isVisible = false
                        } else {
                            itemStep.rvListUserStep.isVisible = true
                            itemStep.tvHintStep.text = ""
                        }
                    }
                )
                rvListUserStep.apply {
                    layoutManager = gridLayoutManager
                    adapter = ownerAdapter
                }
            }
        }

        private fun showSearchUserBottomSheet() {
            SearchUserBottomSheet(
                requestData = null,
                heightValue = (getScreenHeight() * 0.95).toInt()
            ).setOnUserSelected {
                it?.let { data ->
                    var currentOwners = listProcessStep[adapterPosition].owner?.toMutableList() ?: mutableListOf()
                    if (!currentOwners.contains(data)) {
                        currentOwners.add(data)
                        listProcessStep[adapterPosition].owner = currentOwners
                        notifyItemChanged(adapterPosition, listProcessStep[adapterPosition])
                        WTF("addOwner $listProcessStep")
                    } else {
                        //show warning
                    }
//                    onStepAddUserListener.invoke(listProcessStep)
                }
                if (ownerAdapter.itemCount == 1) {
                    itemStep.tvHintStep.text = "Tìm kiếm user"
                    itemStep.rvListUserStep.isVisible = false
                } else {
                    itemStep.rvListUserStep.isVisible = true
                    itemStep.tvHintStep.text = ""
                }
            }.show(fragmentManager, "add_owner_step")
        }

    }

}