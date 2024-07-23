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

    @Query("SELECT * FROM users")
    fun getAllUser(): List<UserTHPEntity>

    @Update
    fun updateUser(genre: UserTHPEntity)

    @Delete
    fun deleteUser(genre: UserTHPEntity)
}