package com.cuongngo.my_thp.utils.date

import com.cuongngo.my_thp.utils.date.enum.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

fun previewAt (dateString: String) : String{
    val OLD_FORMAT = "yyyy-MM-dd"
    val NEW_FORMAT = "dd/MM/yyyy"
    try {
        val sdf = SimpleDateFormat(OLD_FORMAT, Locale.US)
        val d = sdf.parse(dateString)
        sdf.applyPattern(NEW_FORMAT)
        return sdf.format(d)
    }catch (e:Throwable){
        return ""
    }
}

fun Calendar.convertToDateDDMMYYYY(): String {
    val year = this.get(Calendar.YEAR)
    val monthOfYear = this.get(Calendar.MONTH)
    val dayOfMonth = this.get(Calendar.DAY_OF_MONTH)
    val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
    val mon = monthOfYear + 1
    val monthStr = if (mon < 10) "0${mon}" else "$mon"

    return "$dayStr/$monthStr/$year"
}

fun Calendar.isToday() : Boolean {
    val today = Calendar.getInstance()
    return today[Calendar.YEAR] == get(Calendar.YEAR) && today[Calendar.DAY_OF_YEAR] == get(Calendar.DAY_OF_YEAR)
}

fun Calendar.getDateStringByServerStandard(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = this.time
    return dateFormat.format(date)
}

fun getCurrentYear() : Int {
    return Calendar.getInstance().get(Calendar.YEAR)
}

fun getCurrentMonth() : Int {
    return Calendar.getInstance().get(Calendar.MONTH)
}

fun getCurrentDayOfMonth() : Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
}
fun getCurrentHourOfDay() : Int {
    return Calendar.getInstance().get(Calendar.HOUR)
}
fun getCurrentMinuteOfHour() : Int {
    return Calendar.getInstance().get(Calendar.MINUTE)
}

fun getCurrentTimeSecond() : Long = System.currentTimeMillis()/1000

fun Calendar.getDaysDiff(calendar: Calendar): Long {
    val diff = this.timeInMillis - calendar.timeInMillis
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}

class DateUtils {
    companion object {
        /**
         * @return a date string contain year, month and date
         */
        fun getDateString(year: Int, monthOfYear: Int, dayOfMonth: Int, dateFormat: DateFormat): String {
            val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
            val mon = monthOfYear + 1
            val monthStr = if (mon < 10) "0${mon}" else "$mon"

            val dateString ="$dayStr/$monthStr/$year"
            val OLD_FORMAT = "dd/MM/yyyy"
            return try {
                val sdf = SimpleDateFormat(OLD_FORMAT, Locale.US)
                val d = sdf.parse(dateString)
                sdf.applyPattern(dateFormat.format)
                if(d!=null){
                    sdf.format(d)
                } else {
                    ""
                }
            }catch (e:Throwable){
                ""
            }
        }

        /**
         * @return a date string contain year, month and date
         */
        fun getDateString(millis: Long, dateFormat: DateFormat): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            val date = calendar.time
            return try {
                val sdf = SimpleDateFormat(dateFormat.format, Locale.US)
                sdf.format(date)
            } catch (e:Throwable){
                ""
            }

        }

        /**
         * @param dateString example: 26/05/2021 22:00:00
         * @param currentFormat the current format of dateString, example: dateString value is 26/05/2021 22:00:00, the currentFormat value must be dd/MM/yyyy HH:mm:ss
         * @param newFormat the expected new dateString format, example: yyyy/MM/dd
         * @return the new date string rely on newFormat value
         */
        fun formatDateString(dateString: String, currentFormat: String, newFormat: String): String {
            return try {
                val sdf = SimpleDateFormat(currentFormat, Locale.US)
                val d = sdf.parse(dateString)
                sdf.applyPattern(newFormat)
                if(d!=null){
                    sdf.format(d)
                } else {
                    ""
                }
            }catch (e:Throwable){
                "${e.message}"
            }
        }

        /**
         * @param dateString a date string like 27-05-2021
         * @param dateStringFormat the format of the dateString, if this param value is provided incorrectly,
         * the function will return a Calendar instance with current date time.
         * Example: if dateString is 27-05-2021, dateStringFormat must be dd-MM-yyyy
         */
        fun convertStringToCalendar(dateString: String, dateStringFormat: String): Calendar {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat(dateStringFormat, Locale.US)
            try {
                sdf.parse(dateString)?.let {
                    cal.time = it
                }
            } catch (e: Exception) {

            }
            return cal
        }
    }
}
fun convertUTCtoGMT(utcDateTime: String, outputPattern: String = "dd/MM/yyyy HH:mm"): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

    val utcDate = inputFormat.parse(utcDateTime)
    val utcTimeMillis = utcDate.time
    val systemTimeZone = TimeZone.getDefault()
    val timeZoneOffsetInMillis = systemTimeZone.getOffset(utcTimeMillis)
    val gmtTimeMillis = utcTimeMillis + timeZoneOffsetInMillis
    return outputFormat.format(gmtTimeMillis)
}
//
//fun getUTCTime(outputPattern: String = "dd/MM/yyyy HH:mm"): String {
//    val currentTime = System.currentTimeMillis() //GMT time
//    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
//    val outputFormat = SimpleDateFormat(outputPattern, Locale.US)
//
//    val utcDate = inputFormat.parse(utcDateTime)
//    val utcTimeMillis = utcDate.time
//    val systemTimeZone = TimeZone.getDefault()
//    val timeZoneOffsetInMillis = systemTimeZone.getOffset(utcTimeMillis)
//    val gmtTimeMillis = utcTimeMillis + timeZoneOffsetInMillis
//    return outputFormat.format(gmtTimeMillis)
//}