package com.cuongngo.core_project.base.model

import com.cuongngo.core_project.response.BaseModel

data class WrapperModel<T: Any>(
    var data: T? = null
): BaseModel()