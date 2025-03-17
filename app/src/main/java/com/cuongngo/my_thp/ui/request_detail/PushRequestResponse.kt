package com.cuongngo.my_thp.ui.request_detail

import com.cuongngo.my_thp.response.BaseModel

data class PushRequestResponse(
    val status: String?,
    var data: List<PushRequestModel>?
) : BaseModel()

data class PushRequestModel(
    var id: Int?,
    var device_code : String?,
    var json_data : String?,
    var form_structure_id : Int?,
    var process_id : Int?,
    var version : String?,
    var created : String?,
    var created_by : String?,
    var updated : String?,
    var request_code : String?,
): BaseModel()

data class RequestBodyPush(
    var device_code: String,
    var json_data: String?,
    var process_id: String?,
    var version: String?,
    var request_code: String?,
    var form_structure_id: Long?,
    var status: Int?,
): BaseModel()


data class GetRequestStatusResponse(
    val status: String?,
    var data: List<RequestCodeResult>?
) : BaseModel()
data class PushRequestCodeBody(
    var requests: List<String>
): BaseModel()

data class RequestCodeResult(
    var id: String?,
    var status: Int?
):BaseModel()