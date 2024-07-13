package com.cuongngo.core_project.data.database.roomdb.DaoInterFace

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity

@Dao
interface FormDao {

    @Update
    fun updateForm(record: FormEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertForm(formEntity: FormEntity): Long
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertForm(formEntity: FormEntity): Long

    @Query("SELECT * FROM forms")
    fun getAllForm(): List<FormEntity>

    @Query("SELECT * FROM forms WHERE id = :id")
    fun getFormById(id: Long): FormEntity

    @Query("SELECT * FROM forms WHERE title = :title")
    fun getFormByTitle(title: String): FormEntity
    @Query("SELECT * FROM forms WHERE title = :title")
    fun searchFormByKeyWord(title: String): FormEntity

    @Delete
    fun deleteForm(record: FormEntity): Int

}