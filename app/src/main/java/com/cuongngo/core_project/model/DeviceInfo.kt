package com.cuongngo.core_project.model

import com.cuongngo.core_project.response.BaseModel

data class DeviceInfo(
    var manufacturer: String?,
    var model: String?,
    var osVersion: String?,
    var apiLevel: Int?,
    var device: String?,
    var product: String?,
    var brand: String?,
    var hardware: String?,
    var id: String?,
    var user: String?,
    var host: String?,
    var display: String?
): BaseModel()