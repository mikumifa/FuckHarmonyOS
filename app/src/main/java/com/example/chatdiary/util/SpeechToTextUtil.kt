import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.MATCH_ALL
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognitionService
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale


class SpeechToTextUtil(private val context: Context) {
    var onSpeechRecognitionResult: ((String) -> Unit)? = null
    var onSpeechRecognitionError: ((Int) -> Unit)? = null

    private var speechRecognizer: SpeechRecognizer? = null
    var isCanUseSpeechRecognition: Boolean = isSpeechRecognitionAvailable()

    fun setSpeechRecognitionListener(
        onSpeechRecognitionResult: (String) -> Unit, onSpeechRecognitionError: (Int) -> Unit
    ) {
        this.onSpeechRecognitionResult = onSpeechRecognitionResult
        this.onSpeechRecognitionError = onSpeechRecognitionError
    }

    fun startListening() {
        if (isCanUseSpeechRecognition) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    onSpeechRecognitionError?.let { it(error) }
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val recognizedText = matches[0]
                        onSpeechRecognitionResult?.let { it(recognizedText) }
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

    @SuppressLint("QueryPermissionsNeeded")
    fun isSpeechRecognitionAvailable(): Boolean {
        val packageManager = context.packageManager
        val serviceComponent: String = Settings.Secure.getString(
            context.contentResolver, "voice_recognition_service"
        )
        var isRecognizerServiceValid = false
        var currentRecognitionCmp: ComponentName? = null
        val component = ComponentName.unflattenFromString(serviceComponent)
        return packageManager?.let {
            val speechRecognitionActivities = it.queryIntentServices(
                Intent(RecognitionService.SERVICE_INTERFACE), MATCH_ALL
            )
            for (info in speechRecognitionActivities) {
                if (component != null) {
                    if (info.serviceInfo.packageName.equals(component.packageName)) {
                        isRecognizerServiceValid = true;
                        break;
                    } else {

                        currentRecognitionCmp =
                            ComponentName(info.serviceInfo.packageName, info.serviceInfo.name)
                    }
                }
            }
            speechRecognizer = if (isRecognizerServiceValid) {
                SpeechRecognizer.createSpeechRecognizer(context);
            } else {
                SpeechRecognizer.createSpeechRecognizer(
                    context, currentRecognitionCmp
                )
            }
            return@let speechRecognitionActivities.isNotEmpty()
        } ?: false
    }
}
