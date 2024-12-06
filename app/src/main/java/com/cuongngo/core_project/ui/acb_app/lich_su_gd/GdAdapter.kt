package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.DayTransaction
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GdAdapter(
    private val dayTransactions: ArrayList<DayTransaction>,
    private val onItemClickListener: ((GdEntity) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DATE = 0
        private const val TYPE_TRANSACTION = 1
    }

    // Sử dụng ArrayList thay vì List
    private val listDayTransaction = ArrayList<Any>()

    init {
        // Chuyển đổi dayTransactions thành một danh sách các phần tử (Title ngày và giao dịch)
        dayTransactions.forEach { dayTransaction ->
            // Thêm tiêu đề ngày vào list
            listDayTransaction.add(dayTransaction.date)
            // Thêm tất cả giao dịch của ngày đó vào list
            listDayTransaction.addAll(dayTransaction.transactions)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_day_title, parent, false)
                DateViewHolder(view)
            }

            TYPE_TRANSACTION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_transaction, parent, false)
                TransactionViewHolder(view)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> {
                // Kiểm tra xem phần tử là Calendar hay không
                val calendar = listDayTransaction[position] as Calendar
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                holder.tvDate.text = formattedDate
            }

            is TransactionViewHolder -> {
                // Kiểm tra phần tử là GdEntity (giao dịch)
                val transaction = listDayTransaction[position] as GdEntity
                holder.tvTransactionCode.text = transaction.transactionCode
                holder.tvTransactionAmount.text = transaction.transactionAmount.toString()
                holder.tvTransactionContent.text = transaction.transactionContent
                val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(transaction.transactionDate.time)
                holder.tvTransactionTime.text = formattedTime
                holder.itemView.setOnClickListener {
                    onItemClickListener?.invoke(transaction)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listDayTransaction.size  // Trả về số lượng phần tử trong danh sách mới
    }

    override fun getItemViewType(position: Int): Int {
        return if (listDayTransaction[position] is Calendar) TYPE_DATE else TYPE_TRANSACTION
    }

    fun submitListDayTransaction(listDayTransaction: ArrayList<DayTransaction>?) {
        if (listDayTransaction != null) {
            this.listDayTransaction.clear()
            listDayTransaction.forEach { dayTransaction ->
                this.listDayTransaction.add(dayTransaction.date)  // Thêm tiêu đề ngày
                this.listDayTransaction.addAll(dayTransaction.transactions)  // Thêm giao dịch
            }
            notifyDataSetChanged()
        }
    }


    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTransactionCode: TextView = itemView.findViewById(R.id.tvTransactionCode)
        val tvTransactionAmount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
        val tvTransactionContent: TextView = itemView.findViewById(R.id.tvTransactionContent)
        val tvTransactionTime: TextView = itemView.findViewById(R.id.tvTransactionTime)
    }
}

