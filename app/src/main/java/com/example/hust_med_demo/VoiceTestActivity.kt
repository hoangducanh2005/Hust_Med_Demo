package com.example.hust_med_demo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

private const val TAG = "HUST_VOICE_DEBUG"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class VoiceTestActivity : AppCompatActivity() {

    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var statusText: TextView
    private lateinit var resultText: TextView
    private lateinit var btnListen: Button
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "--- HUST VOICE: onCreate ---")
        
        setupSimpleUI()

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.e(TAG, "Speech Recognition is NOT available on this device")
            statusText.text = "Error: System Speech Service missing"
            btnListen.isEnabled = false
            return
        }

        checkAndRequestPermissions()
    }

    private fun setupSimpleUI() {
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(60, 60, 60, 60)
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }

        statusText = TextView(this).apply { 
            text = "Status: Ready"
            setPadding(0, 20, 0, 20)
        }
        
        btnListen = Button(this).apply {
            text = "START LISTENING (ENGLISH)"
            setOnClickListener { 
                Log.d(TAG, "Listen button clicked")
                handleStartClick()
            }
        }

        resultText = TextView(this).apply { 
            text = "Recognition result will appear here"
            textSize = 20f
            setPadding(0, 40, 0, 0)
            setTextColor(android.graphics.Color.BLUE)
        }

        layout.addView(statusText)
        layout.addView(btnListen)
        layout.addView(resultText)
        setContentView(layout)
    }

    private fun cleanupRecognizer() {
        Log.d(TAG, "Cleaning up existing recognizer...")
        try {
            speechRecognizer?.apply {
                stopListening()
                cancel()
                destroy()
            }
            speechRecognizer = null
        } catch (e: Exception) {
            Log.e(TAG, "Error during cleanup: ${e.message}")
        }
    }

    private fun handleStartClick() {
        btnListen.isEnabled = false
        statusText.text = "Status: Initializing..."
        
        cleanupRecognizer()
        
        // Delay 150ms trước khi tạo mới để tránh lỗi 11 (Server Disconnected)
        mainHandler.postDelayed({
            initAndStartListening()
            btnListen.isEnabled = true
        }, 150)
    }

    private fun initAndStartListening() {
        Log.d(TAG, "Creating new recognizer instance...")
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d(TAG, "onReadyForSpeech")
                statusText.text = "Status: Listening... Speak now"
            }

            override fun onBeginningOfSpeech() {
                Log.d(TAG, "onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech")
                statusText.text = "Status: Processing..."
            }

            override fun onError(error: Int) {
                val errorMsg = when (error) {
                    SpeechRecognizer.ERROR_NETWORK -> "Network Error (2)"
                    SpeechRecognizer.ERROR_CLIENT -> "Client Error (5)"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Timeout (6)"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match (7)"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Busy (8)"
                    11 -> "Server Disconnected (11)"
                    else -> "Error code: $error"
                }
                Log.e(TAG, "SpeechRecognizer Error: $errorMsg")
                statusText.text = "Status: $errorMsg"
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val result = matches[0]
                    Log.i(TAG, "RESULT: $result")
                    resultText.text = result
                    statusText.text = "Status: Success!"
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    resultText.text = matches[0] + "..."
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            
            // Cấu hình tăng độ nhạy và thời gian chờ để tránh Error 7
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L)
        }
        
        Log.d(TAG, "Calling startListening")
        try {
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            statusText.text = "Status: Failed to start"
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanupRecognizer()
    }
}
