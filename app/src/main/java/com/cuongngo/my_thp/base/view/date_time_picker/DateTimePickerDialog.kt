package com.cuongngo.my_thp.base.view.date_time_picker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import com.cuongngo.my_thp.utils.date.getCurrentDayOfMonth
import com.cuongngo.my_thp.utils.date.getCurrentMonth
import com.cuongngo.my_thp.utils.date.getCurrentYear
import java.util.*

class DateTimePickerDialog @JvmOverloads constructor(
    var context: Context,
    var listener: ListenerDateTime,
    var calendar: Calendar? = null,
    var maxDate: Long? = null,
    var minDate: Long? = System.currentTimeMillis(),
    var isCancelable: Boolean = true
) {

    lateinit var datePickerDialog: DatePickerDialog
    lateinit var timePickerDialog: TimePickerDialog
    private var isShowing: Boolean = false

    init {
        setUpDateTimePicker()
    }

    private fun setUpDateTimePicker() {
        // Default date values
        val defaultYear = calendar?.get(Calendar.YEAR) ?: getCurrentYear()
        val defaultMonth = calendar?.get(Calendar.MONTH) ?: getCurrentMonth()
        val defaultDay = calendar?.get(Calendar.DAY_OF_MONTH) ?: getCurrentDayOfMonth()

        // DatePicker setup
        datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                // Update the calendar with the selected date
                calendar = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }
                // After selecting date, show the TimePicker
                showTimePickerDialog()
            },
            defaultYear,
            defaultMonth,
            defaultDay
        ).apply {
            maxDate?.let { datePicker.maxDate = it }
            minDate?.let { datePicker.minDate = it }
            setCancelable(isCancelable)
        }
    }

    private fun showTimePickerDialog() {
        // Get current time
        val hour = calendar?.get(Calendar.HOUR_OF_DAY) ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = calendar?.get(Calendar.MINUTE) ?: Calendar.getInstance().get(Calendar.MINUTE)

        // TimePicker setup
        timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Update calendar with the selected time
                calendar?.apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }
                // After selecting time, pass the selected date and time to listener
                calendar?.let { listener.onDateTimeSelected(it) }
                dismiss()
            },
            hour,
            minute,
            true // Use 24-hour format
        ).apply {
            setCancelable(isCancelable)
        }

        // Show the TimePickerDialog after selecting the date
        timePickerDialog.show()
    }

    fun show() {
        if (!isShowing) {
            datePickerDialog.show()
            isShowing = true
        }
    }

    fun dismiss() {
        if (isShowing) {
            datePickerDialog.dismiss()
            timePickerDialog.dismiss()
            isShowing = false
        }
    }
}

interface ListenerDateTime {
    fun onDateTimeSelected(calendar: Calendar)
}
