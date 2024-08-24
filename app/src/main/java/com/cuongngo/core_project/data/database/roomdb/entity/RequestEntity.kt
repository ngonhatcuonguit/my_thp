package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
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
    indices = [Index(value = ["request_code", "request_id"], unique = true)],
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
    @ColumnInfo(name = "informer") var informer: List<UserTHPEntity>? = null,
    @ColumnInfo(name = "form_code") var formCode: String,
    @ColumnInfo(name = "form_id") var formID: String,
    @ColumnInfo(name = "process_id") var process_id: String?,
    @ColumnInfo(name = "form_name") var formName: String,
    @ColumnInfo(name = "request_status") var requestStatus: Int? = null,
    @ColumnInfo(name = "process_steps") var processSteps: List<ProcessStep>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "list_header") var listHeader: List<Field>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "list_body") var listBody: List<Body>? = null,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "form_bottom_sign") var formBottomSign: List<Field>? = null,
    @ColumnInfo(name = "form_bottom_note") var formBottomNote: String? = null,
    @ColumnInfo(name = "created_at") var created: String? = null,
    @ColumnInfo(name = "updated_at") var updated: String? = null,
    @ColumnInfo(name = "deleted_at") var deleted: String? = null,
    @ColumnInfo(name = "version") var version: Float? = 0F,
    @ColumnInfo(name = "is_sync") var isSync: Boolean? = false,
) : BaseModel()


data class RequestData(
    val requestID: Long,
    var requestCode: String,
    var requestName: String? = null,
    var requestDescription: String? = null,
    var createdBy: UserTHPEntity? = null,
    var informer: List<UserTHPEntity>? = null,
    var formCode: String,
    var formID: String,
    var process_id: String?,
    var formName: String,
    var requestStatus: Int? = null,
    var processSteps: List<ProcessStep>? = null,
    var listHeader: List<Field>? = null,
    var listBody: List<Body>? = null,
    var formBottomSign: List<Field>? = null,
    var formBottomNote: String? = null,
    var created: String? = null,
    var updated: String? = null,
    var deleted: String? = null,
    var version: Float? = 0F,
    var isSync: Boolean? = false
) : BaseModel()

fun RequestEntity.toDataClass(): RequestData {
    return RequestData(
        requestID = this.requestID,
        requestCode = this.requestCode,
        requestName = this.requestName,
        requestDescription = this.requestDescription,
        createdBy = this.createdBy,
        informer = this.informer,
        formCode = this.formCode,
        formID = this.formID,
        process_id = this.process_id,
        formName = this.formName,
        requestStatus = requestStatus,
        processSteps = this.processSteps,
        listHeader = this.listHeader,
        listBody = this.listBody,
        formBottomSign = this.formBottomSign,
        formBottomNote = formBottomNote,
        created = this.created,
        updated = this.updated,
        deleted = this.deleted,
        version = this.version,
        isSync = this.isSync
    )
}