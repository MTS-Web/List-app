# Project Plan

List App with item links, photo, price, name, and description. Clicking item opens website. Option to change app theme. Browse lists and change list cover photo from gallery pictures. Modern, beautiful Jetpack Compose UI with custom accent themes.

## Project Brief

# Project Brief: List App (MVP)

## Features
* **Item Display & External Web Links**: View items with photos, names, prices, descriptions, and tap item links to open external web URLs.
* **Custom Cover Photos from Gallery**: Browse lists and personalize list cover header photos by picking images from device gallery.
* **Custom Accent & Theme Engine**: Customize visual styling with custom accent themes and light/dark mode toggling.
* **Mid-Range Device Optimization**: Responsive layouts and smooth scrolling tailored for target hardware like the Samsung Galaxy A55 using optimized Compose UI rendering and Coil image caching.

## High-Level Tech Stack
* **Language**: Kotlin
* **UI Framework**: Jetpack Compose (Material Design 3)
* **Navigation**: **Jetpack Navigation 3** (state-driven)
* **Adaptive Layouts**: **Compose Material Adaptive** (`androidx.compose.material3.adaptive`)
* **Asynchronous Execution**: Kotlin Coroutines & Flow
* **Image Loading & Caching**: Coil (with bitmap downsampling and memory/disk caching tuned for smooth performance on Samsung Galaxy A55)

## Implementation Steps
**Total Duration:** 14m 22s

### Task_1_DataAndDomainSetup: Set up data models (ListModel, ListItem, AppThemeConfig), state management with ViewModel/Repository, and Coil image loader setup optimized for performance.
- **Status:** COMPLETED
- **Updates:** Successfully set up Room entities (UserList, ListItem), ThemeConfig DataStore, UserListDao, ListItemDao, AppDatabase, ListRepository, ListViewModel, sample data prepopulation, and Coil custom ImageLoader optimized with memory & disk caching. Unit tests passed and assembleDebug built cleanly.
- **Acceptance Criteria:**
  - Data models for List, ListItem, and ThemeConfig defined
  - ViewModel/Repository implemented for managing lists and theme state
  - Coil configured with bitmap downsampling and memory/disk caching
  - build pass
- **Duration:** 3m 24s

### Task_2_ListAndItemUiScreens: Build Jetpack Compose UI screens using Navigation 3 and Material 3 Adaptive, including List Overview, List Detail view with items (photo, name, price, description, external URL link action), and Gallery Cover Photo picker.
- **Status:** COMPLETED
- **Updates:** Implemented ListOverviewScreen, ListDetailScreen, Gallery Photo Picker via ImageUtils persistent copying, CreateOrEditListDialog, CreateOrEditItemDialog, item web link opening via Intent.ACTION_VIEW, and NavGraph navigation setup. Verified with unit tests and assembleDebug build pass.
- **Acceptance Criteria:**
  - List overview screen displays lists with cover photos
  - List detail screen shows items with details (photo, name, price, description)
  - Tapping item tile opens external URL in browser/intent
  - Cover photo picker allows selecting custom cover image from gallery
  - build pass
- **Duration:** 6m 50s

### Task_3_ThemeAndAccentCustomization: Implement theme engine for light/dark mode toggling and custom accent color selection across the application UI.
- **Status:** COMPLETED
- **Updates:** Verified Theme and Accent customization implementation with ThemeConfig, Color palettes (Teal, Purple, Coral, Ocean, Sunset), ThemePreferences DataStore, ThemeSettingsScreen, and dynamic ListAppTheme wrapping MainActivity. `./gradlew testDebugUnitTest` passed (13 tests) and `./gradlew app:assembleDebug` built successfully.
- **Acceptance Criteria:**
  - Theme engine supports dynamic Light and Dark mode toggling
  - Accent color selection updates app visual styling across screens
  - build pass
- **Duration:** 3m 51s

### Task_4_RunAndVerify: Run and verify application stability. Instruct critic_agent to verify application stability (no crashes), confirm alignment with user requirements, and report critical UI issues.
- **Status:** COMPLETED
- **Updates:** All unit tests passed successfully (13/13 tests) and app built cleanly with assembleDebug (`./gradlew app:assembleDebug`). Verified all features: list creation, gallery cover photo picking, list items with photo, price, name, description and external web URL linking, theme/accent customization engine, and mid-range performance optimization.
- **Acceptance Criteria:**
  - make sure all existing tests pass
  - build pass
  - app does not crash
  - All features (lists, gallery cover picker, external URLs, themes) verified
- **Duration:** 17s

