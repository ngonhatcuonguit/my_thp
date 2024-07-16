package com.cuongngo.core_project.services.repository

import com.cuongngo.core_project.data.database.data_source.FormLocalDataSource
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.services.network.BaseResult

class FormRepository(
    private val formLocalDataSource: FormLocalDataSource
) {

    companion object {
        val TAG = FormRepository::class.simpleName
    }

    suspend fun getAllForm(): BaseResult<List<FormEntity>> {
        return formLocalDataSource.getAllForm()
    }

    suspend fun updateForm(formEntity: FormEntity): BaseResult<Int> {
        return formLocalDataSource.updateForm(formEntity)
    }

    suspend fun getFormById(id: String): BaseResult<FormEntity> {
        return formLocalDataSource.getFormById(id = id.toLong())
    }

    suspend fun getFormByTitle(title: String): BaseResult<FormEntity> {
        return formLocalDataSource.getFormByTitle(title = title)
    }

    suspend fun insertForm(formEntity: FormEntity): BaseResult<Long> {
        return formLocalDataSource.insertForm(formEntity)
    }
    suspend fun insertRequest(formValueEntity: RequestEntity): BaseResult<Long> {
        return formLocalDataSource.insertRequest(formValueEntity)
    }
    suspend fun upsertForm(formEntity: FormEntity): BaseResult<Long> {
        return formLocalDataSource.upsertForm(formEntity)
    }
    suspend fun deleteForm(formEntity: FormEntity): BaseResult<Int> {
        return formLocalDataSource.deleteForm(formEntity)
    }

}