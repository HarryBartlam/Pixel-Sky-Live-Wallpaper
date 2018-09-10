package com.simplyapp.pixelskylivewallpaper.ui

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.simplyapp.pixelskylivewallpaper.R
import com.simplyapp.pixelskylivewallpaper.wallpaper.StarWallpaperService
import kotlinx.android.synthetic.main.activity_set_wallpaper.*

class SetWallpaperActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_wallpaper)
        wallpaper_settings.setOnClickListener {
            Intent(this, SettingsActivity::class.java).apply {
                startActivity(this)
            }
        }
        wallpaper_set_wallpaper.setOnClickListener {
            setWallpaper()
        }
    }

    private fun setWallpaper() {
        val intent = Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this, StarWallpaperService::class.java))
        startActivity(intent)
        finish()
    }
}