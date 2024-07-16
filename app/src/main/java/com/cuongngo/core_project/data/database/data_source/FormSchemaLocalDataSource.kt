package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.entity.FormSchemaEntity

class FormSchemaLocalDataSource(private val database: AppDatabase) : BaseLocalDataSource(){

    suspend fun getAllFormSchema() = getResult {
        database.formSchemaDao().getAllFormSchema()
    }
    suspend fun getFormSchemaByCode(code: String) = getResult {
        database.formSchemaDao().getSchemaByFormCode(code)
    }
    suspend fun getFormSchemaByTitle(name: String) = getResult {
        database.formSchemaDao().getFormSchemaByTitle(name)
    }
    suspend fun searchFormSchemaByKeyWord(formCode: String) = getResult {
        database.formSchemaDao().searchFormSchemaByKeyWord(formCode)
    }
    suspend fun upsertFormSchema(record: FormSchemaEntity) = getResult {
        database.formSchemaDao().upsertFormSchema(record)
    }
    suspend fun insertFormSchema(record: FormSchemaEntity) = getResult {
        database.formSchemaDao().insertFormSchema(record)
    }
    suspend fun updateFormSchema(record: FormSchemaEntity) = getResult {
        database.formSchemaDao().updateFormSchema(record)
    }
    suspend fun deleteFormSchema(record: FormSchemaEntity) = getResult {
        database.formSchemaDao().deleteFormSchema(record)
    }

}