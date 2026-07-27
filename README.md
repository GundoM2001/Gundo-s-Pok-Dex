# Gundo's Pokédex

A modern Android application built with Jetpack Compose that organizes and locally saves a user's favorite Pokémon. It provides detailed information including base stats, abilities, and PokéDex descriptions across generations.

## ✨ Features
- **Browse Pokémon**: Explore a comprehensive list of Pokémon from the PokéAPI.
- **Search & Filter**: Quickly find Pokémon by name or ID.
- **Detailed View**: View base stats, abilities, types, and descriptions for every generation.
- **Favorites**: Save your favorite Pokémon locally for quick access.
- **Offline Support**: Favorited Pokémon are persisted locally using Room.

## 🛠 Tech Stack
- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Local Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)

## 🏗 Architecture
The project follows **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern:
- **Presentation Layer**: Contains Compose screens, ViewModels (using Hilt), and UI state management.
- **Domain Layer**: Contains business logic, use cases, and repository interfaces.
- **Data Layer**: Implements repository interfaces, manages local (Room) and remote (Retrofit) data sources.

## 📁 Project Structure
```
com.example.pokedexapp
├── data             # Repository implementation, Local & Remote data sources
├── di               # Hilt Dependency Injection modules
├── domain           # Business logic: Models, Repository interfaces, Use Cases
├── presentation     # UI: Features (List, Details, Favorites), Theme, Navigation
└── utils            # Helper classes and extensions
```

## 🚀 Getting Started
1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## 📡 API
This project uses the [PokéAPI](https://pokeapi.co/) for all Pokémon data.

---
*Developed by Gundo Mahatma Mukwevho*
