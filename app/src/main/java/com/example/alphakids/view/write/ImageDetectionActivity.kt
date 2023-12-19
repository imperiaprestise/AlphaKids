package com.example.alphakids.view.write

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.alphakids.R
import com.example.alphakids.data.response.PredictResponse
import com.example.alphakids.databinding.ActivityImageDetectionBinding
import com.example.alphakids.view.ViewModelFactory
import java.io.File
import java.util.Locale

class ImageDetectionActivity : AppCompatActivity(), TextToSpeech.OnInitListener  {

    private lateinit var binding: ActivityImageDetectionBinding

    private val writeViewModel by viewModels<WriteViewModel>(){
        ViewModelFactory.getInstance(this)
    }
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textToSpeech = TextToSpeech(this, this)

        binding.speakerIcon.setOnClickListener {
            val detectedText = binding.detectedText.text.toString()
            if (detectedText.isNotEmpty()) {
                speakText(detectedText)
            } else {
                showToast("No text available to speak.")
            }
        }

        val imagePath = intent.getStringExtra("imagePath")
        if (imagePath != null) {
            val imageBitmap = BitmapFactory.decodeFile(imagePath)
            val imageView: ImageView = findViewById(R.id.imageDetectView)
            imageView.setImageBitmap(imageBitmap)
        } else {
            showToast("Image path is null")
            finish()
        }

        binding.buttonDetect.setOnClickListener {
            performTextDetection()
        }

        writeViewModel.errorMessage.observe(this){ errorMessage ->
            Log.d("Image Detection ACtivity", "Error message: $errorMessage")
            if (errorMessage != null){
                showToast(errorMessage)
            }
        }

        writeViewModel.predictResult.observe(this) { predictResponse ->
            Log.d("ImageDetectionActivity", "Predict result: $predictResponse")
            if (predictResponse != null) {
                setPredict(predictResponse)
                showToast("deteksi berhasil")
            }
        }

        writeViewModel.predictResult.observe(this){
            setPredict(it)
        }

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val langResult = textToSpeech.setLanguage(Locale.US)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("ImageDetectionActivity", "Language is not supported.")
            }
        } else {
            Log.e("ImageDetectionActivity", "TextToSpeech initialization failed.")
        }
    }

    private fun setPredict(response: PredictResponse){
        val predictLetter = response.data?.predictedLetter
        binding.detectedText.text = predictLetter ?: "no prediction result available"
    }

    private fun performTextDetection() {
        val imageFilePath = intent.getStringExtra("imagePath")
        Log.d("ImageDetectionActivity", "Performing text detection for image: $imageFilePath")

        val imageFile = File(imageFilePath)
        if (imageFile.exists()) {
            writeViewModel.predictAlphabet(imageFile)
        } else {
            showToast("File not found: $imageFile")
            Log.e("ImageDetectionActivity", "File not found: $imageFile")
        }
    }

    private fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

}