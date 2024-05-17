package com.cuongngo.core_project.response.news

import com.cuongngo.core_project.response.BaseModel

data class HotNewResponse (
    var resultId: Int?,
    var message: String?,
    var data: List<News>?,
    var nextLink: String?,
    var paging: String?,
): BaseModel()

data class News(
    var id: Int?,
    var title: String?,
    var imageUrl: String?,
    var releaseDate: String?,
): BaseModel()