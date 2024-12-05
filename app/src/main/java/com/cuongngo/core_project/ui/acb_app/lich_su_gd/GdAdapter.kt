package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.data.database.roomdb.entity.DayTransaction
import java.text.SimpleDateFormat
import java.util.Locale

class GdAdapter(private val dayTransactions: List<DayTransaction>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DATE = 0
        private const val TYPE_TRANSACTION = 1
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
                val dayTransaction = dayTransactions[position / 2]  // Xác định vị trí của ngày
                holder.tvDate.text =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dayTransaction.date)
            }

            is TransactionViewHolder -> {
                val dayTransaction = dayTransactions[position / 2]  // Xác định vị trí của giao dịch
                val transaction = dayTransaction.transactions[position % 2]  // Xác định giao dịch
                holder.tvTransactionCode.text = transaction.transactionCode
                holder.tvTransactionAmount.text = transaction.transactionAmount.toString()
                holder.tvTransactionContent.text = transaction.transactionContent
                holder.tvTransactionTime.text = SimpleDateFormat(
                    "HH:mm",
                    Locale.getDefault()
                ).format(transaction.transactionDate)
            }
        }
    }

    override fun getItemCount(): Int {
        return dayTransactions.sumBy { it.transactions.size * 2 }  // Tổng số giao dịch và tiêu đề ngày
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_DATE else TYPE_TRANSACTION
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
