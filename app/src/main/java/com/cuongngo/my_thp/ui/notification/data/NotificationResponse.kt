package com.cuongngo.my_thp.ui.notification.data

import com.cuongngo.my_thp.response.BaseModel

data class NotificationResponse(
    var id: Int?,
    var type: String?,
    var title: String?,
    var body: String?,
    val status: Int?,
    val readDate: String?,
    val isActive: Boolean?,
    val objectId: String?,
    val userId: String?,
    var created: String?,
    var isRead: Boolean?,
): BaseModel()
