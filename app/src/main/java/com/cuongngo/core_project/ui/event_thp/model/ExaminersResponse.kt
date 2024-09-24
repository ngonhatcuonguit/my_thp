package com.cuongngo.core_project.ui.event_thp.model

import com.cuongngo.core_project.response.BaseModel

data class ExaminersResponse(
    var listGK: List<Examiner>?
):BaseModel()

data class Examiner(
    var Id: Int?,
    var Name: String?,
    var JobTitle: String?,
    var ScorePercent: Float?,
    var Type: Float?,
): BaseModel()

data class UpdateScoreResponse(
    var Result: Boolean?,
): BaseModel()

data class ExamResponse(
    var Id: Int?,
    var Name: String?,
    var Department: String?,
    var Division: String?,
    var UserName: String?,
    var OrderBy: Int?,
    var Status: Int?,
    var RegisDate: String?,
    var TheLoai: String?,
    var IsNew: Int?
):BaseModel()