package com.example.beachprofile.measures

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun VoiceTranscriber(transcription: MutableState<String>) {
    val context = LocalContext.current
    val recognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val intent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().language)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }

    var isListening by remember { mutableStateOf(false) }

    val recognitionListener = remember {
        object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                transcription.value = matches?.firstOrNull() ?: "No result"
                isListening = false
            }

            override fun onError(error: Int) {
                if(error != SpeechRecognizer.ERROR_NO_MATCH && error != SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {

                }
            }

            override fun onBeginningOfSpeech() {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
            override fun onPartialResults(partialResults: Bundle?) {
                val partialData = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val partialText = partialData?.firstOrNull()
                if (!partialText.isNullOrEmpty()) {
                    transcription.value = "$partialText…"
                }
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onRmsChanged(rmsdB: Float) {}
        }
    }

    LaunchedEffect(Unit) {
        recognizer.setRecognitionListener(recognitionListener)
    }

    Button(onClick = {
        if (!isListening) {
            recognizer.startListening(intent)
            isListening = true
        }
    }, shape = CircleShape) {
        if (isListening) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Stop recording a transcribed note"
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Record a transcribed note"
            )
        }
    }
}

fun restartListening(recognizer: SpeechRecognizer, intent: Intent) {
    recognizer.stopListening()
    recognizer.cancel()
    Handler(Looper.getMainLooper()).postDelayed({
        recognizer.startListening(intent)
    }, 500)
}