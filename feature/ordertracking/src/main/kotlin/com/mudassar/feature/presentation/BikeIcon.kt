package com.mudassar.feature.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt

fun createBikeIcon(context: Context): BitmapDrawable {
    val size = 64
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#3366FF".toColorInt()
        style = Paint.Style.STROKE
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
    }

    val cx = size / 2f
    val cy = size / 2f
    val rearWheel = PointF(cx - 14f, cy + 8f)
    val frontWheel = PointF(cx + 14f, cy + 8f)
    val pedal = PointF(cx, cy + 8f)
    val seat = PointF(cx - 7f, cy - 9f)
    val handlebar = PointF(cx + 11f, cy - 7f)

    canvas.drawLine(rearWheel.x, rearWheel.y, seat.x, seat.y, paint)
    canvas.drawLine(seat.x, seat.y, pedal.x, pedal.y, paint)
    canvas.drawLine(pedal.x, pedal.y, handlebar.x, handlebar.y, paint)
    canvas.drawLine(handlebar.x, handlebar.y, frontWheel.x, frontWheel.y, paint)
    canvas.drawLine(pedal.x, pedal.y, frontWheel.x, frontWheel.y, paint)
    canvas.drawCircle(rearWheel.x, rearWheel.y, 8f, paint)
    canvas.drawCircle(frontWheel.x, frontWheel.y, 8f, paint)

    return bitmap.toDrawable(context.resources)
}
