package com.example.exammitrabykaushal.UIScreens.component

import java.util.Calendar
import java.util.concurrent.TimeUnit

fun calculateStreak(dates: List<Long>): Int {
    if (dates.isEmpty()) return 0

    val uniqueDays = dates
        .map { normalizeDay(it) }
        .distinct()
        .sortedDescending()

    var streak = 1

    for (i in 0 until uniqueDays.size - 1) {
        val diff = TimeUnit.MILLISECONDS.toDays(
            uniqueDays[i] - uniqueDays[i + 1]
        )
        if (diff == 1L) streak++
        else break
    }

    return streak
}

private fun normalizeDay(time: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
