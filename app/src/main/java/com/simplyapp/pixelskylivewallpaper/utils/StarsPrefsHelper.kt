package com.simplyapp.pixelskylivewallpaper.utils

import android.content.Context
import android.content.SharedPreferences

class StarsPrefsHelper(ctx: Context) {
    val preferences: SharedPreferences = ctx.getSharedPreferences("starWallpaper", Context.MODE_PRIVATE)

    var minSize: Int
        get() = preferences.getInt(PREF_MIN_SIZE, DEFAULT_MIN)
        set(size) = preferences.edit().putInt(PREF_MIN_SIZE, size).apply()

    var maxSize: Int
        get() = preferences.getInt(PREF_MAX_SIZE, DEFAULT_MAX)
        set(size) = preferences.edit().putInt(PREF_MAX_SIZE, size).apply()

    var starCount: Int
        get() = preferences.getInt(PREF_STARS, DEFAULT_STARS)
        set(count) = preferences.edit().putInt(PREF_STARS, count).apply()

    var colour: Colour
        get() = Colour.valueOf(preferences.getString(PREF_COLOUR, Colour.BLUE.name))
        set(colour) = preferences.edit().putString(PREF_COLOUR, colour.name).apply()

    var speedFps: Long
        get() = preferences.getLong(PREF_SPEED_INT, DEFAULT_SPEED)
        set(fps) = preferences.edit().putLong(PREF_SPEED_INT, if (fps > 0) fps else 1).apply()

    fun setCustomColorList(customPrefId: String, customColorList: IntArray) {
        preferences.edit().putStringSet(customPrefId, customColorList.map { it.toString() }.toSet()).apply()
    }

    fun getCustomColorList(customPrefId: String): IntArray {
        return preferences.getStringSet(customPrefId, setOf()).map { it.toInt() }.toIntArray()
    }

    companion object {

        val DEFAULT_MIN = 4
        val DEFAULT_MAX = 24

        private val PREF_MAX_SIZE = "maxSize.int"
        private val PREF_MIN_SIZE = "maxSize.int"

        val DEFAULT_STARS = 500
        private val PREF_STARS = "maxStars.int"

        val PREF_COLOUR = "colour.enum"

        val DEFAULT_SPEED = 60L
        private val PREF_SPEED_INT = "speedfps.int"

        val PREF_COLOUR_CUSTOM_1 = "colourCustom1.set"
        val PREF_COLOUR_CUSTOM_2 = "colourCustom2.set"
        val PREF_COLOUR_CUSTOM_3 = "colourCustom3.set"
        val PREF_COLOUR_CUSTOM_4 = "colourCustom4.set"

    }
}
