package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.cuongngo.core_project.response.BaseModel
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Entity(
    tableName = "forms",
    indices = [Index(value = ["form_code"], unique = true)],
)
@TypeConverters(Converters::class)
data class FormEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "form_id") val formID: Long? = null,
    @ColumnInfo(name = "form_code") var formCode: String,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "type") var type: String? = null,
    @ColumnInfo(name = "status") var status: Int? = null,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "schema_name") var schemaName: String?,
    @ColumnInfo(name = "schema_code") var schemaCode: String?,
    @ColumnInfo(name = "schema_type") var schemaType: String? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_header_schema") var formHeaderSchema: List<Field>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_body_schema") var formBodySchema: List<Field>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "process_step")
    var processStep: List<ProcessStep>? = null,
    @ColumnInfo(name = "sheet_number") var sheetNumber: Int? = null,
    @ColumnInfo(name = "sheet_name") var sheetName: String? = null,
    @ColumnInfo(name = "created_at") var created: String? = null,
    @ColumnInfo(name = "updated_at") var updated: String? = null,
    @ColumnInfo(name = "deleted_at") var deleted: String? = null
//    @TypeConverters(FieldListTypeConverter::class) val fields: List<Field>,
//    @TypeConverters(SubmitButtonTypeConverter::class) val submitButton: SubmitButton
) : BaseModel()

class Converters {
    @TypeConverter
    fun fromFiledList(value: List<Field>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Field>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toFiledList(value: String?): List<Field>? {
        val gson = Gson()
        val type = object : TypeToken<List<Field>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter
    fun fromProcessList(value: List<ProcessStep>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<ProcessStep>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toProcessList(value: String?): List<ProcessStep>? {
        val gson = Gson()
        val type = object : TypeToken<List<ProcessStep>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromUserEntity(user: UserTHPEntity?): String? {
        val gson = Gson()
        return gson.toJson(user)
    }

    @TypeConverter
    fun toUserEntity(userString: String?): UserTHPEntity? {
        val gson = Gson()
        return gson.fromJson(userString, UserTHPEntity::class.java)
    }

}

data class Field(
    val id: Long?,
    val label: String?,
    val value: String?,
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
    val name: String?,
    val status: Int? = null,
    var owner: UserTHPEntity? = null,
    var duration: String? = null,
    val created: String? = null,
    val updated: String? = null,
    val deleted: String? = null,
): BaseModel()


////

//random test data

fun randomString(length: Int): String {
    val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    return (1..length).map { chars.random() }.joinToString("")
}

fun randomBoolean(): Boolean {
    return Random.nextBoolean()
}

fun randomDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

fun randomOptions(size: Int): List<Option> {
    return List(size) {
        Option(
            value = randomString(5), label = randomString(5), id = Random.nextLong(1, 1000)
        )
    }
}

fun generateRandomField(): Field {
    return Field(
        id = Random.nextLong(1, 1000),
        label = randomString(10),
        value = randomString(26),
        placeholder = randomString(15),
        type = listOf("text", "select", "checkbox", "radio", "date").random(),
        required = randomBoolean(),
        options = if (randomBoolean()) randomOptions(Random.nextInt(1, 5)) else null,
        checked = randomBoolean(),
        created = randomDate(),
        updated = randomDate(),
        deleted = if (randomBoolean()) randomDate() else null,
    )
}

fun generateRandomProcessStep(): ProcessStep {
    return ProcessStep(
        id = Random.nextLong(1, 1000),
        name = listOf("TP", "MA", "Leader", "An Ninh", "Head off").random(),
        duration = listOf("120 phút", "1 ngày", "3 ngày", "4 tiếng", "Flexible").random(),
    )
}

fun generateRandomFieldList(size: Int): List<Field> {
    return List(size) { generateRandomField() }
}

