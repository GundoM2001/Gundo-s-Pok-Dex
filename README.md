# Gundo's Pokédex

A modern, feature-rich Android application built with Jetpack Compose that leverages the PokéAPI to provide a comprehensive Pokémon experience. From building competitive teams to exploring detailed Pokémon lore across generations, Gundo's Pokédex is designed for trainers of all levels.

## Features

### Explore and Search
- Comprehensive Pokédex: Browse through the entire Pokémon library with smooth pagination.
- Smart Search: Find any Pokémon instantly by name or ID number.
- Dynamic Backgrounds: UI themes dynamically adjust based on the primary type of the Pokémon being viewed.

### Team Builder (Advanced)
- Custom Team Creation: Build and manage multiple Pokémon teams locally.
- Deep Customization:
    - Nicknaming: Give your Pokémon a personal touch.
    - Level Scaling: Adjust levels (1-100) and see stats calculate in real-time.
    - Held Items: Search, select, and assign items to your Pokémon with detailed effect descriptions and icons.
    - Move Sets: Full access to learnsets (Level-up, TMs/HMs, Tutor, Evolution moves).
    - Nature Selection: Influence stat growth with the full range of Pokémon Natures.
    - EV/IV Training: Fine-tune stats with precise Effort Value (EV) and Individual Value (IV) controls.

### Detailed Lore and Stats
- Multi-Generation Entries: Read Pokédex flavor text from every game version the Pokémon has appeared in.
- Battle Stats: View base stats, type advantages/weaknesses, and ability details.
- Varieties and Forms: Easily switch between Mega Evolutions, Alolan forms, Galarian forms, and more.

### Personalization and Localization
- Global Support: Fully localized in 17 locales, including:
    - English (US and UK), Afrikaans, Venda, French, Italian, Japanese, German, Spanish (European and Latin), Portuguese, Arabic, Russian, Polish, Hindi, and Mandarin.
- Theme Control: Force Light Mode, Dark Mode, or follow the System Default.
- In-App Language Selection: Change the app language independently of the system (supports Android 13+ per-app language settings).

### Performance and Optimization
- Intelligent Caching: Uses a 10MB OkHttp disk cache and bounded LruCache for in-memory data to balance speed and memory usage.
- Optimized Rendering: Implements unique keys in lazy lists to ensure smooth scrolling and efficient UI updates.
- High-Performance Image Loading: Configured Coil with hardware-backed bitmaps and a shared network client to minimize memory footprint.

## Tech Stack

- UI Framework: Jetpack Compose (Material 3)
- Language: Kotlin
- Architecture: Clean Architecture with MVVM (Model-View-ViewModel)
- Dependency Injection: Hilt
- Local Database: Room (with schema versioning and migration support)
- Networking: Retrofit and OkHttp (with Disk Cache)
- Image Loading: Coil 3 (Optimized with hardware bitmaps)
- Reactive Programming: Kotlin Coroutines and StateFlow
- Navigation: Compose Navigation

## Architecture and Project Structure

The project is organized into layers to ensure scalability and testability:

```
com.example.pokedexapp
├── data             # Repositories, DAOs, Entities, and Remote API services
├── di               # Hilt Dependency Injection modules
├── domain           # Business logic: Models and Repository interfaces
├── presentation     # UI Layer
│   ├── components   # Shared UI components (Stat bars, Badges, etc.)
│   ├── feature      # Feature-based modules (Details, Team Builder, Settings)
│   ├── navigation   # NavHost and Route definitions
│   └── theme        # Material 3 Design System implementation
└── utils            # Formatters, Image helpers, and common utilities
```

## Getting Started

1. Clone the Repo: git clone https://github.com/yourusername/Gundo-s-Pok-Dex.git
2. Open in Android Studio: Use the latest Ladybug or newer version.
3. Sync and Run: Allow Gradle to sync and deploy to an API 26+ device.

## API Reference
This project utilizes the PokéAPI for all dynamic data.

---
Developed by Gundo Mahatma Mukwevho
