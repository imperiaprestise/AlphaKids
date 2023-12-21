package com.example.alphakids.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import com.example.alphakids.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
private const val MAXIMAL_SIZE = 1000000

fun createCustomDrawable(context: Context, char: Char, size: Int): Drawable {
    val mBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val mCanvas = Canvas(mBitmap)
    val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    mCanvas.drawColor(ContextCompat.getColor(context, R.color.yellow))
    titlePaint.apply {
        textSize = size / 2f
        color = ContextCompat.getColor(context, R.color.white)
        textAlign = Paint.Align.CENTER
    }

    val x = mCanvas.width / 2f
    val y = (mCanvas.height / 2f) - ((titlePaint.descent() + titlePaint.ascent()) / 2f)

    mCanvas.drawText(char.toString(), x, y, titlePaint)

    return BitmapDrawable(context.resources, mBitmap)
}

fun uriToFile(imageUri: Uri, context: Context): File{
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}



fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun File.reduceFileImage(): File{
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

