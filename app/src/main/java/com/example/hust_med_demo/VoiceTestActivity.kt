package com.example.hust_med_demo

import android.Manifest
import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import java.util.Locale

private const val TAG = "HUST_VOICE_DEBUG"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class VoiceTestActivity : AppCompatActivity() {

    private var speechRecognizer: SpeechRecognizer? = null
    private var controller: MediaController? = null

    private lateinit var statusText: TextView
    private lateinit var resultText: TextView
    private lateinit var btnListen: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "--- HUST VOICE: onCreate ---")

        setupSimpleUI()
        initController()

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.e(TAG, "Speech Recognition is NOT available on this device")
            statusText.text = "Error: System Speech Service missing"
            btnListen.isEnabled = false
            return
        }

        checkPermissions()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        Log.d(TAG, "onNewIntent: action=${intent.action}")
        handleSearchIntent(intent)
    }

    private fun setupSimpleUI() {
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(60, 60, 60, 60)
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }

        statusText = TextView(this).apply {
            text = "Status: Connecting..."
            setPadding(0, 20, 0, 20)
        }

        btnListen = Button(this).apply {
            text = "START VOICE COMMAND"
            setOnClickListener {
                Log.d(TAG, "Button clicked: starting fresh listening session")
                startListening()
            }
        }

        resultText = TextView(this).apply {
            text = "Try saying: 'play Havana'"
            textSize = 20f
            setPadding(0, 40, 0, 0)
            setTextColor(android.graphics.Color.BLUE)
        }

        layout.addView(statusText)
        layout.addView(btnListen)
        layout.addView(resultText)
        setContentView(layout)
    }

    private fun initController() {
        val sessionToken = SessionToken(this, ComponentName(this, MyMediaService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        controllerFuture.addListener({
            try {
                controller = controllerFuture.get()
                Log.d(TAG, "MediaController connected successfully")
                statusText.text = "Status: Connected & Ready"
                handleSearchIntent(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to connect MediaController: ${e.message}")
                statusText.text = "Status: Connection Failed"
            }
        }, MoreExecutors.directExecutor())
    }

    private fun initSpeechRecognizer() {
        // GIẢI PHÓNG phiên cũ để dọn dẹp tài nguyên
        speechRecognizer?.destroy()

        Log.d(TAG, "Creating fresh SpeechRecognizer instance")
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d(TAG, "onReadyForSpeech")
                statusText.text = "Status: Listening... Speak now"
            }

            override fun onBeginningOfSpeech() {
                Log.d(TAG, "onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                if (rmsdB > 2f) {
                    statusText.text = "Status: Listening... 🎙️ (${rmsdB.toInt()})"
                }
            }

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech")
                statusText.text = "Status: Processing..."
            }

            override fun onError(error: Int) {
                val errorMsg = when (error) {
                    SpeechRecognizer.ERROR_NETWORK -> "Network Error (2)"
                    SpeechRecognizer.ERROR_CLIENT -> "Client Error (5) - Re-click to retry"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Timeout (6) - No voice heard"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match (7) - Speak clearer"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Busy (8)"
                    11 -> "Server Disconnected (11)"
                    else -> "Error code: $error"
                }
                Log.e(TAG, "SpeechRecognizer Error: $errorMsg ($error)")
                statusText.text = "Status: $errorMsg"

                // Hủy instance khi lỗi để đảm bảo lần tới là phiên sạch
                if (error == 5 || error == 8) {
                    speechRecognizer?.destroy()
                    speechRecognizer = null
                }
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    Log.i(TAG, "RESULT: $recognizedText")
                    resultText.text = recognizedText

                    val searchIntent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH).apply {
                        putExtra(SearchManager.QUERY, recognizedText)
                        putExtra(MediaStore.EXTRA_MEDIA_TITLE, recognizedText)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    onNewIntent(searchIntent)
                }
                statusText.text = "Status: Ready"
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    resultText.text = matches[0] + "..."
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun startListening() {
        // KHỞI TẠO LẠI mỗi khi nhấn nút để tránh treo dịch vụ hệ thống
        initSpeechRecognizer()

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        }

        try {
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            statusText.text = "Status: Failed to start"
        }
    }

    private fun handleSearchIntent(intent: Intent?) {
        if (intent == null || controller == null) return
        val isPlayFromSearch = intent.action == MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
        val isSearch = intent.action == Intent.ACTION_SEARCH

        if (isPlayFromSearch || isSearch) {
            val query = intent.getStringExtra(SearchManager.QUERY)
                ?: intent.getStringExtra(MediaStore.EXTRA_MEDIA_TITLE)
                ?: ""

            if (query.isBlank()) return

            Log.d(TAG, "Sending query [$query] to MediaSession")
            val item = MediaItem.Builder()
                .setMediaId("voice_request")
                .setRequestMetadata(MediaItem.RequestMetadata.Builder().setSearchQuery(query).build())
                .build()

            try {
                controller?.setMediaItem(item)
                controller?.prepare()
                controller?.play()
                statusText.text = "Playing: $query"
            } catch (e: Exception) {
                Log.e(TAG, "Player error: ${e.message}")
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
        controller?.release()
    }
}