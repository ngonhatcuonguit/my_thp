package com.cuongngo.core_project.base.model

import com.cuongngo.core_project.response.BaseModel

data class DialogModel(
    var title: String?,
    var subTitle: String?,
    var content: String?,
    var edtValue: String? = null,
    var edtTitle: String? = null,
    var edtHint: String? = null,
    var typeInput: String? = null,
    var isSingle: Boolean? = false,
    var leftButtonTitle: String?,
    var rightButtonTitle: String?
) : BaseModel()
