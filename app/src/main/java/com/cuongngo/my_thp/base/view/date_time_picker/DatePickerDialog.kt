package com.cuongngo.my_thp.base.view.date_time_picker

import android.app.DatePickerDialog
import android.content.Context
import com.cuongngo.my_thp.utils.date.getCurrentDayOfMonth
import com.cuongngo.my_thp.utils.date.getCurrentMonth
import com.cuongngo.my_thp.utils.date.getCurrentYear
import java.util.*

class DatePickerDialog @JvmOverloads constructor(
    var context: Context,
    var listener: Listener,
    var calendar: Calendar? = null,
    var maxDate: Long? = null,
    var minDate: Long? = System.currentTimeMillis(),
    var isCancelable: Boolean = true
) {

    lateinit var datePickerDialog: DatePickerDialog
    private var isShowing: Boolean = false

    init {
        setUpDatePicker()
    }

    private fun setUpDatePicker() {
        val defaultYear = calendar?.get(Calendar.YEAR) ?: getCurrentYear()
        val defaultMonth = calendar?.get(Calendar.MONTH) ?: getCurrentMonth()
        val defaultDay = calendar?.get(Calendar.DAY_OF_MONTH) ?: getCurrentDayOfMonth()

        datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                calendar = Calendar.getInstance().apply {
                    set(year,monthOfYear,dayOfMonth)
                }
                calendar?.let { listener.onDateSelected(it) }
                dismiss()
            },
            defaultYear,
            defaultMonth,
            defaultDay
        ).apply {
            maxDate?.let {
                datePicker.maxDate = it
            }
            minDate?.let{
                datePicker.minDate = it
            }
            setCancelable(isCancelable)
        }
    }

    fun show() {
        if (!isShowing) {
//            val width = (getScreenWidth() * 0.5).toInt()
//            datePickerDialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            datePickerDialog.show()
            isShowing = true
        }
    }

    fun dismiss() {
        if (isShowing) {
            datePickerDialog.dismiss()
            isShowing = false
        }
    }
}

interface Listener {
    fun onDateSelected(calendar: Calendar)
}