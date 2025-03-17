package com.cuongngo.my_thp.data.database.data_source

import com.cuongngo.my_thp.data.database.AppDatabase
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity

class UserLocalDataSource(private val database: AppDatabase) : BaseLocalDataSource() {
    suspend fun getUserCount() = getResult {
        database.userDao().getUserCount()
    }

    suspend fun formatUserTable() = getResult {
        database.userDao().formatUserTable()
    }

    suspend fun getAllUser() = getResult {
        database.userDao().getAllUser(50)
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
    suspend fun searchUsers(keyword: String) = getResult {
        database.userDao().searchUsers(keyword)
    }
}