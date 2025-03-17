package com.cuongngo.my_thp.ui.user_thp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.my_thp.App
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.my_thp.databinding.ItemUserHorizontalBinding
import com.cuongngo.my_thp.utils.TFunc
import com.cuongngo.my_thp.utils.mark.setupTooltip

class UserAddedAdapter(
    context: Context,
    listUser: List<UserTHPEntity>,
    private val onAddListener: TFunc<UserTHPEntity>,
    private val onRemoveListener: TFunc<UserTHPEntity>,
    private val onItemSelected: TFunc<UserTHPEntity>
) : RecyclerView.Adapter<UserAddedAdapter.UserAddedViewHolder>() {

    private var context = context
    private var listUser = listUser.toMutableList() ?: mutableListOf()
    private var moreItem = UserTHPEntity(
        personal_number = 0,
        first_name = "Thêm"
    )

    fun submitNewList(listUser: List<UserTHPEntity>?){
        if (listUser.isNullOrEmpty()){
            this.listUser.clear()
        }else{
            this.listUser.clear()
            this.listUser.addAll(listUser)
            this.listUser.add(moreItem)
        }
        notifyDataSetChanged()
    }
    class UserAddedViewHolder(
        var item: ItemUserHorizontalBinding
    ): RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAddedViewHolder {
        return UserAddedViewHolder(ItemUserHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: UserAddedViewHolder, position: Int) {
        var binding = holder.item
        var user = listUser[position]
        var positionName = if (!user.position_name.isNullOrEmpty()){
            " - " + user.position_name
        }else{
            ""
        }
        var lastName = if (user.last_name.isNullOrEmpty()){
            ""
        }else{
            " " + user.last_name
        }
        var initial = if (user.initial.isNullOrEmpty()){
            ""
        }else{
            " - " + user.initial
        }

        if(user != moreItem){
            binding.tvUserName.text = user.first_name + lastName + initial + positionName
        }else{
            binding.tvUserName.text = user.first_name
        }

        binding.flRemove.setOnClickListener {
            onRemoveListener.invoke(user)
        }
        binding.ivAddNew.setOnClickListener {
            onAddListener.invoke(user)
        }


        if (position == listUser.size-1){
            binding.ivAddNew.isVisible = true
            binding.flRemove.isVisible = false

            binding.tvUserName.setTextColor(App.getResources().getColor(R.color.blue_accent))
            binding.tvUserName.setOnClickListener {
                onAddListener.invoke(user)
            }
            binding.ivAddNew.setOnClickListener {
                onAddListener.invoke(user)
            }
        }else{
            binding.flRemove.isVisible = true
            binding.ivAddNew.isVisible = false

            binding.tvUserName.setTextColor(App.getResources().getColor(R.color.black_1c))
            binding.tvUserName.setOnClickListener {
                onItemSelected.invoke(user)
                var positionName = if(user.position_name.isNullOrEmpty()){
                    ""
                }else{
                    "-" + user.position_name
                }
                val text = "${user.first_name} ${user.last_name}-${user.initial}$positionName"
                    setupTooltip(context, text, binding.tvUserName)
            }
            binding.flRemove.setOnClickListener {
                onRemoveListener.invoke(user)
            }
        }

    }

    fun onRemoveItem(userTHPEntity: UserTHPEntity){
        if (listUser.size > 1){
            listUser.remove(userTHPEntity)
        }else{
            listUser = arrayListOf()
        }
        notifyDataSetChanged()
    }

    fun onAddNew(userTHPEntity: UserTHPEntity){
        if (listUser.size > 0){
            listUser.add(listUser.size-1,userTHPEntity)
        }else{
            listUser.add(userTHPEntity)
            listUser.add(moreItem)
        }
        notifyDataSetChanged()
    }
    fun getListInformer(): List<UserTHPEntity>{
        return listUser
    }

}