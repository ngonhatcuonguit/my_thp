package com.cuongngo.my_thp.data.database.roomdb.DaoInterFace

import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(record: UserTHPEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserTHPEntity>)

    @Query("SELECT * FROM users WHERE first_name LIKE '%' || :keyword || '%' COLLATE NOCASE OR last_name LIKE '%' || :keyword || '%' COLLATE NOCASE OR initial LIKE '%' || :keyword || '%' COLLATE NOCASE OR email LIKE '%' || :keyword || '%' COLLATE NOCASE OR position_name LIKE '%' || :keyword || '%' COLLATE NOCASE")
    suspend fun searchUsers(keyword: String): List<UserTHPEntity>

    @Query("SELECT * FROM users LIMIT :limit")
    fun getAllUser(limit: Int): List<UserTHPEntity>

    @Query("SELECT * FROM users WHERE personal_number = :id")
    fun getUserById(id: Long): UserTHPEntity

    @Update
    fun updateUser(genre: UserTHPEntity)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Int

    @Query("DELETE FROM users")
    suspend fun formatUserTable()
    @Delete
    fun deleteUser(genre: UserTHPEntity)
}