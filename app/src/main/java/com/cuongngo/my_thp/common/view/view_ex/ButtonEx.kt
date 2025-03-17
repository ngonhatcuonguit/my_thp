package com.cuongngo.my_thp.common.view.view_ex

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.common.enum.FontType

open class ButtonEx: androidx.appcompat.widget.AppCompatButton {

    constructor(context: Context): super(context){
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init(context, attrs)
    }

    open fun init(context: Context, attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonEx)
        val textStyle = typedArray.getInt(R.styleable.ButtonEx_fontType, FontType.NORMAL.typeValue)
        setFontType(FontType.valueOf(textStyle))
        typedArray.recycle()
    }

    fun setFontType(style: FontType){
        val fontSrc = when (style) {
            FontType.THIN -> {
                R.font.bep_thin
            }
            FontType.EXTRA_LIGHT -> {
                R.font.bep_extra_light
            }
            FontType.LIGHT -> {
                R.font.bep_light
            }
            FontType.EXTRA_BOLD -> {
                R.font.bep_extra_bold
            }
            FontType.BLACK -> {
                R.font.bep_black
            }
            FontType.NORMAL -> {
                R.font.bep_regular
            }
            FontType.MEDIUM -> {
                R.font.bep_medium
            }
            FontType.ITALIC -> {
                R.font.bep_italic
            }
            FontType.SEMI_BOLD -> {
                R.font.bep_semi_bold
            }
            FontType.BOLD -> {
                R.font.bep_bold
            }
            FontType.SEMI_BOLD_ITALIC -> {
                R.font.bep_semi_bold_italic
            }
            FontType.BOLD_ITALIC -> {
                R.font.bep_bold_italic
            }
        }
        typeface =  ResourcesCompat.getFont(context, fontSrc)
    }
}