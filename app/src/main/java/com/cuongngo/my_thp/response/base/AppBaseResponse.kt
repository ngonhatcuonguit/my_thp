package com.cuongngo.my_thp.response.base

import com.cuongngo.my_thp.response.BaseModel

data class AppBaseResponse<T> (
    val data: T?,
    val response_status: ResponseStatus?,
): BaseModel()

data class ResponseStatus(
    val status: String?,
    val messages: List<MessageResponse>?,
): BaseModel()
data class MessageResponse(
    val type: String?,
    val message: String?
): BaseModel()