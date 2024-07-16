package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.cuongngo.core_project.response.thp_form.Field
import com.cuongngo.core_project.response.thp_form.SubmitButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(
    tableName = "forms",
    foreignKeys = [ForeignKey(
        entity = FormSchemaEntity::class,
        parentColumns = arrayOf("form_code"),
        childColumns = arrayOf("code"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class FormEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "code")
    var code: String,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "type")
    var type: String? = null,
    @ColumnInfo(name = "status")
    var status: Int? = null,
    @ColumnInfo(name = "created")
    var created: String? = null,
    @ColumnInfo(name = "updated")
    var updated: String? = null,
//    @TypeConverters(FieldListTypeConverter::class) val fields: List<Field>,
//    @TypeConverters(SubmitButtonTypeConverter::class) val submitButton: SubmitButton
) : Serializable


data class upsertForm(
    val title: String,
    val status: Int,
    val created: String,
    val updated: String,
) : Serializable

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
