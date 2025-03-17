package com.cuongngo.my_thp.ui.acb_app.lich_su_gd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.ConstraintLayout
import com.cuongngo.my_thp.App
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.data.database.roomdb.entity.DayTransaction
import com.cuongngo.my_thp.data.database.roomdb.entity.GdEntity
import com.cuongngo.my_thp.utils.number.formatNumberWithDots
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GdAdapter(
    private val dayTransactions: ArrayList<DayTransaction>,
    private val onItemClickListener: ((GdEntity) -> Unit)? = null,
    private val onItemLongClickListener: ((GdEntity) -> Unit)? = null
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
                var amout = formatNumberWithDots(transaction.transactionAmount)
                if (transaction.transactionName.isNullOrEmpty()){
                    holder.tvTransactionUserName.isVisible = false
                }else{
                    holder.tvTransactionUserName.isVisible = true
                    holder.tvTransactionUserName.text = transaction.transactionName
                }
                if (transaction.transactionType == "Chuyen tien"){
                    holder.clImage.setBackgroundColor(App.getResources().getColor(R.color.acb_gray_background))
                    holder.tvTransactionAmount.setTextColor(App.getResources().getColor(R.color.black_1c))
                    holder.ivIcon.setImageResource(R.drawable.ic_arrow_down)
                    holder.tvTransactionAmount.text = "-$amout VND"
                }else{
                    holder.clImage.setBackgroundColor(App.getResources().getColor(R.color.acb_green_nhat))
                    holder.ivIcon.setImageResource(R.drawable.ic_arrow_up)
                    holder.tvTransactionAmount.setTextColor(App.getResources().getColor(R.color.primary_color))
                    holder.tvTransactionAmount.text = "+$amout VND"
                }

                holder.tvTransactionContent.text = transaction.transactionContent
                val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(transaction.transactionDate.time)
                holder.tvTransactionTime.text = formattedTime
                holder.itemView.setOnClickListener {
                    onItemClickListener?.invoke(transaction)
                }
                holder.itemView.setOnLongClickListener {
                    onItemLongClickListener?.invoke(transaction)
                    true
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

    fun removeItem(transaction: GdEntity) {
        val position = listDayTransaction.indexOf(transaction)
        if (position != -1) {
            listDayTransaction.removeAt(position)
            notifyItemRemoved(position)
//            notifyDataSetChanged()
        }
    }


    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTransactionUserName: TextView = itemView.findViewById(R.id.tvNameUser)
        val tvTransactionAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvTransactionContent: TextView = itemView.findViewById(R.id.tvContent)
        val tvTransactionTime: TextView = itemView.findViewById(R.id.tvTransactionTime)
        val ivIcon: ImageView = itemView.findViewById(R.id.iv_gd)
        val clImage: ConstraintLayout = itemView.findViewById(R.id.cl_image)
    }
}

