package com.cuongngo.core_project.utils.status

fun RequestStatusVNBinding(status: Int):String{
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
