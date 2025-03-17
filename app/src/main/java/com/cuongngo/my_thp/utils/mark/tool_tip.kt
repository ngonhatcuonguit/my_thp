package com.cuongngo.my_thp.utils.mark

import android.content.Context
import android.view.View
import android.widget.TextView
import com.cuongngo.my_thp.R
import com.skydoves.balloon.createBalloon

fun setupTooltip(context: Context, text: String?, view: View?) {
    val balloon = createBalloon(context) {
        setLayout(R.layout.layout_tooltip)
        setCornerRadius(5f)
        setMarginRight(16)
        setArrowVisible(false)
        setArrowBottomPadding(10)
        setBackgroundColorResource(R.color.white)
        setLifecycleOwner(lifecycleOwner)
    }
    balloon.getContentView().findViewById<TextView>(R.id.tv_content_tooltip).text = text
    view?.let { balloon.showAlignBottom(it) }
}