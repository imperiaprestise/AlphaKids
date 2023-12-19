package com.example.alphakids.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.alphakids.R
import com.example.alphakids.databinding.FragmentWriteBinding
import com.example.alphakids.view.scan.ScanViewModel
import com.example.alphakids.view.write.ImageDetectionActivity
import com.example.alphakids.view.write.WriteViewModel
import com.nex3z.fingerpaintview.FingerPaintView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WriteFragment : Fragment() {

    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageDetectedView: ImageView
    private lateinit var fingerPaintView: FingerPaintView
    private lateinit var convertToImageButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fingerPaintView = binding.fingerPaintView
        convertToImageButton = binding.convertToImage

        convertToImageButton.setOnClickListener {
            convertFingerPaintViewToImage()
        }
    }

    private fun convertFingerPaintViewToImage() {
        val drawingBitmap = fingerPaintView.exportToBitmap()
        val imageFile = saveBitmapToFile(drawingBitmap)

        val intent = Intent(activity, ImageDetectionActivity::class.java)
        intent.putExtra("imagePath", imageFile.absolutePath)

        startActivity(intent)
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val filesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "handwriting_image.jpg")

        try {
            val os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imageFile
    }


    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}