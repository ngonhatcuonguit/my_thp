package com.cuongngo.core_project.response.thp_form

import com.cuongngo.core_project.response.BaseModel
import com.google.gson.annotations.SerializedName

data class THPForm(
    @SerializedName("list_form") val listForm: Map<String, Form>
): BaseModel()


data class Form(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("status") val status: Int,
    @SerializedName("created") val created: String,
    @SerializedName("updated") val updated: String,
): BaseModel()

data class ListField(
    @SerializedName("frequency_check") val fields: List<Field>,
): BaseModel()
data class Field(
    @SerializedName("id") val id: String,
    @SerializedName("label") val label: String,
    @SerializedName("type") val type: String,
    @SerializedName("required") val required: Boolean,
    @SerializedName("placeholder") val placeholder: String,
    @SerializedName("options") val options: List<Option>
) : BaseModel()

data class Option(
    @SerializedName("value") val value: String,
    @SerializedName("label") val label: String
) : BaseModel()

data class SubmitButton(
    @SerializedName("label") val label: String,
    @SerializedName("action") val action: String
) : BaseModel()