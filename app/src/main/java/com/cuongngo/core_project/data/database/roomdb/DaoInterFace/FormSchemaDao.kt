package com.cuongngo.core_project.data.database.roomdb.DaoInterFace

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cuongngo.core_project.data.database.roomdb.entity.FormSchemaEntity

@Dao
interface FormSchemaDao {

    @Update
    fun updateFormSchema(record: FormSchemaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertFormSchema(formSchemaEntity: FormSchemaEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertFormSchema(formSchemaEntity: FormSchemaEntity): Long

    @Query("SELECT * FROM form_schema")
    fun getAllFormSchema(): List<FormSchemaEntity>

    @Query("SELECT * FROM form_schema WHERE form_code = :formCode")
    fun getSchemaByFormCode(formCode: String): FormSchemaEntity

    @Query("SELECT * FROM form_schema WHERE schema_name = :formName")
    fun getFormSchemaByTitle(formName: String): FormSchemaEntity

    @Query("SELECT * FROM form_schema WHERE code = :formCode")
    fun searchFormSchemaByKeyWord(formCode: String): FormSchemaEntity

    @Delete
    fun deleteFormSchema(record: FormSchemaEntity)

}