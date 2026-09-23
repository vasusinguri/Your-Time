package com.yourtime.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class NextBirthdayCalculatorTest {

    @Test
    fun `birthday is today returns isToday true`() {
        val birth = LocalDateTime.of(2000, 5, 15, 10, 0, 0)
        val now = LocalDateTime.of(2026, 5, 15, 14, 30, 0)

        val result = NextBirthdayCalculator.calculate(birth, now)

        assertTrue(result.isToday)
        assertEquals(LocalDate.of(2026, 5, 15), result.nextBirthdayDate)
    }

    @Test
    fun `birthday later this year calculates correct countdown`() {
        val birth = LocalDateTime.of(2000, 10, 20, 12, 0, 0)
        val now = LocalDateTime.of(2026, 10, 18, 12, 0, 0)

        val result = NextBirthdayCalculator.calculate(birth, now)

        assertFalse(result.isToday)
        assertEquals(2L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(0L, result.seconds)
        assertEquals(LocalDate.of(2026, 10, 20), result.nextBirthdayDate)
    }

    @Test
    fun `birthday already passed this year targets next year`() {
        val birth = LocalDateTime.of(2000, 1, 10, 8, 0, 0)
        val now = LocalDateTime.of(2026, 6, 1, 12, 0, 0)

        val result = NextBirthdayCalculator.calculate(birth, now)

        assertFalse(result.isToday)
        assertEquals(LocalDate.of(2027, 1, 10), result.nextBirthdayDate)
    }

    @Test
    fun `leap year baby on non leap year resolves to Feb 28`() {
        val leapBirth = LocalDateTime.of(2004, 2, 29, 9, 0, 0)
        val nowInNonLeapYear = LocalDateTime.of(2026, 1, 1, 0, 0, 0) // 2026 is non-leap

        val result = NextBirthdayCalculator.calculate(leapBirth, nowInNonLeapYear)

        assertEquals(LocalDate.of(2026, 2, 28), result.nextBirthdayDate)
    }
}
