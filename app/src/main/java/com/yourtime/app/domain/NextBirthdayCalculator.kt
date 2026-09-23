package com.yourtime.app.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.temporal.ChronoUnit

data class NextBirthdayResult(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
    val isToday: Boolean,
    val nextBirthdayDate: LocalDate
)

object NextBirthdayCalculator {

    fun calculate(birth: LocalDateTime, now: LocalDateTime = LocalDateTime.now()): NextBirthdayResult {
        val today = now.toLocalDate()
        val birthMonth = birth.monthValue
        val birthDay = birth.dayOfMonth

        fun getBirthdayForYear(year: Int): LocalDate {
            return if (birthMonth == 2 && birthDay == 29 && !Year.isLeap(year.toLong())) {
                LocalDate.of(year, 2, 28)
            } else {
                LocalDate.of(year, birthMonth, birthDay)
            }
        }

        val birthdayThisYearDate = getBirthdayForYear(today.year)
        val birthdayThisYearDateTime = LocalDateTime.of(birthdayThisYearDate, birth.toLocalTime())

        val targetDateTime: LocalDateTime
        val isTodayBirthday = today == birthdayThisYearDate

        if (now.isAfter(birthdayThisYearDateTime) && !isTodayBirthday) {
            val nextYearDate = getBirthdayForYear(today.year + 1)
            targetDateTime = LocalDateTime.of(nextYearDate, birth.toLocalTime())
        } else {
            targetDateTime = birthdayThisYearDateTime
        }

        val totalSeconds = maxOf(0L, ChronoUnit.SECONDS.between(now, targetDateTime))
        val days = totalSeconds / 86400
        val hours = (totalSeconds % 86400) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return NextBirthdayResult(
            days = days,
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            isToday = isTodayBirthday,
            nextBirthdayDate = targetDateTime.toLocalDate()
        )
    }
}
