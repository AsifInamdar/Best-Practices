package com.diatoz.bestpractices.ui.custom

import android.graphics.*
import android.graphics.drawable.Drawable

class ThumbDrawable(thumbColor: Int) : Drawable() {

    var DENSITY = 10f
    val SHADOW_DISTANCE = 100f
    val SHADOW_COLOR = Color.RED

    private val whitePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true

        val shadowSize = (SHADOW_DISTANCE * DENSITY)
        setShadowLayer(shadowSize, 10f, 10f, SHADOW_COLOR)
    }

    private val thumbOuterPaint = Paint().apply {
        isAntiAlias = true
        color = thumbColor
        val shadowSize = (SHADOW_DISTANCE * DENSITY)
        setShadowLayer(shadowSize, 10f, 10f, SHADOW_COLOR)
    }

    private val thumbInnerPaint = Paint().apply {
        isAntiAlias = true
        color = thumbColor

        val shadowSize = (SHADOW_DISTANCE * DENSITY)
        setShadowLayer(shadowSize, 10f, 10f, SHADOW_COLOR)
    }

    override fun draw(canvas: Canvas) {
        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()
        val radius = centerX - bounds.left

        canvas.apply {
            drawCircle(centerX, centerY, radius, thumbOuterPaint)
            drawCircle(centerX, centerY, radius / 2f, thumbInnerPaint)
            drawCircle(centerX, centerY, 3f, whitePaint)
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}