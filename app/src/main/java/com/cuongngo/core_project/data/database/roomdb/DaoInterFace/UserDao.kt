package com.cuongngo.core_project.data.database.roomdb.DaoInterFace

import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(genre: UserTHPEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserTHPEntity>)

    @Query("SELECT * FROM users WHERE name LIKE :keyword OR email LIKE :keyword OR department LIKE :keyword OR position LIKE :keyword")
    suspend fun searchUsers(keyword: String): List<UserTHPEntity>

    @Query("SELECT * FROM users")
    fun getAllUser(): List<UserTHPEntity>

    @Update
    fun updateUser(genre: UserTHPEntity)

    @Delete
    fun deleteUser(genre: UserTHPEntity)
}