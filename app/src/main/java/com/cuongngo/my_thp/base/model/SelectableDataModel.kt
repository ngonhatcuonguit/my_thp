package com.cuongngo.my_thp.base.model

data class SelectableDataModel<T>(
    val data: T,
    var isSelected: Boolean = false
)