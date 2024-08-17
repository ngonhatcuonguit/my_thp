package com.cuongngo.core_project.common.toast_custom

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.cuongngo.core_project.R

enum class ToastType {
    SUCCESS,
    WARNING,
    INFORM,
    ERROR,
    NORMAL
}

open class ToastEx(context: Context) : Toast(context) {
    companion object {
        fun makeText(
            context: Context,
            message: String?,
            type: ToastType,
            @DrawableRes
            drawableId: Int,
            isForeground: Boolean = true
        ): Toast {
            return ToastEx(context)
                .makeText(
                    context,
                    message,
                    type,
                    drawableId,
                    isForeground
                )
        }
    }

    private fun makeText(
        context: Context,
        message: String?,
        type: ToastType,
        drawableId: Int? = null,
        isForeground: Boolean = true
    ): Toast {
        val toast = Toast(context)
        val layoutToast: View =
            LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null, false)

        val toastText: TextView = layoutToast.findViewById(R.id.toast_text)
        val toastType: LinearLayoutCompat = layoutToast.findViewById(R.id.toast_type)
        val toastIcon: ImageView = layoutToast.findViewById(R.id.toast_icon)

        toastText.text = message ?: ""

        when (type) {
            ToastType.SUCCESS -> {
                toastType.setBackgroundResource(R.drawable.toast_success_shape)
                toastIcon.setImageResource(drawableId ?: R.drawable.ic_toast_success)
                toastText.setTextColor(ContextCompat.getColor(context, R.color.black_1c))
            }

            ToastType.WARNING -> {
                toastType.setBackgroundResource(R.drawable.toast_warning_shape)
                toastIcon.setImageResource(drawableId ?: R.drawable.ic_toast_warning)
                toastText.setTextColor(ContextCompat.getColor(context, R.color.black_1c))
            }

            ToastType.INFORM -> {
                toastType.setBackgroundResource(R.drawable.toast_inform_shape)
                toastIcon.setImageResource(drawableId ?: R.drawable.ic_toast_info)
                toastText.setTextColor(ContextCompat.getColor(context, R.color.black_1c))
            }

            ToastType.ERROR -> {
                toastType.setBackgroundResource(R.drawable.toast_err_shape)
                toastIcon.setImageResource(drawableId ?: R.drawable.ic_toast_err)
                toastText.setTextColor(ContextCompat.getColor(context, R.color.black_1c))
            }

            ToastType.NORMAL -> {
                toastType.setBackgroundResource(R.drawable.toast_normal_shape)
                toastIcon.setImageResource(drawableId ?: R.drawable.ic_toast_normal)
                toastText.setTextColor(ContextCompat.getColor(context, R.color.black_1c))
            }
        }
        duration = LENGTH_LONG
        return if (!isForeground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            toast
        } else {
            toast.apply {
                view = layoutToast
                setGravity(Gravity.TOP, 0, 60)
            }
        }
    }
}
