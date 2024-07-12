package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.cuongngo.core_project.response.thp_form.Field
import com.cuongngo.core_project.response.thp_form.SubmitButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "forms")
data class FormEntity (
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "title")
    val title: String? = null,
    @ColumnInfo(name = "status")
    val status: Int? = null,
    @ColumnInfo(name = "created")
    val created: String? = null,
    @ColumnInfo(name = "updated")
    val updated: String? = null,
//    @TypeConverters(FieldListTypeConverter::class) val fields: List<Field>,
//    @TypeConverters(SubmitButtonTypeConverter::class) val submitButton: SubmitButton
): Serializable


data class upsertForm(
    val title: String,
    val status: Int,
    val created: String,
    val updated: String,
): Serializable

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
