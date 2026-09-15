package com.yourtime.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class AgeCalculatorTest {

    // 1. Normal birthday
    @Test
    fun testNormalBirthday() {
        val birth = LocalDateTime.of(2003, 9, 14, 7, 30, 0)
        val current = LocalDateTime.of(2026, 8, 14, 17, 45, 32)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(22L, result.years)
        assertEquals(11L, result.months)
        assertEquals(0L, result.days)
        assertEquals(10L, result.hours)
        assertEquals(15L, result.minutes)
        assertEquals(32L, result.seconds)
    }

    // 2. Birthday before current date in the month
    @Test
    fun testBirthdayBeforeCurrentDateInMonth() {
        val birth = LocalDateTime.of(2000, 5, 10, 12, 0, 0)
        val current = LocalDateTime.of(2024, 5, 15, 12, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(24L, result.years)
        assertEquals(0L, result.months)
        assertEquals(5L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(0L, result.seconds)
    }

    // 3. Birthday after current date in the month
    @Test
    fun testBirthdayAfterCurrentDateInMonth() {
        val birth = LocalDateTime.of(2000, 5, 20, 12, 0, 0)
        val current = LocalDateTime.of(2024, 5, 10, 12, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        // 23 years, 11 months, and from April 20 to May 10 = 20 days
        assertEquals(23L, result.years)
        assertEquals(11L, result.months)
        assertEquals(20L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(0L, result.seconds)
    }

    // 4. Leap-year birth
    @Test
    fun testLeapYearBirth() {
        val birth = LocalDateTime.of(2020, 2, 15, 10, 0, 0)
        val current = LocalDateTime.of(2021, 2, 15, 10, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(1L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(0L, result.seconds)
    }

    // 5. February 29 birth
    @Test
    fun testFebruary29Birth() {
        val birth = LocalDateTime.of(2024, 2, 29, 0, 0, 0)
        
        // On non-leap year Feb 28
        val onFeb28NextYear = LocalDateTime.of(2025, 2, 28, 0, 0, 0)
        val res1 = AgeCalculator.calculate(birth, onFeb28NextYear)
        assertEquals(1L, res1.years)
        assertEquals(0L, res1.months)
        assertEquals(0L, res1.days)

        // On non-leap year March 1
        val onMar1NextYear = LocalDateTime.of(2025, 3, 1, 0, 0, 0)
        val res2 = AgeCalculator.calculate(birth, onMar1NextYear)
        assertEquals(1L, res2.years)
        assertEquals(0L, res2.months)
        assertEquals(1L, res2.days)

        // On next leap year (2028-02-29)
        val nextLeap = LocalDateTime.of(2028, 2, 29, 0, 0, 0)
        val res3 = AgeCalculator.calculate(birth, nextLeap)
        assertEquals(4L, res3.years)
        assertEquals(0L, res3.months)
        assertEquals(0L, res3.days)
    }

    // 6. End-of-month dates
    @Test
    fun testEndOfMonthDates() {
        val birth = LocalDateTime.of(2024, 1, 31, 10, 0, 0)
        
        // January 31 -> February 29 (in 2024 leap year)
        val feb29 = LocalDateTime.of(2024, 2, 29, 10, 0, 0)
        val res1 = AgeCalculator.calculate(birth, feb29)
        assertEquals(0L, res1.years)
        assertEquals(1L, res1.months)
        assertEquals(0L, res1.days)

        // January 31 -> March 31
        val mar31 = LocalDateTime.of(2024, 3, 31, 10, 0, 0)
        val res2 = AgeCalculator.calculate(birth, mar31)
        assertEquals(0L, res2.years)
        assertEquals(2L, res2.months)
        assertEquals(0L, res2.days)
    }

    // 7. Midnight
    @Test
    fun testMidnight() {
        val birth = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
        val current = LocalDateTime.of(2024, 1, 2, 0, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(0L, result.years)
        assertEquals(0L, result.months)
        assertEquals(1L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(0L, result.seconds)
    }

    // 8. Exactly one second difference
    @Test
    fun testExactlyOneSecondDifference() {
        val birth = LocalDateTime.of(2024, 6, 1, 12, 0, 0)
        val current = LocalDateTime.of(2024, 6, 1, 12, 0, 1)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(0L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(1L, result.seconds)
    }

    // 9. Exactly one minute difference
    @Test
    fun testExactlyOneMinuteDifference() {
        val birth = LocalDateTime.of(2024, 6, 1, 12, 0, 0)
        val current = LocalDateTime.of(2024, 6, 1, 12, 1, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(0L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(1L, result.minutes)
        assertEquals(0L, result.seconds)
    }

    // 10. Exactly one hour difference
    @Test
    fun testExactlyOneHourDifference() {
        val birth = LocalDateTime.of(2024, 6, 1, 12, 0, 0)
        val current = LocalDateTime.of(2024, 6, 1, 13, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(0L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(1L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(0L, result.seconds)
    }

    // 11. Day transition
    @Test
    fun testDayTransition() {
        val birth = LocalDateTime.of(2024, 5, 10, 23, 59, 59)
        val current = LocalDateTime.of(2024, 5, 11, 0, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(0L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(1L, result.seconds)
    }

    // 12. Month transition
    @Test
    fun testMonthTransition() {
        val birth = LocalDateTime.of(2024, 4, 30, 23, 59, 59)
        val current = LocalDateTime.of(2024, 5, 1, 0, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(0L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(1L, result.seconds)
    }

    // 13. Year transition
    @Test
    fun testYearTransition() {
        val birth = LocalDateTime.of(2023, 12, 31, 23, 59, 59)
        val current = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
        val result = AgeCalculator.calculate(birth, current)

        assertEquals(0L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(0L, result.hours)
        assertEquals(0L, result.minutes)
        assertEquals(1L, result.seconds)
    }

    // 14. Future birth date validation
    @Test
    fun testFutureBirthDateValidation() {
        val birth = LocalDateTime.of(2030, 1, 1, 0, 0, 0)
        val current = LocalDateTime.of(2026, 1, 1, 0, 0, 0)

        assertFalse("Future birth date should be marked invalid", AgeCalculator.isValid(birth, current))
        assertTrue("Past birth date should be valid", AgeCalculator.isValid(current, birth))

        assertThrows(IllegalArgumentException::class.java) {
            AgeCalculator.calculate(birth, current)
        }
    }
}
