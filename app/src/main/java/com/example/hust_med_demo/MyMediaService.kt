package com.example.hust_med_demo

import androidx.media3.exoplayer.ExoPlayer // Trình phát nhạc
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession // Bộ kết nối với xe

class MyMediaService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        session = MediaLibrarySession.Builder(
            this,
            player, // đặt player vào đây
            AutomotiveLibraryCallback()
        ).build()
    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session
    }

    override fun onDestroy() {
        session.release()
        player.release()
        super.onDestroy()
    }

}
