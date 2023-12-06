package com.example.alphakids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.alphakids.databinding.ActivityScanBinding
import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            val message = if(isGranted){
                "Camera permission granted"
            } else {
                "Camera permission rejected"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        startCamera()
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview
                )
            } catch (exc: Exception){
                Toast.makeText(this, "gagal memunculkan kamera.", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}