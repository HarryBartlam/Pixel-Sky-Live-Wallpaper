package com.simplyapp.pixelskylivewallpaper.ui.colourpicker

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatDialog
import android.view.View
import android.view.Window
import com.simplyapp.pixelskylivewallpaper.R
import com.simplyapp.pixelskylivewallpaper.view.ToggleColourView
import kotlinx.android.synthetic.main.dialog_colour_pick.*

class ColourPickerDialog(context: Context, private val startingColorList: IntArray) : AppCompatDialog(context) {

    private var isPickingColor = false
    private var currentColorViewId: Int? = null

    var onDialogFinished: ((colorArray: IntArray) -> Unit)? = null

    private val onColourClick = View.OnClickListener { view ->
        val colourView = view as ToggleColourView

        if (colourView.state == ToggleColourView.ToggleColorStates.ADD) {
            showColorPicker(true, null)
            currentColorViewId = colourView.getColorListId()
        } else {
            currentColorViewId = null
            colourView.setToggleState(ToggleColourView.ToggleColorStates.ADD)

        }
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_colour_pick)

        dialog_colour_picker.setInitialColor(Color.WHITE)
        dialog_colour_picker.setEnabledAlpha(false)

        getAllColorViews { view ->
            view.setOnClickListener(onColourClick)

            val colour = startingColorList.getOrNull(view.getColorListId())
            if (colour != null) {
                view.setToggleState(ToggleColourView.ToggleColorStates.CLEAR)
                view.setBackgroundColor(colour)
            }
        }

        dialog_colour_cancel.setOnClickListener {
            if (isPickingColor) {
                showColorPicker(false, null)
            } else {
                dismiss()
            }
        }
        dialog_colour_save.setOnClickListener {
            if (isPickingColor) {

                showColorPicker(false, dialog_colour_picker.color)
                currentColorViewId = null
            } else {
                val colorList = mutableListOf<Int?>()
                for (i in 0 until dialog_colour_layout.childCount) {
                    val view = dialog_colour_layout.getChildAt(i) as ToggleColourView
                    if (view.state == ToggleColourView.ToggleColorStates.CLEAR) {
                        colorList.add(view.colour)
                    }
                }
                onDialogFinished?.invoke(colorList.filterNotNull().toIntArray())
                dismiss()
            }
        }
    }

    private fun getAllColorViews(viewCallback: ((view: ToggleColourView) -> Unit)) {
        for (i in 0 until dialog_colour_layout.childCount) {
            viewCallback(dialog_colour_layout.getChildAt(i) as ToggleColourView)
        }
    }

    private fun showColorPicker(show: Boolean, color: Int?) {
        isPickingColor = show
        dialog_colour_picker.reset()
        if (show) {
            dialog_colour_layout.visibility = View.GONE
            dialog_colour_picker.visibility = View.VISIBLE
        } else {
            dialog_colour_layout.visibility = View.VISIBLE
            dialog_colour_picker.visibility = View.GONE
            if (currentColorViewId != null) {
                val view = dialog_colour_layout.findViewWithTag<ToggleColourView>(currentColorViewId.toString())
                if (color != null) {
                    view.setToggleState(ToggleColourView.ToggleColorStates.CLEAR)
                    view.setBackgroundColor(color)
                } else {
                    view.setToggleState(ToggleColourView.ToggleColorStates.ADD)
                }
            }
        }
    }
}