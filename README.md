# Flavor

**Your personal kitchen companion.** Flavor is an Android app that helps you discover recipes, browse meal details with ingredients and steps, save favorites, and sign in with email or Google.

---

## Features

- **Auth** — Sign up and log in with email or Google; session handled with Firebase Auth.
- **Home** — Browse recipes by category, see a featured random meal, and search by name.
- **Meal details** — View full recipe: ingredients, step-by-step instructions, and optional YouTube video.
- **Favorites** — Save recipes locally with Room; list and manage them in a dedicated tab.
- **Profile** — User profile screen (after login).
- **Splash** — Animated splash screen (Lottie) before the app loads.

---

## Tech stack

| Layer | Technology |
|-------|------------|
| **UI** | Android Views, Material Design, ConstraintLayout, RecyclerView |
| **Architecture** | MVP-style (Presenters + Contracts) |
| **Networking** | Retrofit 2, RxJava2, Gson |
| **API** | [TheMealDB](https://www.themealdb.com/) (random, by category, search, lookup) |
| **Auth** | Firebase Auth, Google Sign-In, Credentials API |
| **Database** | Room (favorites), RxJava2 for async |
| **Images** | Glide |
| **Other** | Lottie (splash), Android YouTube Player, Core Splash Screen |

- **Language:** Java  
- **Build:** Gradle (Kotlin DSL), AGP 9.x  
- **Min SDK:** 24 · **Target SDK:** 36 · **Java:** 11  

---

## Prerequisites

- [Android Studio](https://developer.android.com/studio) (recommended; or CLI with SDK 36)
- JDK 11
- A [Firebase](https://console.firebase.google.com/) project with:
  - Authentication (Email/Password and Google) enabled
  - `google-services.json` added to the `app/` folder

---

## Setup

1. **Clone the repo**
   ```bash
   git clone <repository-url>
   cd Flavor
   ```

2. **Add Firebase config**
   - Create or use an existing Firebase project and add an Android app with package `com.example.flavor`.
   - Download `google-services.json` and place it in the **`app/`** directory (same level as `app/build.gradle.kts`).

3. **Open in Android Studio**
   - Open the project; Gradle will sync and download dependencies.

4. **Run**
   - Select a device or emulator (API 24+) and run the **app** configuration.

---

## Project structure (high level)

```
app/src/main/
├── java/com/example/flavor/
│   ├── core/storage/          # PrefsManager, etc.
│   ├── data/
│   │   ├── local/             # Room DB, FavoriteRecipe entity/DAO
│   │   ├── remote/            # Retrofit client, MealApiService
│   │   ├── model/             # Recipe, Meal, Category, User, etc.
│   │   └── repo/              # MealRepository, CategoryRepository, FavoriteRepository, AuthRepository
│   └── presentation/
│       ├── auth/              # Login, SignUp (Activity + Presenter + Contract)
│       ├── splash/            # SplashActivity
│       ├── main/              # MainActivity, Home/Favorites/Profile fragments + presenters
│       └── mealdetails/       # MealDetailsActivity, steps/ingredients adapters
├── res/                       # layouts, values, drawables, menu
└── AndroidManifest.xml
```

---

## License

This project is for educational/demo use. TheMealDB and other third-party services have their own terms of use.
