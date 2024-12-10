package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cuongngo.core_project.response.BaseModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
    val transactionAmount: String,

    @ColumnInfo(name = "transaction_content") // Tên cột cho "Nội dung giao dịch"
    val transactionContent: String,

    @ColumnInfo(name = "transaction_date")
    val transactionDate: Calendar

) : BaseModel()

data class DayTransaction(
    val date: Calendar,  // Ngày của các giao dịch
    val transactions: List<GdEntity>  // Các giao dịch trong ngày đó
)

fun groupTransactionsByDate(transactions: List<GdEntity>): ArrayList<DayTransaction> {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val groupedMap = transactions.groupBy {
        dateFormat.format(it.transactionDate.time) // Nhóm các giao dịch theo ngày
    }

    // Chuyển đổi kết quả từ List sang ArrayList
    val result = ArrayList<DayTransaction>(
        groupedMap.map { entry ->
            val dayTransactions = entry.value.sortedByDescending { it.transactionDate.time } // Sắp xếp các giao dịch theo ngày giảm dần
            // Chuyển chuỗi ngày vào Calendar
            val calendar = Calendar.getInstance().apply {
                time = dateFormat.parse(entry.key) ?: Date()
            }
            DayTransaction(calendar, dayTransactions)  // Sử dụng Calendar thay vì Date
        }
    )

    // Sắp xếp các ngày theo thứ tự giảm dần (từ hiện tại đến quá khứ)
    result.sortByDescending { it.date.timeInMillis }

    return result
}



