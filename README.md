<div align="center">

# ⏳ Your Time

**A precision chronometer and temporal awareness instrument for Android.**

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Android-8.0%2B%20(API%2026%2B)-3DDC84.svg?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2F%20Material%203-4285F4.svg?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Offline First](https://img.shields.io/badge/Privacy-100%25%20Offline-10B981.svg)](https://github.com/vasusinguri/Your-Time)
[![F-Droid Ready](https://img.shields.io/badge/F--Droid-Ready-1976D2.svg?logo=f-droid&logoColor=white)](https://f-droid.org)

<br/>

> *"Most people measure life in years. Your Time reveals every second as it unfolds."*

</div>

---

## 📖 Overview

**Your Time** transforms the abstract concept of age into an active, living dashboard. Rather than presenting life as an arbitrary milestone like *"22 years old"*, it renders your exact temporal existence down to the second:

$$\mathbf{22\text{ Years} \cdot 11\text{ Months} \cdot 3\text{ Days} \cdot 7\text{ Hours} \cdot 24\text{ Minutes} \cdot 18\text{ Seconds}}$$

Engineered with a **Swiss-chronometer aesthetic**, deep OLED nocturnal contrast, and high-performance Compose optimizations, Your Time turns your lived journey into a reflective, mindful experience.

---

## ✨ Features

- **⏱️ Real-Time Precision Chronometer**  
  Live ticking display of your exact lived time broken down across Years, Months, Days, Hours, Minutes, and Seconds.

- **⚡ Zero-Lag Architecture (60–120 FPS)**  
  Built with `@androidx.compose.runtime.Immutable` state models, tabular monospaced numerals (`fontFeatureSettings = "tnum"`), and sub-composable skipping. Compose recomposes *only* the active ticking seconds card during runtime.

- **🎂 Next Birthday Countdown**  
  Live day, hour, minute, and second countdown to your next birthday milestone, with special celebratory animations on your birthday.

- **👥 Multi-Profile Management**  
  Easily add, switch, edit, and organize multiple profiles for family members, friends, or pets, each with custom relationship tags.

- **📱 Native Home Screen Widgets**  
  Android AppWidgets (`AppWidgetProvider`) delivering your live lived age and birthday countdown directly to your home screen with deep OLED styling.

- **🌌 Immersive Edge-to-Edge OLED Theme**  
  True obsidian background (`#030508`) with hairline borders (`#1B2437`), vibrant cyber cyan and emerald accents, and transparent system status bars.

- **🔒 100% Offline & Private**  
  Zero tracking, zero analytics, zero external network requests. All data is persisted locally and securely on-device using Android Jetpack DataStore Preferences.

---

## 🛠️ Technology Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.3+ | Coroutines, Flow, idiomatic null-safety |
| **UI Toolkit** | Jetpack Compose (BOM `2026.03.01`) | Declarative, reactive, hardware-accelerated UI |
| **Design System** | Material Design 3 (Material You) | Dynamic shapes, luxury chronometer typography |
| **Architecture** | MVI / Clean Architecture | Unidirectional Data Flow (UDF), immutable states |
| **State Management** | Jetpack ViewModel + StateFlow | Lifecycle-aware coroutine ticker |
| **Persistence** | Jetpack DataStore Preferences | Asynchronous, transactional local storage |
| **Serialization** | Kotlinx Serialization | Fast, type-safe JSON serialization |
| **Date & Time API** | Java Time (`java.time.*`) | Leap-year, month-length, and timezone-aware |
| **Target SDK** | Android 16 (API 36) | Min SDK: Android 8.0 (API 26) |

---

## 🏛️ Architecture

```
com.yourtime.app
├── data/
│   └── PreferencesManager.kt          # Jetpack DataStore local persistence
├── domain/
│   ├── AgeCalculator.kt               # Calendar-aware exact age engine
│   ├── NextBirthdayCalculator.kt      # Leap-year aware next birthday engine
│   ├── AgeResult.kt                   # Immutable chronometer data model
│   └── UserProfile.kt                 # Profile data model with serialization
├── ui/
│   ├── components/
│   │   ├── AgeDisplayCards.kt         # Precision chronometer display cards
│   │   ├── NextBirthdayCard.kt        # Birthday countdown card
│   │   ├── ProfileBar.kt              # Horizontal multi-profile selector
│   │   ├── AddEditProfileDialog.kt    # Profile creation/edit modal
│   │   └── AddWidgetDialog.kt         # In-app widget pinning helper
│   ├── theme/                         # Color tokens, Typography, Obsidian Theme
│   └── YourTimeScreen.kt              # Main edge-to-edge dashboard screen
├── viewmodel/
│   ├── YourTimeUiState.kt             # Immutable UI state definitions
│   └── YourTimeViewModel.kt           # Lifecycle-aware ticker & intent handlers
└── widget/
    ├── AgeWidgetProvider.kt           # Native Android Home Screen Widget
    └── YourTimeWidgetUpdater.kt       # Asynchronous widget broadcaster
```

---

## 🚀 Building from Source

### Prerequisites
- **JDK 17** (e.g. Eclipse Temurin 17)
- **Android SDK** (API 26+)
- **Git**

### Clone & Compile

```bash
# Clone the repository
git clone https://github.com/vasusinguri/Your-Time.git
cd "Your-Time"

# Run unit tests
./gradlew testDebugUnitTest

# Build Debug APK
./gradlew assembleDebug
```

The compiled APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 📥 Download

- **Pre-built APK**: Download the latest build directly from [GitHub Releases](https://github.com/vasusinguri/Your-Time/releases) or the root [`YourTime.apk`](./YourTime.apk).
- **F-Droid**: Coming soon to the official F-Droid repository.

---

## 🧪 Testing

The domain logic is backed by pure JUnit unit tests covering critical date/time edge cases:
- Leap years and February 29th leap-day birthdays
- Variable month lengths (30 vs 31 vs 28 days)
- Midnight hour and second turnover accuracy
- Future-date validation safeguards

Run tests locally:
```bash
./gradlew testDebugUnitTest
```

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](./LICENSE) file for details.

---

<div align="center">
<sub>Crafted with precision by <b>Vasu Singuri</b>. Every second counts.</sub>
</div>
