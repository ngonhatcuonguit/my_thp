package com.cuongngo.core_project.data.database.data_source
import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity

class FormLocalDataSource(private val database: AppDatabase) : BaseLocalDataSource() {

    suspend fun getAllForm() = getResult {
        database.thpFormDao().getAllForm()
    }
    suspend fun getFormById(id: Long) = getResult {
        database.thpFormDao().getFormById(id)
    }
    suspend fun getFormByTitle(title: String) = getResult {
        database.thpFormDao().getFormByTitle(title)
    }
    suspend fun upsertForm(record: FormEntity) = getResult {
        database.thpFormDao().upsertForm(record)
    }
    suspend fun updateForm(record: FormEntity) = getResult {
        database.thpFormDao().updateForm(record)
    }
    suspend fun deleteForm(record: FormEntity) = getResult {
        database.thpFormDao().deleteForm(record)
    }


}