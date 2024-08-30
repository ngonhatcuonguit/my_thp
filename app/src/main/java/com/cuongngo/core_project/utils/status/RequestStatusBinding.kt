package com.cuongngo.core_project.utils.status

import android.view.View
import com.cuongngo.core_project.R

fun RequestStatusVNBinding(status: Int?):String{
    when(status){
        0 -> {
            return "Đang soạn thảo"
        }
        1 -> {
            return "Đã trình ký"
        }
        2 -> {
            return "Đã ký"
        }
        3 -> {
            return "Reject"
        }
        else -> {
            return "Status $status"
        }
    }
}
fun RequestStatusItemBinding(status: Int?):String{
    when(status){
        0 -> {
            return "Processing"
        }
        1 -> {
            return "Sent"
        }
        2 -> {
            return "Approved"
        }
        3 -> {
            return "Reject"
        }
        else -> {
            return "Status $status"
        }
    }
}

fun SetBackgroundRequestStatus(binding: View, status: Int?){

    when(status){
        0 -> {
            binding.setBackgroundResource(R.drawable.shape_orange_radius16)
        }
        1 -> {
            binding.setBackgroundResource(R.drawable.shape_blue_radius12)
        }
        2 -> {
            binding.setBackgroundResource(R.drawable.shape_primary_radius6)
        }
        3 -> {
            binding.setBackgroundResource(R.drawable.shape_red_radius12)
        }
        else -> {
            binding.setBackgroundResource(R.drawable.shape_orange_radius16)
        }
    }
}
