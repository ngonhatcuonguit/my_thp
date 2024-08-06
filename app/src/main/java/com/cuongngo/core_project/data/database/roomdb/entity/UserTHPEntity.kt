package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cuongngo.core_project.response.BaseModel

@Entity(
    tableName = "users",
    indices = [Index(value = ["msnv"], unique = true)],
)
data class UserTHPEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "status") val status: Int? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "phone") var phone: String? = null,
    @ColumnInfo(name = "msnv") var msnv: Long? = null,
    @ColumnInfo(name = "device_id") var deviceID: Long? = null,
    @ColumnInfo(name = "department") var department: String? = null,
    @ColumnInfo(name = "position") var position: String? = null,
) : BaseModel()