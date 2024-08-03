package com.cuongngo.core_project.data.database.roomdb.DaoInterFace

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Sheet

@Dao
interface RequestDao {

    @Update
    fun updateRequestValue(record: RequestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertRequest(requestEntity: RequestEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertRequest(requestEntity: RequestEntity): Long

    @Query("SELECT * FROM request_value")
    fun getAllRequest(): List<RequestEntity>

    @Query("SELECT * FROM request_value WHERE request_name = :requestName")
    fun getRequestByName(requestName: String): RequestEntity

    @Query("UPDATE request_value SET list_sheet = :listSheet, updated_at = :currentTimestamp WHERE request_id = :requestId")
    fun updateListSheet(requestId: Long, listSheet: List<Sheet>?, currentTimestamp: String)

    @Delete
    fun deleteRequest(record: RequestEntity)

    @Query("SELECT * FROM request_value WHERE request_code = :requestCode")
    fun getListRequestByCode(requestCode: String): RequestEntity
    @Query("SELECT * FROM request_value WHERE request_id = :requestID")
    fun getRequestByID(requestID: Long): RequestEntity

}