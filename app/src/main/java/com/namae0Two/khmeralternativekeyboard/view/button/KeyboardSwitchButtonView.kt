package com.namae0Two.khmeralternativekeyboard.view.button

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonData
import com.namae0Two.khmeralternativekeyboard.util.Util

class KeyboardSwitchButtonView(context: Context?, buttonData: ButtonData, rowHeight: Int) : KeyboardButton(context, buttonData) {

    var textColor: Int
    var textSize: Float

    var keyboardSwitcherContent: TextView

    init {

        //ID
        id = View.generateViewId()

        //layout Params
        val lp = LinearLayout.LayoutParams(0, Util.getPixelFromDp(rowHeight, context))
        lp.weight = buttonData.weight.toFloat()
        layoutParams = lp


        //Color
        textColor = Color.parseColor(viewConfig!!.buttonMiddleTextColor)
        //text Size
        textSize = viewConfig.buttonMainFontSize.toFloat()

        //View
        keyboardSwitcherContent = TextView(context)

        //View Content

        keyboardSwitcherContent.text = buttonData.middle

        //Color
        keyboardSwitcherContent.setTextColor(textColor)
        //Size
        keyboardSwitcherContent.textSize = textSize


        //layout params
        val layoutParams: LayoutParams = generateDefaultLayoutParams()

        layoutParams.bottomToBottom = id
        layoutParams.endToEnd = id
        layoutParams.startToStart = id
        layoutParams.topToTop = id

        keyboardSwitcherContent.layoutParams = layoutParams

        addView(keyboardSwitcherContent)

        //Background
        setBackgroundColor(backgroundColor)

    }

    class KeyboardSwitchButtonTouchListener : OnLongClickListener, OnTouchListener {

        var longPressed = false

        var onDownOperation: () -> Unit
        var onUpOperation: () -> Unit
        var onLongPressOperation: () -> Unit

        constructor(downOp: () -> Unit, upOp: () -> Unit, longOp: () -> Unit) {
            onDownOperation = downOp
            onUpOperation = upOp
            onLongPressOperation = longOp
        }

        override fun onLongClick(v: View?): Boolean {
            longPressed = true
            onLongPressOperation()
            return true
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            val view = v as KeyboardSwitchButtonView
            when (event?.actionMasked) {

                MotionEvent.ACTION_DOWN -> {
                    view.changeBackground(true)
                    onDownOperation()
                }
                MotionEvent.ACTION_UP -> {
                    onUpOperation()
                    view.changeBackground(false)
                    reset()

                }
            }


            return false
        }

        private fun reset() {
            longPressed = false
        }


    }
}