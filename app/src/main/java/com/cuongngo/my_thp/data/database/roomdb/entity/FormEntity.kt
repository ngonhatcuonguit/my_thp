package com.cuongngo.my_thp.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cuongngo.my_thp.response.BaseModel
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
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "form_id") var form_id: Long? = null,
    @ColumnInfo(name = "form_code") var form_code: String,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "category") var category: String? = null,
    @ColumnInfo(name = "process_id") var process_id: Int? = null,
    @ColumnInfo(name = "status") var status: String? = null,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "schema_name") var schema_name: String?=null,
    @ColumnInfo(name = "schema_code") var schema_code: String?=null,
    @ColumnInfo(name = "schema_type") var schema_type: String? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "list_header") var list_header: List<Field>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "list_body") var list_body: List<Body>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "process_steps") var process_steps: List<ProcessStep>? = null,
    @ColumnInfo(name = "form_bottom_sign") var form_bottom_sign: List<Field>? = null,
    @ColumnInfo(name = "form_bottom_note") var form_bottom_note: String? = null,
    @ColumnInfo(name = "created_at") var created_at: String? = null,
    @ColumnInfo(name = "updated_at") var updated_at: String? = null,
    @ColumnInfo(name = "deleted_at") var deleted_at: String? = null,
    @ColumnInfo(name = "form_version") var form_version: String? = null
) : BaseModel()

data class FormResponse(
    var status: String?,
    var data: List<FormEntity>?
) : BaseModel()

data class Body(
    val id: Long?,
    var name: String?,
    var form_code: String?,
    var request_code: String?,
    var form_name: String?,
    var list_header: List<Field>? = null,
    var list_field: List<Field>?,
    var is_done: Boolean?,
    var type: String?,
    var created: String?,
    var updated: String?,
    var deleted: String?,
): BaseModel()

data class Field(
    val id: Long?,
    var label: String?,
    var value: String?,
    val placeholder: String?,
    val type: String?,
    val required: Boolean?,
    var options: List<Option>?,
    var checked: Boolean?,
    var created: String?,
    var updated: String?,
    var deleted: String?,
):  BaseModel() {
    fun copyWithEmptyFieldsAsNull(): Field {
        return this.copy(
            label = if (label.isNullOrBlank()) null else label,
            value = if (value.isNullOrBlank()) "N/A" else value,
            placeholder = if (placeholder.isNullOrBlank()) null else placeholder,
            type = if (type.isNullOrBlank()) null else type,
            options = if (options.isNullOrEmpty()) null else options,
            created = if (created.isNullOrBlank()) null else created,
            updated = if (updated.isNullOrBlank()) null else updated,
            deleted = if (deleted.isNullOrBlank()) null else deleted
        )
    }
}

data class Option(
    val id: Long?,
    val value: String? = null,
    val label: String? = null,
    val type: String? = null
): BaseModel()

data class ProcessStep(
    val id: Long,
    val name: String?,
    val status: String? = null,
    var owner: List<UserTHPEntity>? = mutableListOf(),
    var duration: String? = null,
    val created: String? = null,
    val updated: String? = null,
    val deleted: String? = null,
) : BaseModel()

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
            value =  listOf(
                "Họ và tên", "Ngày sinh", "Giới tính", "Nơi ở", "Vị trí công việc",
                "Số điện thoại", "Email", "Quốc tịch", "Số CMND/CCCD", "Ngày cấp",
                "Nơi cấp", "Tình trạng hôn nhân", "Trình độ học vấn", "Chuyên ngành",
                "Ngôn ngữ", "Kinh nghiệm làm việc", "Kỹ năng", "Sở thích", "Giới thiệu bản thân",
                "Mục tiêu nghề nghiệp"
            ).random(),
            label = listOf(
                "Họ và tên", "Ngày sinh", "Giới tính", "Nơi ở", "Vị trí công việc",
                "Số điện thoại", "Email", "Quốc tịch", "Số CMND/CCCD", "Ngày cấp",
                "Nơi cấp", "Tình trạng hôn nhân", "Trình độ học vấn", "Chuyên ngành",
                "Ngôn ngữ", "Kinh nghiệm làm việc", "Kỹ năng", "Sở thích", "Giới thiệu bản thân",
                "Mục tiêu nghề nghiệp"
            ).random(),
            id = Random.nextLong(1, 1000)
        )
    }
}

fun generateRandomField(): Field {
    return Field(
        id = Random.nextLong(1, 1000),
        label = listOf(
            "Họ và tên", "Ngày sinh", "Giới tính", "Nơi ở", "Vị trí công việc",
            "Số điện thoại", "Email", "Quốc tịch", "Số CMND/CCCD", "Ngày cấp",
            "Nơi cấp", "Tình trạng hôn nhân", "Trình độ học vấn", "Chuyên ngành",
            "Ngôn ngữ", "Kinh nghiệm làm việc", "Kỹ năng", "Sở thích", "Giới thiệu bản thân",
            "Mục tiêu nghề nghiệp"
        ).random(),
        value = null,
        placeholder = randomString(15),
        type = listOf("text", "date", "time", "number", "singleChoice", "multiChoice", "checkBox").random(),
        required = randomBoolean(),
        options = if (randomBoolean()) randomOptions(Random.nextInt(10, 30)) else null,
        checked = randomBoolean(),
        created = randomDate(),
        updated = randomDate(),
        deleted = if (randomBoolean()) randomDate() else null,
    )
}
fun generateRandomFieldHeader(): Field {
    return Field(
        id = Random.nextLong(1, 1000),
        label = listOf(
            "Họ và tên", "Ngày sinh", "Giới tính", "Nơi ở", "Vị trí công việc",
            "Số điện thoại", "Email", "Quốc tịch", "Số CMND/CCCD", "Ngày cấp",
            "Nơi cấp", "Tình trạng hôn nhân", "Trình độ học vấn", "Chuyên ngành",
            "Ngôn ngữ", "Kinh nghiệm làm việc", "Kỹ năng", "Sở thích", "Giới thiệu bản thân",
            "Mục tiêu nghề nghiệp"
        ).random(),
        value = randomString(24),
        placeholder = randomString(15),
        type = listOf("text", "date", "time", "number", "singleChoice", "multiChoice", "checkBox").random(),
        required = randomBoolean(),
        options = if (randomBoolean()) randomOptions(Random.nextInt(3, 20)) else null,
        checked = randomBoolean(),
        created = randomDate(),
        updated = randomDate(),
        deleted = if (randomBoolean()) randomDate() else null,
    )
}
fun generateRandomSheet(): Body {
    return Body(
        id = Random.nextLong(1, 1000),
        type = listOf("Sheet", "Tần suất", "Nhiều tờ", "Other").random(),
        list_field = generateRandomFieldList(18),
        is_done = false,
        name = "Form Sheet 1",
        form_code = randomString(10),
        form_name = randomString(10),
        request_code = randomString(10),
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
fun generateRandomHeaderList(size: Int): List<Field> {
    return List(size) { generateRandomFieldHeader() }
}

