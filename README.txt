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