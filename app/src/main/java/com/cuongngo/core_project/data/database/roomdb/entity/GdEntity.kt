package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cuongngo.core_project.response.BaseModel
import java.util.Date

@Entity(
    tableName = "giao_dich"
)

@TypeConverters(Converters::class)
data class GdEntity(
    @PrimaryKey(autoGenerate = true) // Id sẽ tự động tăng dần
    val id: Long = 0,

    @ColumnInfo(name = "transaction_code") // Tên cột cho "Mã giao dịch"
    val transactionCode: String,

    @ColumnInfo(name = "transaction_type") // Tên cột cho "Loại giao dịch"
    val transactionType: String,

    @ColumnInfo(name = "transaction_amount") // Tên cột cho "Số tiền giao dịch"
    val transactionAmount: Int,

    @ColumnInfo(name = "transaction_content") // Tên cột cho "Nội dung giao dịch"
    val transactionContent: String,

    @ColumnInfo(name = "transaction_date") // Tên cột cho "Ngày và Giờ giao dịch"
    @TypeConverters(Converters::class) // Chuyển đổi đối tượng Date thành kiểu lưu trữ trong SQLite
    val transactionDate: Date
) : BaseModel()

data class DayTransaction(
    val date: Date,  // Ngày của các giao dịch
    val transactions: List<GdEntity>  // Các giao dịch trong ngày đó
)