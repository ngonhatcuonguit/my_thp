package com.cuongngo.core_project.data.database.roomdb.DaoInterFace
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cuongngo.core_project.data.database.roomdb.entity.RecordProcessEntity

@Dao
interface RecordProcessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecordProcess(record: RecordProcessEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(record: RecordProcessEntity) : Long

    @Query("SELECT * FROM record_process")
    fun getAllRecordProcess(): List<RecordProcessEntity>

    @Query("SELECT * FROM record_process WHERE name = :name")
    fun getRecordProcessByName(name : String) : RecordProcessEntity

//    @Query("SELECT * FROM record_process WHERE name = :name AND id = :id")
//    fun getRecordProcessByName(name : String, id : Long) : RecordProcessEntity

    @Update
    fun updateRecordProcess(record: RecordProcessEntity)

    @Delete
    fun deleteRecordProcess(record: RecordProcessEntity)
}