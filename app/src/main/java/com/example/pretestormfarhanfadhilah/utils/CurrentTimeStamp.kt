package com.example.pretestormfarhanfadhilah.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTime(): String{
    val calendarInstance = Calendar.getInstance().time
    val dateString = calendarInstance.toString("dd/MM/yyyy | HH:mm:ss")

    return dateString
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
