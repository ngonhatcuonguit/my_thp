package com.cuongngo.core_project.roomdb.Dao

import androidx.room.*
import com.cuongngo.core_project.roomdb.entity.GenreEntity

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGenre(genre: GenreEntity)

    @Query("SELECT * FROM genre")
    fun getGenres(): List<GenreEntity>

    @Update
    fun updateGenre(genre: GenreEntity)

    @Delete
    fun deleteGenre(genre: GenreEntity)
}