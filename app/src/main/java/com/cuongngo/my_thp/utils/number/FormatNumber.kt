package com.cuongngo.my_thp.utils.number

import java.text.NumberFormat
import java.util.Locale

fun formatNumberWithDots(input: String?): String {
    if (!input.isNullOrEmpty()){
        val number = input.toLongOrNull() ?: return input // Convert to Long or return original if invalid
        val numberFormat = NumberFormat.getInstance(Locale.GERMANY) // Locale with dot as grouping separator
        return numberFormat.format(number)
    }else{
        return "0"
    }
}