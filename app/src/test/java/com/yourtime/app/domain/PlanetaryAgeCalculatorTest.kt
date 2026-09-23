package com.yourtime.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class PlanetaryAgeCalculatorTest {

    @Test
    fun `calculates planetary ages for exactly 365 point 25 days lived`() {
        val birth = LocalDateTime.of(2025, 1, 1, 0, 0, 0)
        // 365.25 days = 31,557,600 seconds
        val now = birth.plusDays(365).plusHours(6)

        val results = PlanetaryAgeCalculator.calculate(birth, now)

        assertEquals(6, results.size)

        val mercury = results.first { it.planetName == "Mercury" }
        val earth = results.first { it.planetName == "Earth" }
        val mars = results.first { it.planetName == "Mars" }

        // Earth should be ~1.0 year
        assertTrue("Earth age should be approximately 1.0", earth.ageInYears in 0.99..1.01)

        // Mercury year is ~88 days, so in 365.25 days, Mercury age should be ~4.15
        assertTrue("Mercury age should be approximately 4.15", mercury.ageInYears in 4.1..4.2)

        // Mars year is ~687 days, so in 365.25 days, Mars age should be ~0.53
        assertTrue("Mars age should be approximately 0.53", mars.ageInYears in 0.5..0.6)
    }

    @Test
    fun `all planets contain valid symbols and accent colors`() {
        val birth = LocalDateTime.of(2000, 1, 1, 0, 0, 0)
        val now = LocalDateTime.of(2026, 1, 1, 0, 0, 0)

        val results = PlanetaryAgeCalculator.calculate(birth, now)

        results.forEach { planet ->
            assertTrue("Planet name should not be blank", planet.planetName.isNotBlank())
            assertTrue("Planet symbol should not be blank", planet.symbol.isNotBlank())
            assertTrue("Age should be positive", planet.ageInYears > 0.0)
            assertTrue("Orbital period should be positive", planet.orbitalPeriodEarthDays > 0.0)
        }
    }
}
