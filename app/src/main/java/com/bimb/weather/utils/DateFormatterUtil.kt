package com.bimb.weather.utils

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatterUtil {

    fun unixToDateString(
        unixTimestamp: Long,
        timeZone: Long = 0L,
        pattern: String = "hh:mm:ssa",
        locale: Locale = Locale.getDefault()
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        val instant = Instant.ofEpochSecond(unixTimestamp)
        val timeZoneOffset = ZoneOffset.ofTotalSeconds(timeZone.toInt())
        return instant.atZone(timeZoneOffset).format(formatter)
    }
}
