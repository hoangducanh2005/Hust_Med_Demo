# Hust Med Demo

**Hust Med Demo** is a specialized Android Automotive OS (AAOS) media playback application designed for in-vehicle infotainment (IVI) systems. This project demonstrates the integration of a media application with Android Automotive system services, providing seamless audio playback and control through the AAOS Media Center.

---

## 🎬 Demo Video
Check out the application in action:
[Watch Demo on YouTube](https://youtu.be/KfEvP0I6PiE)

---

## 🚀 Key Features
*   **Native AAOS Integration:** Local audio playback fully integrated with the Android Automotive emulator.
*   **Media Browsing:** Robust support for content discovery via `MediaLibraryService`.
*   **Intuitive Controls:** Full support for standard playback actions: Play, Pause, Skip, and Seek.
*   **System Synchronization:** Real-time synchronization of media metadata (Title, Artist, Album Art) with the system UI.
*   **Background Playback:** Reliable foreground media service to ensure uninterrupted audio.
*   **AAOS Media Center Compatibility:** Seamless interaction with the native Automotive Media Center.

---

## 🏗️ System Architecture

The application follows a modular architecture to ensure compatibility with AAOS requirements:

```text
       UI Layer (AAOS Media Center / App UI)
                 ↓
       Media Controller (System/App)
                 ↓
       MyMediaService (Foreground Service)
                 ↓
       MediaLibrarySession (Session Management)
                 ↓
       ExoPlayer (Playback Engine)
                 ↓
       Audio Output (Hardware/Emulator)
```

---

## ⚙️ Main Components

### 1. `MainActivity`
The primary entry point, providing the initial UI and setup for the application.

### 2. `MyMediaService`
The core engine of the app. It manages the lifecycle of the media session and controls the player instance.

### 3. `AutomotiveLibraryCallback`
Responsible for handling media browsing requests, resolving media IDs, and providing content to the AAOS Media Center.

### 4. `ExoPlayer`
The underlying playback engine used for high-performance audio processing.

### 5. `MediaLibrarySession`
The bridge that connects the application's internal playback state to the Android Automotive system-wide media controls.

---

## 🔄 Playback Workflow

1.  **Selection:** User selects a media item from the AAOS Launcher or the Media Center.
2.  **Request:** The system sends a playback request to `MyMediaService`.
3.  **Resolution:** `AutomotiveLibraryCallback` identifies and resolves the requested media metadata.
4.  **Loading:** `ExoPlayer` loads the audio resource and prepares for playback.
5.  **Sync:** Playback state and metadata are pushed to the AAOS system UI via the `MediaSession`.

---

## 🔐 Required Permissions

The application requires the following permissions to operate as a media service:

```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
```

---

## 🚗 Automotive Requirements

To ensure visibility in the Automotive ecosystem, the following feature is declared:

```xml
<uses-feature
    android:name="android.hardware.type.automotive"
    android:required="true" />
```

---

## 🧩 Technologies Used
*   **Language:** Kotlin
*   **Platform:** Android Automotive OS
*   **Media Framework:** AndroidX Media3
*   **Player:** ExoPlayer
*   **API:** MediaLibraryService & MediaSession API

---

## 📌 Project Purpose
This project was developed as a research and development effort focusing on:
*   Understanding Android Automotive media architecture.
*   Mastering Automotive service lifecycles.
*   Implementing complex Media Session communication.
*   Designing robust foreground services for vehicle environments.

---

## 🔮 Future Improvements
*   [ ] Dynamic playlist support.
*   [ ] Cloud-based media library integration.
*   [ ] Bluetooth audio source management.
*   [ ] Google Assistant / Voice control integration.
*   [ ] Driving distraction compliance (DO) enhancements.

---

## 👨‍💻 Author
**Hoang Duc Anh**  
HEDSPI - Hanoi University of Science and Technology
