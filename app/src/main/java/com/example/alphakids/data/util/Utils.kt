package com.example.alphakids.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.alphakids.R

fun createCustomDrawable(context: Context, char: Char): Drawable {
    val mBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    val mCanvas = Canvas(mBitmap)
    val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    mCanvas.drawColor(ContextCompat.getColor(context, R.color.yellow))
    titlePaint.apply {
        textSize = 50f
        color = ContextCompat.getColor(context, R.color.white)
        textAlign = Paint.Align.CENTER
    }

    val x = mCanvas.width / 2f
    val y = (mCanvas.height / 2f) - ((titlePaint.descent() + titlePaint.ascent()) / 2f)

    mCanvas.drawText(char.toString(), x, y, titlePaint)

    return BitmapDrawable(context.resources, mBitmap)
}
