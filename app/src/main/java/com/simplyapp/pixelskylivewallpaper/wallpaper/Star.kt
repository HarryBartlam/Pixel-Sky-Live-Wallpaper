package com.simplyapp.pixelskylivewallpaper.wallpaper

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Star(starConstraints: StarConstraints, var x: Int, var y: Int, var opacity: Double, var color: Int, viewWidth: Int, viewHeight: Int, private val colorListener: () -> Int) {
    var alpha: Int = 0
    var factor: Int = 1
    var increment: Double

    private val length: Double = (starConstraints.minStarSize + Math.random() * (starConstraints.maxStarSize - starConstraints.minStarSize))
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private var shape: StarShape

    private lateinit var hRect: RectF
    private lateinit var vRect: RectF

    init {

        fillPaint.color = color

        strokePaint.color = color
        strokePaint.strokeWidth = length.toFloat() / 4f

        shape = if (length >= starConstraints.bigStarThreshold) {

            if (Math.random() < 0.7) {
                StarShape.STAR
            } else {
                StarShape.CIRCLE
            }
        } else {
            StarShape.DOT
        }

        increment = when (shape) {
            StarShape.CIRCLE -> {
                Math.random() * .025
            }
            StarShape.STAR -> {
                Math.random() * .030
            }
            StarShape.DOT -> {
                Math.random() * .045
            }
        }

        initLocationAndRectangles(viewWidth, viewHeight)
    }

    fun calculateFrame(viewWidth: Int, viewHeight: Int) {

        if (opacity >= 1 || opacity <= 0) {
            factor *= -1
        }

        opacity += increment * factor

        alpha = (opacity * 255.0).toInt()

        when {
            alpha > 255 -> {
                alpha = 255
            }
            alpha <= 0 -> {
                alpha = 0

                initLocationAndRectangles(viewWidth, viewHeight)

                color = colorListener.invoke()

                fillPaint.color = color

                strokePaint.color = color
                strokePaint.style = Paint.Style.STROKE
                strokePaint.strokeWidth = length.toFloat() / 4f
            }
        }
    }

    private fun initLocationAndRectangles(viewWidth: Int, viewHeight: Int) {
        x = Math.round(Math.random() * viewWidth).toInt()
        y = Math.round(Math.random() * viewHeight).toInt()

        if (shape == StarShape.STAR) {

            val hLeft = (x - length / 2).toFloat()
            val hRight = (x + length / 2).toFloat()
            val hTop = (y - length / 6).toFloat()
            val hBottom = (y + length / 6).toFloat()

            hRect = RectF(hLeft, hTop, hRight, hBottom)

            val vLeft = (x - length / 6).toFloat()
            val vRight = (x + length / 6).toFloat()
            val vTop = (y - length / 2).toFloat()
            val vBottom = (y + length / 2).toFloat()

            vRect = RectF(vLeft, vTop, vRight, vBottom)
        }
    }

    fun draw(canvas: Canvas) {
        fillPaint.alpha = alpha
        strokePaint.alpha = alpha
        canvas.save()
        when (shape) {
            StarShape.DOT -> {
                canvas.drawCircle(x.toFloat(), y.toFloat(), length.toFloat() / 2f, fillPaint)
            }
            StarShape.STAR -> {
                canvas.drawRoundRect(hRect, 6f, 6f, fillPaint)
                canvas.drawRoundRect(vRect, 6f, 6f, fillPaint)
            }
            StarShape.CIRCLE -> {
                canvas.drawCircle(x.toFloat(), y.toFloat(), length.toFloat() / 2f, strokePaint)
            }
        }
        canvas.restore()
    }

    private enum class StarShape {
        CIRCLE, STAR, DOT
    }

    class StarConstraints(val minStarSize: Int, val maxStarSize: Int, val bigStarThreshold: Int)
}

