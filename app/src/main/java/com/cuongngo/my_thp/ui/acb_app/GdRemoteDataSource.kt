package com.cuongngo.my_thp.ui.acb_app

import com.cuongngo.my_thp.data.database.AppDatabase
import com.cuongngo.my_thp.data.database.data_source.BaseLocalDataSource
import com.cuongngo.my_thp.data.database.roomdb.entity.GdEntity

class GdRemoteDataSource(private val database: AppDatabase) : BaseLocalDataSource() {

    suspend fun getAllGd() = getResult {
        database.gdDao().getAllGd()
    }

    suspend fun upsertGD(record: GdEntity) = getResult {
        database.gdDao().upsertGd(record)
    }

    suspend fun deleteGD(record: GdEntity) = getResult {
        database.gdDao().deleteGd(record)
    }

    suspend fun getTransactionsByDateRange(startDate: Long, endDate: Long) = getResult {
        database.gdDao().getTransactionsByDateRange(startDate = startDate, endDate = endDate)
    }
    suspend fun getLastNDaysTransactions(dateAgo: Long) = getResult {
        database.gdDao().getLastNDaysTransactions(dateAgo)
    }


}