package com.cuongngo.my_thp.ui.acb_app

import com.cuongngo.my_thp.data.database.roomdb.entity.GdEntity
import com.cuongngo.my_thp.services.network.BaseResult

class GdRepository(
    private val gdRemoteDataSource: GdRemoteDataSource
) {
    suspend fun getAllGD(): BaseResult<List<GdEntity>> {
        return gdRemoteDataSource.getAllGd()
    }

    suspend fun upsertGD(gdEntity: GdEntity): BaseResult<Long> {
        return gdRemoteDataSource.upsertGD(gdEntity)
    }

    suspend fun deleteGD(gdEntity: GdEntity): BaseResult<Int> {
        return gdRemoteDataSource.deleteGD(gdEntity)
    }

    suspend fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): BaseResult<List<GdEntity>> {
        return gdRemoteDataSource.getTransactionsByDateRange(startDate, endDate)
    }

    suspend fun getLastNDaysTransactions(
        dateAgo: Long
    ): BaseResult<List<GdEntity>> {
        return gdRemoteDataSource.getLastNDaysTransactions(dateAgo)
    }


}