# 🪙 Coin Quest — Kids Financial Adventure & Habit Tracker

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.0-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Room Database](https://img.shields.io/badge/Database-Room%20KSP-F88909.svg?style=flat&logo=sqlite)](https://developer.android.com/training/data-storage/room)
[![Biometric Authentication](https://img.shields.io/badge/Security-AndroidX%20Biometrics-00C853.svg?style=flat&logo=fingerprint)](https://developer.android.com/training/sign-in/biometric-auth)
[![API 24+](https://img.shields.io/badge/Min%20SDK-24-blue.svg)](https://developer.android.com/)

**Coin Quest** is an engaging, gamified life-adventure application designed to teach children lifelong financial literacy and positive daily habits. Through interactive gameplay, everyday parent-guided chores, multi-jar budgeting, compounding gardens, and entrepreneurial challenges, kids learn to earn, save, invest, spend wisely, and give back.

---

## 🌟 Key Features

### 🏰 1. Village Hub (Home Dashboard)
- **Kid Character Profile**: Real-time Level, XP, Level Title (e.g., Novice Saver, Coin Wizard), and total pouch balance.
- **Multi-Child Switcher**: Easily switch between child profiles on the fly.
- **Active Goal Progress**: Direct glance at the current savings dream goal and percentage achieved.
- **Adventure Portals**: One-tap access to all activities and learning modules.

### 📋 2. Adventures & Daily Habits (Earn & Chores)
- **Customizable Daily Habits**: Brush teeth, complete homework, make bed, read books, clean room, and practice instruments.
- **Approval Flow**: Tasks can be verified immediately or queued for parent approval.
- **Streak Tracking & Streaks Shield**: Encourages consistency and accountability.
- **Recovery Quests**: Fun bounce-back challenges when a habit is missed to regain streaks.

### 🏺 3. Magic Jars (4-Jar Smart Money System)
- **Spend Jar**: For daily wishes and fun purchases.
- **Save Jar**: Allocated toward long-term Dream Goals.
- **Give Jar**: Dedicated to donating and community charity projects.
- **Safety Jar**: The emergency fund shield teaching kids resilience against unexpected events.
- **Interactive Distribution**: Visual sliders to allocate newly earned coins across jars.

### 🌱 4. Compounding Garden & Investment Simulator
- **Seed Planting**: Plant trees with different growth cycles and returns (e.g., *Oak of Patience*, *Golden Apple of Dividends*, *Bamboo of Growth*).
- **Daily Care**: Water plants to nurture growth stages.
- **Harvesting & Compounding**: Experience compound interest firsthand—reinvest harvested yield to multiply earnings over time.

### 📈 5. Invest SIP (Systematic Investment Plan)
- **Pocket-Money SIP**: Automated periodic coin savings allocation.
- **Growth Projections**: Visual graphs demonstrating compounding returns over time.
- **Risk & Growth Lessons**: Kid-friendly explanations of how investing builds long-term wealth.

### 🏦 6. Benny's Bank
- **Savings Vault**: Deposit coins to earn simulated interest over time.
- **Withdrawals & Balances**: Teaches banking basics, interest calculation, and patience.

### 🎯 7. Dream Goals
- **Visual Wishlist**: Set targets for toys, books, bicycles, or experiences.
- **Milestone Tracking**: Direct allocation from the Save Jar with celebration animations upon goal attainment.

### 🛍️ 8. Market & Custom Toy Shop
- **Smart Choices vs. Impulse Spending**: Interactive scenarios comparing durable vs. temporary goods.
- **Custom Toy Registry**: Kids and parents can add real-world items with price, description, and alternative suggestions.

### 🧠 9. Skill School & Age Skills
- **Craft & Entrepreneurship**: Hands-on projects categorized by age (crafting, lemonades, digital skills, coding, art).
- **Material Costs & Profits**: Calculate craft expenses versus selling price to understand net profit.

### 🧩 10. Daily Quiz & Puzzle Arcade
- **Financial IQ Quiz**: Daily questions on Needs vs. Wants, Compound Interest, Opportunity Cost, and Scams.
- **Mini-Games**:
  - *Needs vs. Wants Sorter*: Quick categorization game.
  - *Coin Catch & Budget Match*: Fun arcade interactions rewarding bonus coins.

### 🎲 11. Life Adventures
- **Life Event Cards**: Random real-life scenario simulations (e.g., bicycle flat tire, lost wallet, pet vet visit, garage sale).
- **Critical Decision Making**: Tests emergency safety funds and consequence evaluation.

### 🛡️ 12. Parent Dashboard & Parental Controls
- **Biometric Fingerprint Lock**: Native Android `BiometricPrompt` support with automatic prompt and hardware fingerprint reader integration.
- **Fallback Security**: Parent PIN (default: `1234`) and arithmetic verification gates for devices without biometric hardware.
- **One-Touch "Approve All"**: Instantly approve all pending habit logs in one tap without intrusive pop-up dialogs.
- **"Give Points ⭐"**: Conveniently award bonus coins with encouragement notes and custom presets (+2, +5, +10, +20, +50).
- **Coin Adjustments & Revocation**: Deduct coins with reason logging for rule violations or negative behavior.
- **Multi-Child Management**: Add, edit, or remove child profiles and customize daily chore rewards.
- **Analytics & History**: Review habit streaks, spending trends, quiz history, and jar distribution charts.

---

## 🏗️ Architecture & Tech Stack

The application follows modern Android development practices, MVVM (Model-View-ViewModel) architecture, and unidirectional data flow (UDF).

```
                      ┌─────────────────────────────────┐
                      │    Jetpack Compose UI Layer     │
                      │  (MainActivity, Screens, Theme) │
                      └────────────────┬────────────────┘
                                       │ StateFlow / Events
                                       ▼
                      ┌─────────────────────────────────┐
                      │         GameViewModel           │
                      │  (UI State, Business Logic)     │
                      └────────────────┬────────────────┘
                                       │ Coroutines / Flow
                                       ▼
                      ┌─────────────────────────────────┐
                      │         GameRepository          │
                      │   (Data Coordination & Cache)   │
                      └────────────────┬────────────────┘
                                       │ DAO calls
                                       ▼
                      ┌─────────────────────────────────┐
                      │      Room Local Database        │
                      │    (Offline-first SQLite)       │
                      └─────────────────────────────────┘
```

| Layer | Technologies |
| :--- | :--- |
| **Language** | Kotlin 2.0 (Coroutines, Flow, StateFlow) |
| **UI Framework** | Jetpack Compose with Material Design 3 (M3) |
| **Architecture** | MVVM + Repository Pattern |
| **Local Persistence** | Room Database with KSP (Kotlin Symbol Processing) |
| **Security & Auth** | `androidx.biometric:biometric` (`BiometricPrompt` & `BiometricManager`) |
| **Navigation** | State-driven Compose Navigation with animated transitions |
| **Build Tool** | Gradle Kotlin DSL (`build.gradle.kts`), Version Catalog (`libs.versions.toml`) |
| **Target SDK** | Android 14+ (compileSdk 36, minSdk 24, targetSdk 36) |

---

## 📁 Project Directory Structure

```text
├── app/
│   ├── build.gradle.kts                # Module-level Gradle build file
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml     # App permissions (Biometric, Internet)
│       │   ├── java/com/example/
│       │   │   ├── MainActivity.kt     # App entry point & screen navigation router
│       │   │   ├── data/
│       │   │   │   ├── local/          # Room Entities, DAOs & MoneyAdventureDatabase
│       │   │   │   ├── model/          # Domain data models & enums
│       │   │   │   └── repository/     # GameRepository data management
│       │   │   └── ui/
│       │   │       ├── components/     # Reusable UI widgets, bars, dialogs
│       │   │       ├── screens/        # All 17 Composable game and parent screens
│       │   │       ├── theme/          # Material 3 colors, typography, shapes
│       │   │       └── viewmodel/      # GameViewModel & GameUiState
│       │   └── res/                    # Drawables, strings, mipmaps, XML resources
│       └── test/                       # Unit tests & Robolectric test suites
├── gradle/
│   └── libs.versions.toml              # Version catalog for dependencies and plugins
├── build.gradle.kts                    # Root build configuration
├── settings.gradle.kts                 # Project settings & dependency resolution
├── metadata.json                       # AI Studio project metadata
└── README.md                           # Project documentation (this file)
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug / Jellyfish or newer (recommended)
- **JDK**: Java 11 or Java 17
- **Android SDK**: API 34+ installed
- **Device / Emulator**: Android 7.0 (API 24) or higher (Biometric fingerprint sensor recommended for parent lock)

### Building and Running the App

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd coin-quest
   ```

2. **Open in Android Studio**:
   - Launch Android Studio and choose **Open an Existing Project**.
   - Select the root project directory.
   - Allow Gradle to sync dependencies.

3. **Build the Debug APK**:
   ```bash
   gradle assembleDebug
   ```

4. **Run Unit Tests**:
   ```bash
   gradle :app:testDebugUnitTest
   ```

5. **Deploy to Device**:
   - Connect an Android device with USB Debugging enabled, or start an Android Virtual Device (AVD).
   - Click the green **Run** button (or press `Shift + F10`) in Android Studio.

---

## 🔒 Security & Privacy

- **Offline-First Storage**: All child profiles, coins, goals, and habit histories are stored locally on the device using encrypted SQLite via Room. No personal data is transmitted without consent.
- **Biometric Security**:
  - The Parent Zone utilizes `BiometricPrompt` configured with `BIOMETRIC_STRONG` and `DEVICE_CREDENTIAL`.
  - Fallback authentication is provided via a secure 4-digit Parent PIN (default: `1234`) and randomized arithmetic challenges.
- **Non-Destructive Actions**: Sensitive actions such as deleting child profiles or revoking coins require explicit parent authentication and confirmation.

---

## 👨‍👩‍👧‍👦 Parent Quick Guide

1. **Accessing the Parent Dashboard**:
   - Tap the **Parent Gate (Shield Icon)** in the top app bar of the Village Hub.
   - Authenticate using your fingerprint, device biometrics, or enter the default PIN (`1234`).
2. **Approving Habits**:
   - View pending tasks under **Pending Habit Approvals**.
   - Tap **Approve All** to approve all submissions in one tap with zero pop-up delays, or approve individual tasks.
3. **Awarding Bonus Coins**:
   - Tap **Give Points ⭐** in the dashboard or **+ Points** on any child's card.
   - Choose a preset (+2, +5, +10, +20) or enter a custom amount with an encouraging message.
4. **Adding New Chores**:
   - Go to **Manage Chores & Habits** in the Parent Dashboard.
   - Define custom habits, reward coin values, categories, and approval requirements.

---

## 📄 License

This project is licensed under the Apache License 2.0. See the `LICENSE` file for more information.
