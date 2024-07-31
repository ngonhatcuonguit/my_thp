package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cuongngo.core_project.response.BaseModel

@Entity(
    tableName = "request_value",
//    foreignKeys = [ForeignKey(
//        entity = FormEntity::class,
//        parentColumns = arrayOf("form_code"),
//        childColumns = arrayOf("request_id"),
////        onDelete = ForeignKey.CASCADE
//    )],
    indices = [Index(value = ["request_code","request_id"], unique = true)],
)
@TypeConverters(Converters::class)
data class RequestEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "request_id") val requestID: Long,
    @ColumnInfo(name = "request_code") var requestCode: String,
    @ColumnInfo(name = "request_name") var requestName: String? = null,
    @ColumnInfo(name = "request_description:") var requestDescription: String? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "created_by") var createdBy: UserTHPEntity? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "informer") var informer: UserTHPEntity? = null,
    @ColumnInfo(name = "form_code") var formCode: String,
    @ColumnInfo(name = "request_status") var requestStatus: Int? = null,
    @ColumnInfo(name = "process_step")
    var processStep: List<ProcessStep>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_header") var formHeader: List<Field>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "list_sheet") var listSheet: List<Sheet>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_bottom_sign") var formBottomSign: List<Field>? = null,
    @ColumnInfo(name = "form_bottom_note") var formBottomNote: String? = null,
    @ColumnInfo(name = "created_at") var created: String? = null,
    @ColumnInfo(name = "updated_at") var updated: String? = null,
    @ColumnInfo(name = "deleted_at") var deleted: String? = null
) : BaseModel(){

}