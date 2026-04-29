# City Scout Android

City Scout Android is a Jetpack Compose weather app focused on searching for cities and viewing current weather plus multi-day forecast details.

It is built as a modular Android project, with a lightweight app module and a UI module backed by a shared Kotlin library.

## Features

- Search for cities by name
- View current weather details for a selected city
- View forecast cards for upcoming days
- Toggle temperature display format
- Adaptive list-detail layout for phones and larger screens

## Project Structure

- `app`
  - Android entry point, dependency wiring, and secrets-to-build config mapping
  - Hosts `MainActivity` and Hilt module setup
- `ui`
  - Compose screens, previews, and UI-specific Hilt wrappers
  - Main screens:
    - `MainView`
    - `SearchCityView`
    - `CityWeatherView`

## Architecture Overview

- **UI framework:** Jetpack Compose
- **Navigation pattern:** Material 3 Adaptive `ListDetailPaneScaffold`
- **Dependency injection:** Hilt
- **Domain/data provider:** `cityscoutshared` library (`com.github.MarwanAziz.CityScoutShared:sharedLib-android`)

Current flow:
1. `MainActivity` sets Compose content to `MainView`.
2. `MainView` hosts adaptive list/detail panes and back handling.
3. `SearchCityView` performs city search and emits selected city.
4. `CityWeatherView` fetches and renders weather for the selected city.

## Libraries Used

### Android / UI

- **Jetpack Compose UI + Material3**
  - Build declarative screens and components.
- **Material3 Adaptive**
  - Provides list-detail scaffold behavior across form factors.
- **Material Icons Extended**
  - Used for weather/stat visual affordances.

### Dependency Injection

- **Hilt (`com.google.dagger:hilt-android`)**
  - Provides app-wide object graph and screen dependencies.
- **KSP**
  - Generates Hilt code at build time.

### Image Loading

- **Coil 3 (`coil-compose`, `coil-network-okhttp`)**
  - Loads weather condition icons efficiently in Compose.

### Shared Business Layer

- **CityScoutShared (`sharedLib-android`)**
  - Supplies remote access, models, and shared view-model contracts consumed by this app.

## Requirements

- Android Studio (recent stable release recommended)
- JDK 11
- Android SDK with:
  - `compileSdk = 36`
  - `minSdk = 24`
  - `targetSdk = 36`

## Setup

1. Clone the repository.
2. Create or update `local.properties` in the project root.
3. Add the required API keys:

```properties
RAPID_API_KEY=your_rapid_api_key
WEATHER_API_KEY=your_weather_api_key
```

4. Sync Gradle.
5. Run the app from Android Studio, or via:

```bash
./gradlew :app:installDebug
```

## Build and Verification

- Compile UI module:

```bash
./gradlew :ui:compileDebugKotlin
```

- Build debug app:

```bash
./gradlew :app:assembleDebug
```

- Run tests:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Guidance for New Maintainers

### Where to start

- Entry point: `app/src/main/java/net/marwanaziz/cityscout/MainActivity.kt`
- Dependency graph: `app/src/main/java/net/marwanaziz/cityscout/di/CityScoutModule.kt`
- Primary UI orchestration: `ui/src/main/java/net/marwanaziz/ui/MainView.kt`

### Conventions in this codebase

- Keep runtime screen code separate from preview/mock code.
- Keep UI strings in resources, not hardcoded in composables.
- Prefer small focused composables over very large screen files.
- Preserve adaptive behavior in `MainView` when changing navigation/state logic.

### Working with the shared library

- Most weather/search logic comes from `cityscoutshared`.
- Android code here is mainly responsible for:
  - UI composition
  - State collection/rendering
  - Wiring dependencies
- If you need to change business logic, you may need changes in the shared library repository and then update its version in `gradle/libs.versions.toml`.

### Dependency updates

- Centralized versions live in `gradle/libs.versions.toml`.
- Prefer updating versions there rather than hardcoding versions in module build files.

## Security Notes

- Never commit real API keys.
- Keep secrets in local environment files (`local.properties`) or secure CI secret storage.
- Review any logging changes to avoid exposing sensitive values.

## Roadmap Ideas

- Add explicit UI state models for each screen to reduce scattered state collection.
- Expand automated UI tests for list-detail and rotation behaviors.
- Improve error and empty states for search and weather views.

