# Your Time ⏳

> **Know your time. Feel its value. Live it.**

**Your Time** is a native Android application that calculates and displays exactly how long you have been alive based on your date and time of birth.

Instead of showing age as just **22 years old**, Your Time breaks it down into a live, continuously updating experience:

**22 Years · 11 Months · 3 Days · 7 Hours · 24 Minutes · 18 Seconds**

Every second changes.

The goal is simple: **make the passage of time visible.**

---

## 🎯 Project Goal

Most people think about their age in years:

> "I'm 22."

Your Time presents a different perspective:

> "I've been alive for 22 years, 11 months, 3 days, 7 hours, 24 minutes and 18 seconds."

The application is designed to create a stronger awareness of time by showing that life is not just measured in years, but in **months, days, hours, minutes and seconds**.

The visual design uses a dark, cinematic interface combined with warm accent colors, subtle imagery and time-related icons to create a feeling of urgency, reflection and appreciation.

The intention is not to frighten the user, but to encourage a simple thought:

> **Every second is part of your life.**

---

## ✨ Features

### Live Age Counter

Displays the user's current age in:

* Years
* Months
* Days
* Hours
* Minutes
* Seconds

The counter continuously updates using the device's current time.

### 📅 Date & Time of Birth

Users can enter their exact:

* Date of birth
* Time of birth

### 💾 Local Storage

Birth information is stored locally on the device so users do not need to enter it every time they open the application.

### 🌐 Completely Offline

Your Time does not require:

* Internet
* Backend servers
* User accounts
* Cloud databases

The calculation happens directly on the device.

### 🌙 Modern Interface

The UI is designed around:

* Dark visual styling
* Warm time-inspired accent colors
* Large numerical typography
* Minimal icons
* Strong visual hierarchy
* Responsive Android layouts
* Light and dark theme support

### 🔒 Privacy-Focused

Your date and time of birth remain on your device.

No birth information is sent to a remote server in Version 1.

---

## 🧠 How It Works

The application takes two inputs:

```text
Date of Birth
+
Time of Birth
```

It then compares them with the device's current date and time.

Conceptually:

```text
Birth Date & Time
        ↓
Current Date & Time
        ↓
Calendar-aware calculation
        ↓
Years
Months
Days
Hours
Minutes
Seconds
        ↓
Live Android UI
```

The application uses calendar-aware date/time calculations rather than treating every month or year as a fixed number of seconds.

This allows the application to correctly account for things such as:

* Different month lengths
* Leap years
* February 29
* Year transitions
* Month transitions
* Day transitions
* Exact time differences

---

## 🛠️ Technology Stack

**Platform:** Android

**Language:** Kotlin

**UI:** Jetpack Compose

**Design:** Material 3

**Build System:** Gradle

**Date & Time:** Kotlin/Java `java.time` APIs

**Local Storage:** DataStore Preferences

**Architecture:** Simple separation of UI, state, business logic and local data

**IDE:** Antigravity IDE

---

## 🏗️ Architecture

The project keeps the major responsibilities separated:

```text
Your Time
│
├── UI
│   └── Jetpack Compose
│
├── ViewModel
│   └── Application state
│
├── Domain
│   └── Age calculation engine
│
└── Data
    └── Local birth information
```

The calculation engine is kept independent from the UI so it can be tested separately.

---

## 🧪 Testing

The project should include tests for important date/time edge cases, including:

* Normal dates
* Leap years
* February 29 birthdays
* Different month lengths
* Midnight transitions
* Month transitions
* Year transitions
* Future birth dates
* Exact second/minute/hour differences

The goal is to ensure that the displayed age is based on correct calendar and time calculations rather than approximate arithmetic.

---

## 📱 Installation

This project is initially intended for personal Android use and does not require Google Play Store distribution.

The application can be built into an APK:

```text
Android Project
      ↓
Gradle Build
      ↓
APK
      ↓
Transfer to Android Phone
      ↓
Install
```

For development, a debug APK can be generated using the project's Gradle wrapper.

Example:

```bash
./gradlew assembleDebug
```

The generated APK will typically be located under:

```text
app/build/outputs/apk/debug/
```

A release APK can be generated after release signing is configured.

---

## 🚀 Development Workflow

```text
Plan
 ↓
Build UI
 ↓
Implement age calculation
 ↓
Add live counter
 ↓
Add local storage
 ↓
Test
 ↓
Build APK
 ↓
Install on Android device
 ↓
Test on real hardware
 ↓
Polish
```

The project is intentionally being developed in small phases rather than generating the entire application blindly.

---

## 🎨 Design Philosophy

Your Time is built around one central idea:

### Make time visible.

The interface intentionally avoids the appearance of a conventional calculator.

Instead, it uses:

* Large numbers to emphasize the current age
* Dark backgrounds to create visual depth
* Warm accent colors to represent passing time
* Hourglass/time-related imagery
* Minimal icons
* Strong typography
* Subtle visual transitions

The design should make the user pause for a moment when they open the application.

---

## 🔮 Future Possibilities

Potential future features include:

* Total days lived
* Total hours lived
* Total minutes lived
* Total seconds lived
* Next birthday countdown
* Life milestones
* Personal time statistics
* Multiple profiles
* Widgets
* Home-screen live age widget
* More advanced visualizations
* Optional Play Store release

These features are intentionally outside the initial Version 1 scope.

---

## 📌 Project Status

**Status:** Early Development

The current objective is to build and validate the core Android experience, generate an installable APK, and test it on a physical Android device.

---

## 📄 License

Add an appropriate open-source license here once the project's distribution model has been decided.

---

## 👤 About

**Your Time** is an experimental Android project built to explore native Android development, date/time computation, UI/UX design and the process of taking an application from an idea to a working APK.

The project is intentionally simple in functionality, but its design goal is deeper:

> **Don't just know how old you are. Know how much time you've already lived.**
