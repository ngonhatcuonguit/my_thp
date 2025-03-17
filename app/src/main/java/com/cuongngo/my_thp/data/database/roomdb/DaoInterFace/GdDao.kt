package com.cuongngo.my_thp.data.database.roomdb.DaoInterFace

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cuongngo.my_thp.data.database.roomdb.entity.GdEntity

@Dao
interface GdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertGd(gdEntity: GdEntity): Long

    @Query("SELECT * FROM giao_dich")
    fun getAllGd(): List<GdEntity>


    @Query("SELECT COUNT(*) FROM giao_dich")
    fun getGdCount(): Int

    @Delete
    fun deleteGd(record: GdEntity): Int

    @Query("DELETE FROM giao_dich")
    suspend fun formatFormTable()

    // Lấy giao dịch theo khoảng thời gian
    @Query("SELECT * FROM giao_dich WHERE transaction_date BETWEEN :startDate AND :endDate")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): List<GdEntity>

    // Lấy giao dịch trong n ngày gần nhất
    @Query("SELECT * FROM giao_dich WHERE transaction_date >= :daysAgo")
    fun getLastNDaysTransactions(daysAgo: Long): List<GdEntity>

}