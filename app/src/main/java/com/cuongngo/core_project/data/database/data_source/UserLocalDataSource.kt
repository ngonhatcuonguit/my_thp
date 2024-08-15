package com.cuongngo.core_project.data.database.data_source

import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity

class UserLocalDataSource(private val database: AppDatabase) : BaseLocalDataSource() {
    suspend fun getUserCount() = getResult {
        database.userDao().getUserCount()
    }

    suspend fun getAllUser() = getResult {
        database.userDao().getAllUser(20)
    }

    suspend fun getUserById(id: Long) = getResult {
        database.userDao().getUserById(id)
    }

    suspend fun upsertListUser(listUser: List<UserTHPEntity>) = getResult {
        database.userDao().insertUsers(listUser)
    }

    suspend fun insertUser(record: UserTHPEntity) = getResult {
        database.userDao().addUser(record)
    }
}