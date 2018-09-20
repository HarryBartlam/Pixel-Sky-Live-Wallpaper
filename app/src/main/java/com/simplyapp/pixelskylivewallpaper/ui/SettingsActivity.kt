package com.simplyapp.pixelskylivewallpaper.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.simplyapp.pixelskylivewallpaper.R
import com.simplyapp.pixelskylivewallpaper.ui.colourpicker.ColourPickerDialog
import com.simplyapp.pixelskylivewallpaper.utils.Colour
import com.simplyapp.pixelskylivewallpaper.utils.StarsPrefsHelper
import com.simplyapp.pixelskylivewallpaper.view.SmartTogglePaletteButton
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : Activity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var prefsHelper: StarsPrefsHelper

    private val onColourClick = View.OnClickListener { view ->
        val button = view as SmartTogglePaletteButton
        if (button.isCustomPalette) {
            val dialog = ColourPickerDialog(this, prefsHelper.getCustomColorList(button.customPaletteId!!))
            dialog.onDialogFinished = { colorArray ->
                if (colorArray.isNotEmpty()) {
                    prefsHelper.setCustomColorList(button.customPaletteId, colorArray)
                    button.setSelected(true)
                    val colour = Colour.valueOf(button.tag as String)
                    prefsHelper.colour = colour

                } else {
                    prefsHelper.setCustomColorList(button.customPaletteId, colorArray)
                    if (button.isSelected) {
                        val colour = Colour.valueOf(Colour.BLUE.name)
                        prefsHelper.colour = colour
                        findViewById<View>(R.id.settings_colour_blue).setSelected(true)
                    }
                }
                reloadCustomColours()
            }
            dialog.show()
        } else {
            val colour = Colour.valueOf(button.tag as String)
            prefsHelper.colour = colour
            button.isSelected = true
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        prefsHelper = StarsPrefsHelper(this)

        for (i in 0 until settings_color_layout.childCount) {
            settings_color_layout.getChildAt(i).setOnClickListener(onColourClick)
        }

        settings_fps_seekbar.setOnSeekBarChangeListener(this)
        settings_star_count_seekbar.setOnSeekBarChangeListener(this)
    }

    override fun onResume() {
        super.onResume()

        val colourButton = settings_color_layout.findViewWithTag<View>(prefsHelper.colour.name)
        colourButton?.setSelected(true)
                ?: findViewById<View>(R.id.settings_colour_blue).setSelected(true)

        val speedSetting = prefsHelper.speedFps
        settings_current_fps.text = speedSetting.toString()
        settings_fps_seekbar.progress = speedSetting.toInt()

        val countSetting = prefsHelper.starCount.toLong()
        settings_current_star_count.text = countSetting.toString()
        settings_star_count_seekbar.progress = countSetting.toInt()
    }

    private fun reloadCustomColours() {
        settings_colour_custom_1.reloadCustomColours()
        settings_colour_custom_2.reloadCustomColours()
        settings_colour_custom_3.reloadCustomColours()
        settings_colour_custom_4.reloadCustomColours()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.settings_fps_seekbar -> {
                prefsHelper.speedFps = progress.toLong()
                settings_current_fps.text = progress.toString()
            }
            R.id.settings_star_count_seekbar -> {
                prefsHelper.starCount = progress
                settings_current_star_count.text = progress.toString()
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}
