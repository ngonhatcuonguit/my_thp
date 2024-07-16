package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "form_schema", indices = [Index(value = ["form_code"], unique = true)])
@TypeConverters(Converters::class)
data class FormSchemaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "schema_name")
    var schemaName: String?,
    @ColumnInfo(name = "form_code")
    var formCode: String,
    @ColumnInfo(name = "code")
    var code: String? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_schema")
    var formSchema: List<Filed>? = null,
    @ColumnInfo(name = "created_at")
    var created: String? = null,
    @ColumnInfo(name = "updated_at")
    var updated: String? = null,
    @ColumnInfo(name = "deleted_at")
    var deleted: String? = null
)

class Converters {
    @TypeConverter
    fun fromFiledList(value: List<Filed>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Filed>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toFiledList(value: String?): List<Filed>? {
        val gson = Gson()
        val type = object : TypeToken<List<Filed>>() {}.type
        return gson.fromJson(value, type)
    }
}


data class Filed(
    val id: Long,
    val label: String,
    @SerializedName("placeholder") val placeholder: String?,
    val type: String?,
    val required: Boolean?,
    val options: List<Option>?,
    val checked: Boolean?,
    val created: String?,
    val updated: String?,
    val deleted: String?,
) {
    companion object {
        const val STATUS_ACTIVE = 1
        const val STATUS_INACTIVE = 0
    }
}

data class Option(
    val id: Long?,
    val value: String?,
    val label: String?,
)
