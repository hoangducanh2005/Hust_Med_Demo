package com.example.hust_med_demo

import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors

private const val TAG = "HUST_VOICE"

class VoiceTestActivity : AppCompatActivity() {

    private var controller: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "VoiceTestActivity onCreate(), action=${intent?.action}")
        initController()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        Log.d(TAG, "VoiceTestActivity onNewIntent(), action=${intent.action}")
        handleSearchIntent(intent)
    }

    private fun initController() {
        val sessionToken = SessionToken(this, ComponentName(this, MyMediaService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        controllerFuture.addListener({
            controller = controllerFuture.get()
            Log.d(TAG, "MediaController connected to MyMediaService")
            handleSearchIntent(intent)
        }, MoreExecutors.directExecutor())
    }

    private fun handleSearchIntent(intent: Intent?) {
        if (intent == null) {
            Log.d(TAG, "handleSearchIntent(): intent is null")
            return
        }

        val isPlayFromSearch = intent.action == MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
        val isSearch = intent.action == Intent.ACTION_SEARCH

        Log.d(TAG, "handleSearchIntent(): action=${intent.action}, extras=${intent.extras}")

        if (!isPlayFromSearch && !isSearch) {
            Log.d(TAG, "Not a media search intent, ignore")
            return
        }

        val query = intent.getStringExtra(SearchManager.QUERY)
            ?: intent.getStringExtra(MediaStore.EXTRA_MEDIA_TITLE)
            ?: ""

        Log.d(TAG, "VoiceTestActivity received query=[$query]")

        val item = MediaItem.Builder()
            .setMediaId("voice_request")
            .setRequestMetadata(
                MediaItem.RequestMetadata.Builder()
                    .setSearchQuery(query)
                    .build()
            )
            .build()

        val mediaController = controller
        if (mediaController == null) {
            Log.d(TAG, "Controller not ready yet")
            return
        }

        mediaController.setMediaItem(item)
        mediaController.prepare()
        mediaController.play()

        Log.d(TAG, "Sent searchQuery to MediaSession: [$query]")
    }

    override fun onDestroy() {
        controller?.release()
        controller = null
        super.onDestroy()
    }
}
