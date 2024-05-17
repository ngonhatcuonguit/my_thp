package com.cuongngo.core_project.response.login_response

import com.cuongngo.core_project.response.BaseModel

data class LoginResponse(
    var access_token: String?,
    var token_type: String?,
    var userName: String?,
    var fullName: String?,
    var appPrivacyApproved: String?,
    var expires_in: Long?,
): BaseModel(){

}