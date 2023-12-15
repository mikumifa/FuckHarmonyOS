import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.*
class SpeechToTextUtil(private val context: Context) {
    interface SpeechRecognitionListener {
        fun onSpeechRecognitionResult(result: String)
        fun onSpeechRecognitionError(errorCode: Int)
    }

    private var speechRecognizer: SpeechRecognizer? = null
    private var listener: SpeechRecognitionListener? = null

    fun setSpeechRecognitionListener(listener: SpeechRecognitionListener) {
        this.listener = listener
    }

    fun startListening() {
        if (isSpeechRecognitionAvailable()) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    listener?.onSpeechRecognitionError(error)
                }
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (matches != null && matches.isNotEmpty()) {
                        val recognizedText = matches[0]
                        listener?.onSpeechRecognitionResult(recognizedText)
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            speechRecognizer?.startListening(intent)
        }
    }
    fun stopListening() {
        speechRecognizer?.stopListening()
    }
    fun destroy() {
        speechRecognizer?.destroy()
    }
    private fun isSpeechRecognitionAvailable(): Boolean {
        val packageManager = context.packageManager
        return packageManager?.let {
            val speechRecognitionActivities =
                it.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
            return@let speechRecognitionActivities.isNotEmpty()
        } ?: false
    }
}