package com.cuongngo.my_thp.response.news

import androidx.annotation.DrawableRes
import com.cuongngo.my_thp.response.BaseModel

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
    @DrawableRes
    var drawableId: Int,
    var releaseDate: String?,
): BaseModel()