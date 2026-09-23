package com.yourtime.app.domain

import java.time.Duration
import java.time.LocalDateTime

data class PlanetaryAge(
    val planetName: String,
    val symbol: String,
    val ageInYears: Double,
    val orbitalPeriodEarthDays: Double,
    val description: String,
    val accentHex: Long
)

object PlanetaryAgeCalculator {

    fun calculate(birth: LocalDateTime, now: LocalDateTime = LocalDateTime.now()): List<PlanetaryAge> {
        val totalSeconds = maxOf(0L, Duration.between(birth, now).seconds)
        val earthDaysLived = totalSeconds.toDouble() / 86400.0

        return listOf(
            PlanetaryAge(
                planetName = "Mercury",
                symbol = "☿",
                ageInYears = earthDaysLived / 87.969,
                orbitalPeriodEarthDays = 87.97,
                description = "88 Earth days per year",
                accentHex = 0xFF67E8F9 // Cyan
            ),
            PlanetaryAge(
                planetName = "Venus",
                symbol = "♀",
                ageInYears = earthDaysLived / 224.701,
                orbitalPeriodEarthDays = 224.70,
                description = "225 Earth days per year",
                accentHex = 0xFFFBBF24 // Amber/Gold
            ),
            PlanetaryAge(
                planetName = "Earth",
                symbol = "♁",
                ageInYears = earthDaysLived / 365.256,
                orbitalPeriodEarthDays = 365.26,
                description = "Home planet (365.25 days)",
                accentHex = 0xFF10B981 // Emerald
            ),
            PlanetaryAge(
                planetName = "Mars",
                symbol = "♂",
                ageInYears = earthDaysLived / 686.980,
                orbitalPeriodEarthDays = 686.98,
                description = "687 Earth days per year",
                accentHex = 0xFFF87171 // Red/Coral
            ),
            PlanetaryAge(
                planetName = "Jupiter",
                symbol = "♃",
                ageInYears = earthDaysLived / 4332.589,
                orbitalPeriodEarthDays = 4332.59,
                description = "11.86 Earth years per year",
                accentHex = 0xFFFB923C // Orange
            ),
            PlanetaryAge(
                planetName = "Saturn",
                symbol = "♄",
                ageInYears = earthDaysLived / 10759.22,
                orbitalPeriodEarthDays = 10759.22,
                description = "29.45 Earth years per year",
                accentHex = 0xFFA78BFA // Purple
            )
        )
    }
}
