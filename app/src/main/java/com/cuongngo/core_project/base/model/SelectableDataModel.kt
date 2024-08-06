package com.cuongngo.core_project.base.model

data class SelectableDataModel<T>(
    val data: T,
    var isSelected: Boolean = false
)