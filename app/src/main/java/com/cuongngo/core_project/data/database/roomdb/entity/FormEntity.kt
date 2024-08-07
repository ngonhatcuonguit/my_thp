package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cuongngo.core_project.response.BaseModel
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
    @ColumnInfo(name = "category") var category: String? = null,
    @ColumnInfo(name = "process_id") var processID: Int? = null,
    @ColumnInfo(name = "status") var status: Int? = null,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "schema_name") var schemaName: String?,
    @ColumnInfo(name = "schema_code") var schemaCode: String?,
    @ColumnInfo(name = "schema_type") var schemaType: String? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "list_header") var listHeader: List<Field>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "list_body") var listBody: List<Body>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "process_steps") var processSteps: List<ProcessStep>? = null,
    @ColumnInfo(name = "form_bottom_sign") var formBottomSign: List<Field>? = null,
    @ColumnInfo(name = "form_bottom_note") var formBottomNote: String? = null,
    @ColumnInfo(name = "created_at") var created: String? = null,
    @ColumnInfo(name = "updated_at") var updated: String? = null,
    @ColumnInfo(name = "deleted_at") var deleted: String? = null,
    @ColumnInfo(name = "form_version") var formVersion: String? = null
) : BaseModel()

data class Body(
    val id: Long?,
    var name: String?,
    var formCode: String?,
    var requestCode: String?,
    var formName: String?,
    var listHeader: List<Field>? = null,
    var listField: List<Field>?,
    var isDone: Boolean?,
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
    companion object {
        const val STATUS_ACTIVE = 1
        const val STATUS_INACTIVE = 0
    }
}

data class Option(
    val id: Long?,
    val value: String?,
    val label: String?,
    val type: String? = null,
    var isSelect: Boolean? = false
): BaseModel()

data class ProcessStep(
    val id: Long,
    val name: String?,
    val status: Int? = null,
    var owner: List<UserTHPEntity>? = null,
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
        type = listOf("text", "select", "checkbox", "radio", "date").random(),
        required = randomBoolean(),
        options = if (randomBoolean()) randomOptions(Random.nextInt(3, 20)) else null,
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
        type = listOf("text", "select", "checkbox", "date", "time", "checkbox").random(),
        required = randomBoolean(),
        options = if (randomBoolean()) randomOptions(Random.nextInt(10, 100)) else null,
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
        listField = generateRandomFieldList(18),
        isDone = false,
        name = "Form Sheet 1",
        formCode = randomString(10),
        formName = randomString(10),
        requestCode = randomString(10),
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

