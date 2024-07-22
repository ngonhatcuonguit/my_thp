package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(
    tableName = "request_value",
    foreignKeys = [ForeignKey(
        entity = FormEntity::class,
        parentColumns = arrayOf("form_code"),
        childColumns = arrayOf("form_code"),
//        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["request_code"], unique = true)],
)
@TypeConverters(Converters::class)
data class RequestEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "request_id") val requestID: Long,
    @ColumnInfo(name = "request_name") var requestName: String? = null,
    @ColumnInfo(name = "request_description:") var requestDescription: String? = null,
    @ColumnInfo(name = "created_by") var createdBy: String? = null,//chưa define user
    @ColumnInfo(name = "informer") var informer: String? = null,//chưa define user
    @ColumnInfo(name = "form_code") var formCode: String,
    @ColumnInfo(name = "request_code") var requestCode: String,
    @ColumnInfo(name = "request_status") var requestStatus: Int? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_value") var formValue: List<Field>? = null,
    @ColumnInfo(name = "created_at") var created: String? = null,
    @ColumnInfo(name = "updated_at") var updated: String? = null,
    @ColumnInfo(name = "deleted_at") var deleted: String? = null
) : Serializable