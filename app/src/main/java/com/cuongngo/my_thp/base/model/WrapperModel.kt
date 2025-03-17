package com.cuongngo.my_thp.base.model

import com.cuongngo.my_thp.response.BaseModel

data class WrapperModel<T: Any>(
    var data: T? = null
): BaseModel()