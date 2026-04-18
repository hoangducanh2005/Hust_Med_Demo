# HUST_MED_DEMO

Android Automotive OS media playback application built for in-vehicle infotainment systems.
This project demonstrates how a media application integrates with Android Automotive system services to provide audio playback through AAOS media center.

---

## 🎬 Demo Video

👉 **[Watch HUST_MED_DEMO in action](https://youtu.be/KfEvP0I6PiE)**

---

## 🚀 Features

* Local audio playback inside Android Automotive emulator
* Media browsing support with `MediaLibraryService`
* Playback control: play / pause / skip
* Media metadata synchronization with system UI
* Foreground media playback service
* Integration with Android Automotive media center

---

## 🏗️ System Architecture

```text
UI Layer
   ↓
Media Controller
   ↓
MyMediaService
   ↓
MediaLibrarySession
   ↓
ExoPlayer
   ↓
Audio Output
```

---

## ⚙️ Main Components

### `MainActivity`

Entry point of the application.

### `MyMediaService`

Core media playback service responsible for session lifecycle and player management.

### `AutomotiveLibraryCallback`

Handles media browsing requests and resolves media items.

### `ExoPlayer`

Audio playback engine.

### `MediaLibrarySession`

Connects app playback state to Android Automotive system media controls.

---

## 🔄 Playback Workflow

1. User selects media from AAOS launcher or media center
2. Request is sent to `MyMediaService`
3. `AutomotiveLibraryCallback` returns requested media item
4. ExoPlayer loads audio resource
5. Playback state is synchronized with AAOS system UI

---

## 🔐 Required Permissions

```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK"/>
```

---

## 🚗 Automotive Requirement

```xml
<uses-feature
    android:name="android.hardware.type.automotive"
    android:required="true"/>
```

---

## 🧩 Technologies Used

* Kotlin
* Android Automotive OS
* AndroidX Media3
* ExoPlayer
* MediaLibraryService
* MediaSession API

---

## 📌 Project Purpose

This project is developed for studying:

* Android Automotive media architecture
* Automotive service lifecycle
* Media session communication
* Foreground playback service design

---

## 🔮 Future Improvements

* Playlist support
* Dynamic media library
* Bluetooth source integration
* Voice assistant control
* Driving distraction compliance enhancement

---

## 👨‍💻 Author

Hoang Duc Anh
HEDSPI - Hanoi University of Science and Technology
