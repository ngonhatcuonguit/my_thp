package com.cuongngo.core_project.ui.acb_app

import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.data_source.BaseLocalDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity

class GdRemoteDataSource(private val database: AppDatabase) : BaseLocalDataSource() {

    suspend fun getAllGd() = getResult {
        database.gdDao().getAllGd()
    }

    suspend fun upsertGD(record: GdEntity) = getResult {
        database.gdDao().upsertGd(record)
    }


}