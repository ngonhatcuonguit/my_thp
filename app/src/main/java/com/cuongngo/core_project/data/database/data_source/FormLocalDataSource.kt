package com.cuongngo.core_project.data.database.data_source
import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity

class FormLocalDataSource(private val database: AppDatabase) : BaseLocalDataSource() {

    suspend fun getAllForm() = getResult {
        database.thpFormDao().getAllForm()
    }
     suspend fun upsertListForm(listForm: List<FormEntity>) = getResult {
        database.thpFormDao().upsertListForm(listForm)
    }

    suspend fun getFormById(id: Long) = getResult {
        database.thpFormDao().getFormById(id)
    }
    suspend fun getFormByTitle(title: String) = getResult {
        database.thpFormDao().getFormByTitle(title)
    }
    suspend fun getFormByCode(code: String) = getResult {
        database.thpFormDao().getFormByCode(code)
    }
    suspend fun upsertForm(record: FormEntity) = getResult {
        database.thpFormDao().upsertForm(record)
    }
    suspend fun insertForm(record: FormEntity) = getResult {
        database.thpFormDao().insertForm(record)
    }
    suspend fun updateForm(record: FormEntity) = getResult {
        database.thpFormDao().updateForm(record)
    }

    suspend fun getFormCount() = getResult {
        database.thpFormDao().getFormCount()
    }

    suspend fun deleteForm(record: FormEntity) = getResult {
        database.thpFormDao().deleteForm(record)
    }
    suspend fun formatFormTable() = getResult {
        database.thpFormDao().formatFormTable()
    }

    suspend fun searchForms(keyword: String) = getResult {
        database.thpFormDao().searchForms(keyword)
    }

    //request

    suspend fun insertRequest(request: RequestEntity) = getResult {
        database.thpFormDao().insertRequest(request)
    }
    suspend fun upsertRequest(request: RequestEntity) = getResult {
        database.thpFormDao().upsertRequest(request)
    }
    suspend fun getRequestByID(requestID: Long) = getResult {
        database.thpFormDao().getRequestByID(requestID)
    }
    suspend fun getRequestByCode(requestCode: String) = getResult {
        database.thpFormDao().getListRequestByCode(requestCode)
    }
    suspend fun getAllRequest() = getResult {
        database.thpFormDao().getAllRequest()
    }

}