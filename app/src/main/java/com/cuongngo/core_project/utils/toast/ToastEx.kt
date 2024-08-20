package com.cuongngo.core_project.utils.toast

import android.content.Context
import com.cuongngo.core_project.R
import com.cuongngo.core_project.common.toast_custom.ToastEx
import com.cuongngo.core_project.common.toast_custom.ToastType

fun showMessageOnSyncDataSuccess(context: Context, success: Boolean){
    ToastEx
        .makeText(
            context,
            if (success)
                "Đồng bộ dữ liệu thành công"
            else
                "Đồng bộ dữ liệu Không thành công",
            if (success){
                ToastType.SUCCESS
            }else ToastType.WARNING,

            if (success){
                R.drawable.ic_tick
            }else R.drawable.ic_toast_warning,
            isForeground = true
        )
        .show()
}
fun showMessageCheckInternet(context: Context, isConnect: Boolean){
    ToastEx
        .makeText(
            context,
            if (isConnect)
                "Thiết bị đang được kết nối mạng"
            else
                "Thiết bị không được kết nối mạng",

            if (isConnect){
                ToastType.SUCCESS
            }else ToastType.ERROR,

            if (isConnect){
                R.drawable.ic_tick
            }else R.drawable.ic_toast_err,
            isForeground = true
        )
        .show()
}

fun showMessageSaveData(context: Context, done: Boolean){
    ToastEx
        .makeText(
            context,
            if (done)
                "Lưu dữ liệu thành công"
            else
                "Đã có lỗi xảy ra! Vui lòng kiểm tra lại",

            if (done){
                ToastType.SUCCESS
            }else ToastType.ERROR,

            if (done){
                R.drawable.ic_tick
            }else R.drawable.ic_toast_err,
            isForeground = true
        )
        .show()
}
fun showMessageToast(context: Context, done: Boolean, contentDone: String?, contentFail: String?){
    ToastEx
        .makeText(
            context,
            if (done)
                contentDone
            else
                contentFail,

            if (done){
                ToastType.SUCCESS
            }else ToastType.ERROR,

            if (done){
                R.drawable.ic_tick
            }else R.drawable.ic_toast_err,
            isForeground = true
        )
        .show()
}