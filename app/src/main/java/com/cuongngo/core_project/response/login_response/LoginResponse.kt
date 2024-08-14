package com.cuongngo.core_project.response.login_response

import com.cuongngo.core_project.response.BaseModel

data class LoginResponse(
    var token: String?,
    var userName: String?,
    var device_is_active: Boolean?,
    var expires_in: Long?,
    var message: String?,
): BaseModel()