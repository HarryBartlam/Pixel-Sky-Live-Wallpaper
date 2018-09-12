package com.simplyapp.pixelskylivewallpaper.wallpaper

import android.graphics.Canvas
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.simplyapp.pixelskylivewallpaper.utils.StarsPrefsHelper

class StarWallpaperService : android.service.wallpaper.WallpaperService() {

    override fun onCreateEngine(): WallpaperService.Engine {
        return GridEngine()
    }

    private inner class GridEngine internal constructor() : WallpaperService.Engine() {
        private var fps: Long = 1000L / StarsPrefsHelper.DEFAULT_SPEED

        private val handler = Handler()
        private val drawRunner = Runnable { draw() }

        private var visible = true
        private val animatedStarsDrawer: AnimatedStarsDrawer = AnimatedStarsDrawer()

        init {
            setupStars()
            handler.removeCallbacks(drawRunner)
            handler.post(drawRunner)
        }

        private fun setupStars() {
            val prefs = StarsPrefsHelper(this@StarWallpaperService)
            fps = 1000L / prefs.speedFps
            val colour = prefs.colour
            if (colour.customId == null) {
                animatedStarsDrawer.initStars(colour.colours, prefs.starCount, prefs.minSize, prefs.maxSize)
            } else {
                val colours = prefs.getCustomColorList(colour.customId)
                if (colours.isEmpty()) return
                animatedStarsDrawer.initStars(colours, prefs.starCount, prefs.minSize, prefs.maxSize)
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                setupStars()
                handler.removeCallbacks(drawRunner)
                handler.post(drawRunner)
            } else {
                handler.removeCallbacks(drawRunner)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            this.visible = false
            handler.removeCallbacks(drawRunner)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            setupStars()
            animatedStarsDrawer.setCanvasSize(width, height)
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder) {
            super.onSurfaceRedrawNeeded(holder)
            draw()
        }

        private fun draw() {
            var canvas: Canvas? = null
            try {
                canvas = surfaceHolder.lockCanvas()
                if (canvas != null) {
                    animatedStarsDrawer.draw(canvas)
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }

            handler.removeCallbacks(drawRunner)

            if (visible) {
                handler.postDelayed(drawRunner, fps)
            }
        }
    }
}