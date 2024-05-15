package com.cuongngo.core_project.data.database.roomdb.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "record_process")
data class RecordProcessEntity (
    @PrimaryKey(autoGenerate = true) var id : Long? = null,
    @ColumnInfo(name = "name")
    val name: String? = null,
    @ColumnInfo(name = "value")
    val value: Double? = null,
    @ColumnInfo(name = "note")
    val note: String? = null
): Serializable