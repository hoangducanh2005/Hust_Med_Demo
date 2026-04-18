# HUST_MED_DEMO

Android Automotive OS media playback application built for in-vehicle infotainment systems.  
This project demonstrates how a media application integrates with Android Automotive system services to provide audio playback through the AAOS Media Center.

---

## 🎬 Demo Video

<p align="center">
  <a href="https://youtu.be/KfEvP0I6PiE" target="_blank">
    <img src="https://img.shields.io/badge/Watch-Demo%20Video-red?style=for-the-badge&logo=youtube" alt="Watch Demo Video" />
  </a>
</p>

---

## 🚀 Features

- Local audio playback inside the Android Automotive emulator
- Media browsing support with `MediaLibraryService`
- Playback controls: play / pause / skip
- Media metadata synchronization with system UI
- Foreground media playback service
- Integration with Android Automotive Media Center

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

⚙️ Main Components
MainActivity

Entry point of the application.

MyMediaService

Core media playback service responsible for session lifecycle and player management.

AutomotiveLibraryCallback

Handles media browsing requests and resolves media items.

ExoPlayer

Audio playback engine.

MediaLibrarySession

Connects the app playback state to Android Automotive system media controls.

🔄 Playback Workflow
User selects media from the AAOS launcher or Media Center
Request is sent to MyMediaService
AutomotiveLibraryCallback resolves and returns the requested media item
ExoPlayer loads the audio resource
Playback state is synchronized with the AAOS system UI
🔐 Required Permissions
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
🚗 Automotive Requirement
<uses-feature
    android:name="android.hardware.type.automotive"
    android:required="true" />
🧩 Technologies Used
Kotlin
Android Automotive OS
AndroidX Media3
ExoPlayer
MediaLibraryService
MediaSession API
📌 Project Purpose

This project was developed for studying:

Android Automotive media architecture
Automotive service lifecycle
Media session communication
Foreground playback service design
🔮 Future Improvements
Playlist support
Dynamic media library
Bluetooth source integration
Voice assistant control
Driving distraction compliance enhancement
👨‍💻 Author

Hoang Duc Anh
HEDSPI - Hanoi University of Science and Technology