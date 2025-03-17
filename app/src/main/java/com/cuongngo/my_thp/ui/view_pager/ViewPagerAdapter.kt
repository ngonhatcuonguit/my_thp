package com.cuongngo.my_thp.ui.view_pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.databinding.ItemBannerSliderBinding
import com.cuongngo.my_thp.response.news.News

class ViewPagerAdapter (
    private val data: List<News> = emptyList(),
    private val viewPager2: ViewPager2,
    private val onDetachFromWindow: (() -> Unit)? = null,
    private val onAttachToWindow: (() -> Unit)? = null,
    private val onItemClick: ((News) -> Unit)? = null
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    private var infiniteData: MutableList<News> = ArrayList()

    init {
        infiniteData.apply {
            infiniteData.addAll(data)
            infiniteData.addAll(data)
            infiniteData.addAll(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_banner_slider,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val binding = holder.itemBannerSliderBinding
        val hotNew = infiniteData[position]
        binding.hotNew = hotNew
        binding.root.setOnClickListener {
            onItemClick?.invoke(hotNew)
        }
        binding.imgBanner.setBackgroundResource(hotNew.drawableId)
    }

    fun submitList(listHotNew: ArrayList<News>){
        this.infiniteData.addAll(listHotNew)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return infiniteData.size
    }

    fun getRealItemCount() = itemCount/3

    fun getRealPosition(position: Int)  = position % data.size

    override fun onViewAttachedToWindow(holder: ViewPagerViewHolder) {
        super.onViewAttachedToWindow(holder)
        onDetachFromWindow?.invoke()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        onAttachToWindow?.invoke()
    }

    class ViewPagerViewHolder(
        val itemBannerSliderBinding: ItemBannerSliderBinding
    ): RecyclerView.ViewHolder(itemBannerSliderBinding.root)

}