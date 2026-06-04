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

        // Hardcode fix: Nạp sẵn bài hát đầu tiên và chuẩn bị player
        // để ép hệ thống nhận diện đây là Active Media Source ngay khi chạy
        val defaultUri = androidx.media3.datasource.RawResourceDataSource.buildRawResourceUri(R.raw.kiep_ve_sau)
        val defaultItem = androidx.media3.common.MediaItem.Builder()
            .setMediaId("track_1")
            .setUri(defaultUri)
            .setMediaMetadata(
                androidx.media3.common.MediaMetadata.Builder()
                    .setTitle("Kiếp Ve Sầu")
                    .setArtist("Đan Trường")
                    .setIsPlayable(true)
                    .setIsBrowsable(false)
                    .build()
            )
            .build()
            
        player.setMediaItem(defaultItem)
        player.prepare()

        Log.d(TAG, "MediaLibrarySession created and player prepared with default track")
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
