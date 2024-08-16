package com.cuongngo.core_project.services.repository

import com.cuongngo.core_project.data.database.data_source.FormLocalDataSource
import com.cuongngo.core_project.data.database.data_source.FormRemoteDatSource
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.FormResponse
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.services.network.BaseResult

class FormRepository(
    private val formLocalDataSource: FormLocalDataSource,
    private val formRemoteDatSource: FormRemoteDatSource
) {

    //Local

    companion object {
        val TAG = FormRepository::class.simpleName
    }

    suspend fun getAllForm(): BaseResult<List<FormEntity>> {
        return formLocalDataSource.getAllForm()
    }
    suspend fun upsertListForm(listForm: List<FormEntity>): BaseResult<Unit> {
        return formLocalDataSource.upsertListForm(listForm)
    }

    suspend fun updateForm(formEntity: FormEntity): BaseResult<Unit> {
        return formLocalDataSource.updateForm(formEntity)
    }

    suspend fun getFormById(id: String): BaseResult<FormEntity> {
        return formLocalDataSource.getFormById(id = id.toLong())
    }

    suspend fun getFormByTitle(title: String): BaseResult<FormEntity> {
        return formLocalDataSource.getFormByTitle(title = title)
    }
    suspend fun getFormByCode(code: String): BaseResult<FormEntity> {
        return formLocalDataSource.getFormByCode(code = code)
    }

    suspend fun insertForm(formEntity: FormEntity): BaseResult<Long> {
        return formLocalDataSource.insertForm(formEntity)
    }
    suspend fun upsertForm(formEntity: FormEntity): BaseResult<Long> {
        return formLocalDataSource.upsertForm(formEntity)
    }

    suspend fun getFormCountLocal():BaseResult<Int>{
        return formLocalDataSource.getFormCount()
    }

    suspend fun formatFormTable():BaseResult<Unit>{
        return formLocalDataSource.formatFormTable()
    }

    suspend fun searchForms(keyword: String):BaseResult<List<FormEntity>>{
        return formLocalDataSource.searchForms(keyword)
    }

    suspend fun deleteForm(formEntity: FormEntity): BaseResult<Int> {
        return formLocalDataSource.deleteForm(formEntity)
    }

    //request

    suspend fun insertRequest(request: RequestEntity): BaseResult<Long> {
        return formLocalDataSource.insertRequest(request)
    }

    suspend fun upsertRequest(request: RequestEntity): BaseResult<Long> {
        return formLocalDataSource.upsertRequest(request)
    }

    suspend fun getRequestByID(requestID: Long): BaseResult<RequestEntity> {
        return formLocalDataSource.getRequestByID(requestID = requestID)
    }
    suspend fun getRequestByCode(requestCode: String): BaseResult<RequestEntity> {
        return formLocalDataSource.getRequestByCode(requestCode)
    }
    suspend fun getAllRequest(): BaseResult<List<RequestEntity>> {
        return formLocalDataSource.getAllRequest()
    }

    //-----------remote-------------

    suspend fun getListForm(isGetAll: Boolean): BaseResult<FormResponse>{
        return formRemoteDatSource.getListForm(isGetAll)
    }

}