package com.cuongngo.my_thp.ui.event_thp.model

import com.cuongngo.my_thp.response.BaseModel

data class ExaminersResponse(
    var status: String?,
    var data: List<Examiner>?
):BaseModel()

data class Examiner(
    var Id: Int?,
    var Name: String?,
    var Status: Int?,
    var JobTitle: String?,
    var ScorePercent: Float?,
    var Type: String?,
): BaseModel()

data class UpdateScoreResponse(
    var status: String?,
    var data: ResultUpdate?,
): BaseModel()

data class ResultUpdate(
    var Result: Boolean?
): BaseModel()

data class ExamResponse(
    var status: String?,
    var data: Exam?
):BaseModel()

data class Exam(
    var Id: Int?,
    var Name: String?,
    var Department: String?,
    var Division: String?,
    var UserName: String?,
    var OrderBy: Int?,
    var Status: Int?,
    var RegisDate: String?,
    var TheLoai: String?,
    var IsNew: Int?,
    var IsLast: Boolean?,
):BaseModel()