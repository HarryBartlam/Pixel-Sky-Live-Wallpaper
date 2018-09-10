package com.simplyapp.pixelskylivewallpaper.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import com.simplyapp.pixelskylivewallpaper.R
import com.simplyapp.pixelskylivewallpaper.utils.Colour
import com.simplyapp.pixelskylivewallpaper.utils.StarsPrefsHelper
import java.util.*

class SmartTogglePaletteButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : android.support.v7.widget.AppCompatButton(context, attrs, defStyleAttr) {

    private val otherButtons = ArrayList<SmartTogglePaletteButton>()
    private val paints = mutableListOf<Paint>()
    private val textBackgroundPaint = Paint().apply { this.color = Color.WHITE }
    private val textBounds = Rect()
    private val textBackgroundRect = RectF()
    private val textBackgroundPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
    private var textBackgroundRadus = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, resources.displayMetrics)

    private var textSelectedColour = Color.BLACK
    private var textUnSelectedColour = Color.WHITE
    private var textSelectedBkColour = Color.WHITE
    private var textUnSelectedBkColour = Color.BLACK

    val customPaletteId: String?
    val isCustomPalette: Boolean get() = customPaletteId.isNullOrEmpty().not()

    init {

        val array = context.obtainStyledAttributes(attrs, R.styleable.SmartTogglePaletteButton, defStyleAttr, 0)

        try {
            textSelectedColour = array.getColor(R.styleable.SmartTogglePaletteButton_textSelectedColour, Color.WHITE)
            textUnSelectedColour = array.getColor(R.styleable.SmartTogglePaletteButton_textUnselectedColour, Color.BLACK)
            textSelectedBkColour = array.getColor(R.styleable.SmartTogglePaletteButton_textSelectedBkColour, Color.BLACK)
            textUnSelectedBkColour = array.getColor(R.styleable.SmartTogglePaletteButton_textUnselectedBkColour, Color.WHITE)
        } catch (e: Exception) {

        } finally {
            array.recycle()
        }
        val colour = Colour.valueOf(tag as String)
        customPaletteId = colour.customId
        if (colour.customId.isNullOrEmpty()) {
            colour.colours.forEach { color ->
                paints.add(Paint().apply {
                    this.color = color
                })
            }
        } else {
            loadCustomColours()
        }

        viewTreeObserver.addOnGlobalLayoutListener {
            otherButtons.clear()
            val parent = parent as ViewGroup
            val count = parent.childCount
            for (i in 0 until count) {
                val child = parent.getChildAt(i)
                if (child is SmartTogglePaletteButton && child !== this@SmartTogglePaletteButton) {
                    otherButtons.add(child)
                }
            }
        }

        textBackgroundPaint.typeface = Typeface.DEFAULT// your preference here
        textBackgroundPaint.textSize = textSize
    }

    override fun onDraw(canvas: Canvas) {
        val colorWidth = (width - (paddingLeft + paddingRight)) / paints.size.toFloat()
        val viewTop = paddingTop.toFloat()
        val viewBottom = height - paddingBottom.toFloat()

        var start = paddingLeft.toFloat()
        paints.forEach {
            canvas.drawRect(start, viewTop, start + colorWidth, viewBottom, it)
            start += colorWidth
        }

        val widthCenter = width / 2f
        val heightCenter = height / 2f

        textBackgroundPaint.getTextBounds(text.toString(), 0, text.length, textBounds)

        val textHalfWidth = textBounds.width() / 2f + textBackgroundPadding
        val textHalfHeight = textBounds.height() / 2f + textBackgroundPadding

        textBackgroundRect.set(widthCenter - textHalfWidth, heightCenter - textHalfHeight, widthCenter + textHalfWidth, heightCenter + textHalfHeight)

        canvas.drawRoundRect(textBackgroundRect, textBackgroundRadus, textBackgroundRadus, textBackgroundPaint)

        super.onDraw(canvas)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            for (button in otherButtons) {
                button.isSelected = false
            }
        }
        setTextColor(if (selected) textSelectedColour else textUnSelectedColour)
        textBackgroundPaint.color = if (selected) textSelectedBkColour else textUnSelectedBkColour
        invalidate()
    }

    fun reloadCustomColours() {
        loadCustomColours()
        invalidate()
    }
    private fun loadCustomColours() {
        val prefs = StarsPrefsHelper(context)
        paints.clear()
        prefs.getCustomColorList(customPaletteId!!).forEach { color ->
            paints.add(Paint().apply {
                this.color = color
            })
        }
    }


}