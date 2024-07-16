package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.cuongngo.core_project.response.thp_form.Field
import com.cuongngo.core_project.response.thp_form.SubmitButton
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(
    tableName = "forms",
    indices = [Index(value = ["form_code"], unique = true)],
)
@TypeConverters(Converters::class)
data class FormEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "form_code")
    var formCode: String,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "type")
    var type: String? = null,
    @ColumnInfo(name = "status")
    var status: Int? = null,
    @ColumnInfo(name = "schema_name")
    var schemaName: String?,
    @ColumnInfo(name = "schema_code")
    var schemaCode: String?,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_schema")
    var formSchema: List<Filed>? = null,
//    @ColumnInfo(name = "process_step")
//    var processStep: List<ProcessStep>? = null,
    @ColumnInfo(name = "created_at")
    var created: String? = null,
    @ColumnInfo(name = "updated_at")
    var updated: String? = null,
    @ColumnInfo(name = "deleted_at")
    var deleted: String? = null
//    @TypeConverters(FieldListTypeConverter::class) val fields: List<Field>,
//    @TypeConverters(SubmitButtonTypeConverter::class) val submitButton: SubmitButton
) : Serializable{
    companion object {
        const val STATUS_ACTIVE = 1
        const val STATUS_INACTIVE = 0
    }
}

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

data class ProcessStep(
    val id: Long,
    val name: String,
    val status: Int,
    val created: String,
    val updated: String,
    val deleted: String,
)


////
data class UserTHP(
    val id: Int?,
    val status: Int?,
    var name: String?,
    var email: String?,
    var phone: String?,
    var msnv: Long?,
    var position: String?,
    var avatar: String?,
) : Serializable {
    constructor() : this(
        0, 0, "", "", "", 0, "", ""
    )
}

class FieldListTypeConverter {
    @TypeConverter
    fun fromFieldList(fields: List<Field>?): String? {
        return Gson().toJson(fields)
    }

    @TypeConverter
    fun toFieldList(data: String?): List<Field>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Field>>() {}.type
        return Gson().fromJson(data, listType)
    }
}

class SubmitButtonTypeConverter {
    @TypeConverter
    fun fromSubmitButton(submitButton: SubmitButton?): String? {
        return Gson().toJson(submitButton)
    }

    @TypeConverter
    fun toSubmitButton(data: String?): SubmitButton? {
        return Gson().fromJson(data, SubmitButton::class.java)
    }
}
