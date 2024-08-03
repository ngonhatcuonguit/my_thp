package com.cuongngo.core_project.data.database.roomdb.DaoInterFace

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity

@Dao
interface FormDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertForm(formEntity: FormEntity): Long

    @Update
    fun updateForm(record: FormEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertForm(formEntity: FormEntity): Long

    @Query("SELECT * FROM forms")
    fun getAllForm(): List<FormEntity>

    @Query("SELECT * FROM forms WHERE form_id = :id")
    fun getFormById(id: Long): FormEntity

    @Query("SELECT * FROM forms WHERE name = :title")
    fun getFormByTitle(title: String): FormEntity
    @Query("SELECT * FROM forms WHERE form_code = :code")
    fun getFormByCode(code: String): FormEntity
    @Query("SELECT * FROM forms WHERE name = :title")
    fun searchFormByKeyWord(title: String): FormEntity

    @Delete
    fun deleteForm(record: FormEntity): Int


    //interface for RequestEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertRequest(request: RequestEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertRequest(request: RequestEntity): Long
    @Query("SELECT * FROM request_value WHERE request_code = :requestCode")
    fun getListRequestByCode(requestCode: String): RequestEntity
    @Query("SELECT * FROM request_value WHERE request_id = :requestID")
    fun getRequestByID(requestID: Long): RequestEntity
    @Query("SELECT * FROM request_value")
    fun getAllRequest(): List<RequestEntity>

}