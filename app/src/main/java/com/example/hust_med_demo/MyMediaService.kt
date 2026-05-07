package com.example.hust_med_demo

import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession

private const val TAG = "HUST_VOICE"

class MyMediaService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private var session: MediaLibrarySession? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MyMediaService onCreate() called")

        player = ExoPlayer.Builder(this).build()

        session = MediaLibrarySession.Builder(
            this,
            player,
            AutomotiveLibraryCallback()
        ).build()

        Log.d(TAG, "MediaLibrarySession created")
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        Log.d(
            TAG,
            "onGetSession() from package=${controllerInfo.packageName}, uid=${controllerInfo.uid}"
        )
        return session
    }

    override fun onDestroy() {
        Log.d(TAG, "MyMediaService onDestroy() called")
        session?.release()
        session = null
        player.release()
        super.onDestroy()
    }
}
