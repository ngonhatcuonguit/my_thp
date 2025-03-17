package com.cuongngo.my_thp.common.view.view_ex

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.common.enum.FontType


open class TextViewEx: androidx.appcompat.widget.AppCompatTextView {
    private var cornerRadius = 0f
    private val clipPath by lazy {
        Path()
    }

    constructor(context: Context): super(context){
        onInitProperties(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs){
        onInitProperties(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        onInitProperties(context, attrs)
    }

    open fun onInitProperties(context: Context, attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewEx)
        val textStyle = typedArray.getInt(R.styleable.TextViewEx_fontType, FontType.NORMAL.typeValue)
        val cornerRadiusInDimension = typedArray.getDimension(R.styleable.TextViewEx_rounded_radius, 0f)
        this.cornerRadius = cornerRadiusInDimension
        setFontType(FontType.valueOf(textStyle))
        typedArray.recycle()
    }

    fun setFontType(@StyleRes fontType: Int){
        setTypeface(typeface, fontType)
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

    override fun onDraw(canvas: Canvas) {
        if (cornerRadius > 0f && canvas != null) {
            val rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
            clipPath.reset()
            clipPath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CCW)
            try {
                canvas.clipPath(clipPath)
            } catch (exception: UnsupportedOperationException) {
                setLayerType(LAYER_TYPE_SOFTWARE, null)
                try {
                    canvas.clipPath(clipPath)
                } catch (exception2: UnsupportedOperationException) {
                    // shouldn't happen, but just in case
                }
            }
        }
        super.onDraw(canvas)
    }
}