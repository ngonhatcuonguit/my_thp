package com.cuongngo.core_project.response.base

import com.cuongngo.core_project.response.BaseModel

data class AppBaseResponse<T> (
    val status: String?,
    val data: T?,
    val message: String?,
): BaseModel()