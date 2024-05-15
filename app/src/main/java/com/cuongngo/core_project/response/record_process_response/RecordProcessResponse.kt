package com.cuongngo.core_project.response.record_process_response

import com.cuongngo.core_project.response.BaseModel

data class RecordProcessResponse(
    val id: Int?,
    val name: String?,
    var value: String?
)  : BaseModel()
