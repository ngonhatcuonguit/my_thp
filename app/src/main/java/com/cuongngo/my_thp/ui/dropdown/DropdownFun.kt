package com.cuongngo.my_thp.ui.dropdown

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
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.model.SelectableDataModel
import com.cuongngo.my_thp.data.database.roomdb.entity.Option
import com.cuongngo.my_thp.utils.convertDpToPixel
import com.cuongngo.my_thp.utils.getScreenHeight

@SuppressLint("InflateParams", "ClickableViewAccessibility")
fun onShowPopupOption(
    context: Context,
    view: View,
    listOption: List<Option>,
    optionDefault: Option? = null,
    onSelectedListener: ((Option) -> Unit)? = null
) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val root = inflater.inflate(R.layout.layout_show_dropdown, null)
    val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view_dropdown)

    val popupWidth = context.resources.getDimensionPixelSize(R.dimen._200dp)
    var popupHeight = context.resources.getDimensionPixelSize(R.dimen._250dp)

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

    val adapter = DropdownAdapter(
        listData = listOption.map {
            SelectableDataModel(it)
        },
        optionDefault = optionDefault
    ) { selectedOption ->
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

    if(listOption.lastOrNull()?.id == 75L){
        optionPopupWindow.showAtLocation(
            parentView,      // You can use the root view of your layout here
            Gravity.CENTER,  // This will center the popup both horizontally and vertically
            250,               // X offset, no need for any horizontal offset
            positionOfIcon - popupHeight    // Y offset, no need for any vertical offset
        )
    }else{
        optionPopupWindow.showAtLocation(
            parentView,      // You can use the root view of your layout here
            Gravity.CENTER,  // This will center the popup both horizontally and vertically
            60,               // X offset, no need for any horizontal offset
            positionOfIcon - popupHeight     // Y offset, no need for any vertical offset
        )
    }

//    if (positionOfIcon > height) {
//        // when parent view is at the bottom of the screen show popup up
//        val offsetY = -popupHeight
//        optionPopupWindow.showAtLocation(
//            parentView,
//            Gravity.NO_GRAVITY,
//            popupOffsetX,
//            values[1] + offsetY
//        )
//    } else {
//        // when parent view is at the top of the screen show popup down
//        val offsetY = view.height
//        optionPopupWindow.showAtLocation(
//            parentView,
//            Gravity.NO_GRAVITY,
//            popupOffsetX,
//            values[1] + offsetY
//        )
//    }
}