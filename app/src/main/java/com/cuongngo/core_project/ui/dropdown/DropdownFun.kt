package com.cuongngo.core_project.ui.dropdown

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.utils.convertDpToPixel
import com.cuongngo.core_project.utils.getScreenHeight

@SuppressLint("InflateParams", "ClickableViewAccessibility")
fun onShowPopupOption(
    context: Context,
    view: View,
    listOption: ArrayList<Option>,
    onSelectedListener: ((Option) -> Unit)? = null
) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val root = inflater.inflate(R.layout.layout_show_dropdown, null)
    val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view_dropdown)

    val popupWidth = context.resources.getDimensionPixelSize(R.dimen._300dp)
    val popupHeight = context.resources.getDimensionPixelSize(R.dimen._360dp)
    val optionPopupWindow = PopupWindow(
        root,
        popupWidth,
        popupHeight,
        true
    ).apply {
        // Set the custom background with elevation and border radius
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_dropdown_radius))
        elevation = convertDpToPixel(6F, context)
        isFocusable = true
        isOutsideTouchable = true
        setTouchInterceptor { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                dismiss()
                true
            } else {
                false
            }
        }
    }

    val adapter = DropdownAdapter(listOption) { selectedOption ->
        onSelectedListener?.invoke(selectedOption) ?: return@DropdownAdapter
        optionPopupWindow.dismiss()
    }
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)



    root.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_OUTSIDE) {
            optionPopupWindow.dismiss()
            true
        } else {
            false
        }
    }

    // Show popup menu centered horizontally
    val parentView = view.parent as View
    val location = IntArray(2)
    parentView.getLocationOnScreen(location)
    val parentCenterX = location[0] + parentView.width / 2
    val popupOffsetX = parentCenterX - popupWidth / 2


    // Show popup menu
    val values = IntArray(2)
    view.getLocationOnScreen(values)
    val positionOfIcon = values[1]
    val height = getScreenHeight() * 2 / 3
    if (positionOfIcon > height) {
        // when parent view is at the bottom of the screen show popup up
        val offsetY = -popupHeight
        optionPopupWindow.showAtLocation(
            parentView,
            Gravity.NO_GRAVITY,
            popupOffsetX,
            values[1] + offsetY
        )
    } else {
        // when parent view is at the top of the screen show popup down
        val offsetY = view.height
        optionPopupWindow.showAtLocation(
            parentView,
            Gravity.NO_GRAVITY,
            popupOffsetX,
            values[1] + offsetY
        )
    }
}