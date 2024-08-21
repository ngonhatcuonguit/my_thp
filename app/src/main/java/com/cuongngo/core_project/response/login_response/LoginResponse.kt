package com.cuongngo.core_project.response.login_response

import com.cuongngo.core_project.response.BaseModel

data class LoginResponse(
    var token: String?,
    var userName: String?,
    var device_is_active: Boolean?,
    var employee_number: String?,
    var position_id: String?,
    var employee_sap_number: Long?,
    var organization_name: String?,
    var organization_id: Long?,
    var position_name: String?,
    var expires_in: Long?,
    var message: String?,
    var email: String?,
    var first_name: String?,
    var last_name: String?,
): BaseModel()

data class ApiResponse(
    var status: String?,
    var message: String?
): BaseModel()