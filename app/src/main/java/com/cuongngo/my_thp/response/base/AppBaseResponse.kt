package com.cuongngo.my_thp.response.base

import com.cuongngo.my_thp.response.BaseModel

data class AppBaseResponse<T> (
    val status: String?,
    val data: T?,
    val message: String?,
): BaseModel()