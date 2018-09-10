package com.simplyapp.pixelskylivewallpaper.wallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import com.simplyapp.pixelskylivewallpaper.utils.Colour
import com.simplyapp.pixelskylivewallpaper.utils.StarsPrefsHelper
import java.util.*
import java.util.Collections.emptyList

class AnimatedStarsDrawer {
    private var starCount: Int = StarsPrefsHelper.DEFAULT_STARS
    private var starColors: IntArray = Colour.BLUE.colours
    private var minStarSize: Int = StarsPrefsHelper.DEFAULT_MIN
    private var maxStarSize: Int  = StarsPrefsHelper.DEFAULT_MIN
    private var bigStarThreshold: Int = minStarSize + maxStarSize / 2

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var starConstraints: Star.StarConstraints = Star.StarConstraints(minStarSize, maxStarSize, bigStarThreshold)

    private var stars: List<Star> = emptyList()

    private val random: Random = Random()
    private var initiated: Boolean = false

    fun setCanvasSize(w: Int, h: Int) {
        viewWidth = w
        viewHeight = h

        if (viewWidth > 0 && viewHeight > 0) {
            initStarList()
        }
    }

    fun draw(canvas: Canvas) {
        if (!initiated) return
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        stars.forEach {
            it.calculateFrame(viewWidth, viewHeight)
            it.draw(canvas)
        }
    }

    fun initStars(starColors: IntArray, starCount: Int, minStarSize: Int, maxStarSize: Int) {
        this.starColors = starColors
        this.starCount = starCount
        this.minStarSize = minStarSize
        this.maxStarSize = maxStarSize
        this.bigStarThreshold = minStarSize + maxStarSize / 2
        this.starConstraints = Star.StarConstraints(minStarSize, maxStarSize, bigStarThreshold)

        initStarList()
    }
    private fun initStarList(){
        stars = List(starCount) {
            Star(
                    starConstraints, // constraints
                    Math.round(Math.random() * viewWidth).toInt(), // x
                    Math.round(Math.random() * viewHeight).toInt(), // y
                    Math.random(), // opacity
                    starColors[it % starColors.size], // color
                    viewWidth,
                    viewHeight
                    // function for generating new color
            ) { starColors[random.nextInt(starColors.size)] }
        }

        initiated = true
    }

}


