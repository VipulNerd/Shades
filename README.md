# Shades - Android App

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-orange?style=flat-square)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-blue?style=flat-square)
![Firebase](https://img.shields.io/badge/Firebase-Backend-yellow?style=flat-square)
![Build Status](https://img.shields.io/github/actions/workflow/status/<USERNAME>/<REPO>/android.yml?branch=main&style=flat-square)
![Demo](https://img.shields.io/badge/Demo-GIF-red?style=flat-square)

## Overview
Shades is a social communication and media sharing Android application built using **Jetpack Compose**. The app provides a dark-themed interface with a red accent color, supporting user authentication, chat functionality, and content posting.

### Features
- **Authentication:** User sign-up and login using Firebase Authentication.
- **Chat:** Real-time one-to-one chat powered by Firebase Firestore.
- **Posts:** Users can post text, images, videos, and audio.
- **Bottom Navigation Bar:** Navigate between Home, Chat, Post, and Logout.
- **Theming:** Dark mode with red accents for a modern and appealing look.
- **Password Toggle:** Users can show/hide passwords in login/sign-up screens.
- **UI/UX:** Material 3 theming with polished cards and chat bubbles.

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Backend:** Firebase Authentication & Firestore
- **Image Loading:** Coil Compose

## Project Structure
- `authentication/`: Login and SignUp screens, AuthViewModel.
- `chatRoom/`: ChatListScreen and ChatRoomScreen, real-time messaging functionality.
- `cards/`: PostCard and PostScreen for media posts.
- `MyAppNavigation/`: Navigation and BottomNavigationBar components.
- `ui/theme/`: App theming, color palette, and typography.

## How to Run
1. Clone the repository.
2. Open in Android Studio (preferably latest stable version).
3. Add your Firebase configuration (`google-services.json`) to `app/`.
4. Sync Gradle and build the project.
5. Run on an Android device or emulator with Android 12+.

## Screenshots / Demo
![Demo GIF](https://link-to-your-demo.gif)

## Future Improvements
- Group chat functionality.
- Media preview and playback in posts.
- Push notifications for new messages.
- Dark/Light theme toggle.
- Profile customization and avatars.

## License
