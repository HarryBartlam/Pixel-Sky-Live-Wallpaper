package com.simplyapp.pixelskylivewallpaper.view

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.View

import com.simplyapp.pixelskylivewallpaper.utils.StarsPrefsHelper
import com.simplyapp.pixelskylivewallpaper.wallpaper.AnimatedStarsDrawer

class StarView : View, Runnable, SharedPreferences.OnSharedPreferenceChangeListener {
    private var starDrawer: AnimatedStarsDrawer? = null
    private var prefsHelper: StarsPrefsHelper? = null

    var fps = 1000L / StarsPrefsHelper.DEFAULT_SPEED

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        starDrawer = AnimatedStarsDrawer()
        prefsHelper = StarsPrefsHelper(context)
//        updateStarDrawer()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post {
            updateStarDrawer()
            starDrawer!!.setCanvasSize(width, height)
        }
        post(this)
        prefsHelper!!.preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(this)
        prefsHelper!!.preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        starDrawer!!.draw(canvas)
    }

    override fun run() {
        postInvalidate()
        postDelayed(this, fps)
    }

    private fun updateStarDrawer() {
        if (fps != 1000 / prefsHelper!!.speedFps){
            fps = 1000 / prefsHelper!!.speedFps
        }
        val colour = prefsHelper!!.colour
        if (colour.customId == null) {
            starDrawer?.initStars(prefsHelper!!.colour.colours, prefsHelper!!.starCount, prefsHelper!!.minSize, prefsHelper!!.maxSize)
        } else {
            val colorList = prefsHelper!!.getCustomColorList(colour.customId)
            if (colorList.isEmpty()) return
            starDrawer?.initStars(colorList, prefsHelper!!.starCount, prefsHelper!!.minSize, prefsHelper!!.maxSize)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        updateStarDrawer()
    }
}
