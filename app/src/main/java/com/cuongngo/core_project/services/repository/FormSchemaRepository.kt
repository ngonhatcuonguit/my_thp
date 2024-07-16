package com.cuongngo.core_project.services.repository

import com.cuongngo.core_project.data.database.data_source.FormSchemaLocalDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.FormSchemaEntity

class FormSchemaRepository(
    private val formSchemaLocalDataSource: FormSchemaLocalDataSource
) {
    companion object {
        val TAG = FormSchemaRepository::class.simpleName
    }

    suspend fun getAllFormSchema() = formSchemaLocalDataSource.getAllFormSchema()
    suspend fun getFormSchemaByCode(code: String) =
        formSchemaLocalDataSource.getFormSchemaByCode(code)

    suspend fun getFormSchemaByTitle(formCode: String) =
        formSchemaLocalDataSource.getFormSchemaByTitle(formCode)

    suspend fun insertFormSchema(record: FormSchemaEntity) =
        formSchemaLocalDataSource.insertFormSchema(record)

    suspend fun upsertFormSchema(record: FormSchemaEntity) =
        formSchemaLocalDataSource.upsertFormSchema(record)

    suspend fun updateFormSchema(record: FormSchemaEntity) =
        formSchemaLocalDataSource.updateFormSchema(record)

    suspend fun deleteFormSchema(record: FormSchemaEntity) =
        formSchemaLocalDataSource.deleteFormSchema(record)

    suspend fun searchFormSchemaByKeyWord(formCode: String) =
        formSchemaLocalDataSource.searchFormSchemaByKeyWord(formCode)

}