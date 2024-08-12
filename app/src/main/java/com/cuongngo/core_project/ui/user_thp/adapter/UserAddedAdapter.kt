package com.cuongngo.core_project.ui.user_thp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.App
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.databinding.ItemUserHorizontalBinding
import com.cuongngo.core_project.utils.TFunc

class UserAddedAdapter(
    listUser: ArrayList<UserTHPEntity>,
    private val onAddListener: TFunc<UserTHPEntity>,
    private val onRemoveListener: TFunc<UserTHPEntity>,
    private val onItemSelected: TFunc<UserTHPEntity>
) : RecyclerView.Adapter<UserAddedAdapter.UserAddedViewHolder>() {

    private var listUser = listUser
    private var moreItem = UserTHPEntity(
        id = 8888,
        name = "Thêm"
    )
    private fun listUserAddMore():ArrayList<UserTHPEntity>{
        var list = listUser
        return if(listUser.isNotEmpty()){
            list.add(moreItem)
            list
        }else{
            list
        }
    }

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
        binding.tvUserName.text = user.name

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

    fun onItemClick(){
        //
    }

}