package com.simplyapp.pixelskylivewallpaper.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.simplyapp.pixelskylivewallpaper.R
import kotlinx.android.synthetic.main.view_colour_toggle.view.*

class ToggleColourView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    enum class ToggleColorStates {
        ADD, CLEAR
    }

    var state = ToggleColorStates.ADD

    var colour: Int? = null

    init {
        View.inflate(getContext(), R.layout.view_colour_toggle, this)
    }

    fun setToggleState(state: ToggleColorStates) {
        this.state = state
        if (this.state == ToggleColorStates.ADD) {
            colour = null
            toggle_colour_icon.setImageResource(R.drawable.ic_add)
            toggle_colour_background.setBackgroundColor(Color.WHITE)
        } else {
            toggle_colour_icon.setImageResource(R.drawable.ic_clear)
        }
    }

    override fun setBackgroundColor(color: Int) {
        this.colour = color
        toggle_colour_background.setBackgroundColor(color)
    }

    fun getColorListId(): Int {
        return (tag as String).toInt()
    }


}