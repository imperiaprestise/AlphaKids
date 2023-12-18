package com.example.alphakids.view.scan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.alphakids.databinding.ActivityScanBinding
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.coroutineScope
import com.example.alphakids.R
import com.example.alphakids.data.response.PredictResponse
import com.example.alphakids.data.util.reduceFileImage
import com.example.alphakids.data.util.uriToFile
import com.example.alphakids.view.ViewModelFactory
import com.example.alphakids.view.scan.CameraActivity.Companion.CAMERAX_RESULT
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private val scanViewModel by viewModels<ScanViewModel>(){
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    private var isImageFromCameraX: Boolean = false

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
                isGranted: Boolean ->
            if (isGranted){
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted()=
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()){
            //request permission
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.cameraCaptureButton.setOnClickListener { startCameraX() }

        binding.cameraScanButton.setOnClickListener { scanButton() }

        scanViewModel.errorMessage.observe(this) { errorMessage ->
            Log.d("ScanActivity", "Error message: $errorMessage")
            if (errorMessage != null) {
                showToast(errorMessage)
            }
        }

        scanViewModel.predictResult.observe(this) { predictResponse ->
            Log.d("ScanActivity", "Predict result: $predictResponse")
            if (predictResponse != null) {
                setPredict(predictResponse)
                showToast("deteksi berhasil")
            }
        }

        scanViewModel.predictResult.observe(this){
            setPredict(it)
        }

    }

    private fun scanButton() {
        if (isImageFromCameraX) {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                scanViewModel.predictAlphabet(imageFile)
            } ?: showToast("please insert the image file first")
        } else {
            showToast("Please capture an image using CameraX first.")
        }
    }

    private fun setPredict(response: PredictResponse){
        val predictedLetter = response.data?.predictedLetter
        binding.detectedText.text = predictedLetter ?: "No prediction result available"
    }


    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == CAMERAX_RESULT){
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            isImageFromCameraX = true // Setel status bahwa gambar berasal dari kamera X
            showImage()
        }
    }

    private fun showImage(){
        currentImageUri?.let {
            Log.d("image URI", "showImage: $it")
            binding.cameraView.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }


}
