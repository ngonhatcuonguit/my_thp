package com.cuongngo.core_project.data.database.roomdb.DaoInterFace

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Body

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

    @Query("SELECT * FROM request_value WHERE is_sync = 0")
    fun getRequestNeedUpload(): List<RequestEntity>

    @Query("UPDATE request_value SET list_body = :listBody, updated_at = :currentTimestamp WHERE request_id = :requestId")
    fun updateListSheet(requestId: Long, listBody: List<Body>?, currentTimestamp: String)
    @Query("UPDATE request_value SET is_sync = :is_sync, updated_at = :currentTimestamp WHERE request_code = :requestCode")
        fun updateSyncStatus(requestCode: String, is_sync: Boolean?, currentTimestamp: String)

    @Delete
    fun deleteRequest(record: RequestEntity)

    @Query("SELECT * FROM request_value WHERE request_code = :requestCode")
    fun getListRequestByCode(requestCode: String): RequestEntity
    @Query("SELECT * FROM request_value WHERE request_id = :requestID")
    fun getRequestByID(requestID: Long): RequestEntity

}