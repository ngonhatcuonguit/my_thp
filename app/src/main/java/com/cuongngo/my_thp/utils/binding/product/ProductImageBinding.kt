package com.cuongngo.my_thp.utils.binding.product

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cuongngo.my_thp.utils.MyDrawableCompat

@BindingAdapter("loadImageHotNew")
fun loadImageHotNew(view: ImageView, url: String?) {
    Glide.with(view)
        .load(url)
        .apply(RequestOptions().placeholder(MyDrawableCompat.createProgressDrawable(view.context)))
        .apply(RequestOptions.centerCropTransform())
        .into((view))
}

