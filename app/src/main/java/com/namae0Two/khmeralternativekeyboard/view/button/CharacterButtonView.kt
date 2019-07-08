package com.namae0Two.khmeralternativekeyboard.view.button

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.config.ViewConfig
import com.namae0Two.khmeralternativekeyboard.data.ButtonData
import com.namae0Two.khmeralternativekeyboard.util.Util
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class CharacterButtonView(context: Context?, buttonData: ButtonData, rowHeight: Int) : KeyboardButton(context, buttonData) {

    companion object {
        val DEBUG_TAG = "CHARACTER_BUTTON"

    }

    var startEndMargin: Float
    var middleTextSize: Float
    var otherTextSize: Float

    var middleColor: Int
    var otherColor: Int

    //TextView

    var middleText: TextView
    var topText: TextView
    var rightText: TextView
    var bottomText: TextView
    var leftText: TextView


    init {

        //get ID
        id = View.generateViewId()

        val lp = LinearLayout.LayoutParams(0, Util.getPixelFromDp(rowHeight, context))
        lp.weight = buttonData.weight.toFloat()
        layoutParams = lp


        val resource = context!!.resources
        //dimensions
        startEndMargin = resource.getInteger(R.integer.leftAndRightKeyButtonMarginNoUnit).toFloat()


        middleTextSize = viewConfig!!.buttonMainFontSize.toFloat()
        otherTextSize = viewConfig.buttonOtherFontSize.toFloat()
        //color
        middleColor = Color.parseColor(viewConfig.buttonMiddleTextColor)
        otherColor = Color.parseColor(viewConfig.buttonOtherTextColor)

        //init TextView
        middleText = TextView(context)
        topText = TextView(context)
        rightText = TextView(context)
        bottomText = TextView(context)
        leftText = TextView(context)

        //Set TextView Params
        //Text
        middleText.text = buttonData.middle
        topText.text = buttonData.top
        rightText.text = buttonData.right
        bottomText.text = buttonData.bottom
        leftText.text = buttonData.left
        //Color
        middleText.setTextColor(middleColor)
        topText.setTextColor(otherColor)
        rightText.setTextColor(otherColor)
        bottomText.setTextColor(otherColor)
        leftText.setTextColor(otherColor)

        //textSize
        middleText.textSize = middleTextSize
        topText.textSize = otherTextSize
        rightText.textSize = otherTextSize
        bottomText.textSize = otherTextSize
        leftText.textSize = otherTextSize

        //Layout Params
        modifyParams()


        addView(middleText)
        addView(topText)
        addView(rightText)
        addView(bottomText)
        addView(leftText)

        setBackgroundColor(backgroundColor)

    }

    private fun modifyParams() {


        middleText.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        topText.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        rightText.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        bottomText.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        leftText.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        //Middle
        val midParams = middleText.layoutParams as LayoutParams
        midParams.startToStart = id
        midParams.topToTop = id
        midParams.endToEnd = id
        midParams.bottomToBottom = id

        //Top
        val topParams = topText.layoutParams as LayoutParams
        topParams.startToStart = id
        topParams.topToTop = id
        topParams.endToEnd = id
        topParams.topMargin = startEndMargin.toInt()

        //Right
        val rightParams = rightText.layoutParams as LayoutParams
        rightParams.topToTop = id
        rightParams.endToEnd = id
        rightParams.bottomToBottom = id
        rightParams.marginEnd = startEndMargin.toInt()

        //Bottom
        val bottomParams = bottomText.layoutParams as LayoutParams
        bottomParams.startToStart = id
        bottomParams.endToEnd = id
        bottomParams.bottomToBottom = id
        bottomParams.bottomMargin = startEndMargin.toInt()


        //Left
        val leftParams = leftText.layoutParams as LayoutParams
        leftParams.startToStart = id
        leftParams.topToTop = id
        leftParams.bottomToBottom = id
        leftParams.marginStart = startEndMargin.toInt()

    }


    class CharacterButtonTouchListener :
            OnTouchListener, OnLongClickListener {

        companion object {
            val DEBUG_TAG = "charBtnTouchListener"

        }

        var onDownOperation: (CharacterButtonView) -> Unit
        var onUpOperation: (CharacterButtonView) -> Unit
        var onActionOperation: (CharacterButtonView, Int) -> Unit

        var currentViewId = -1
        var downX: Double = 0.0
        var downY: Double = 0.0
        var direction = -1
        var longPressed = false
        //Distance Threshold
        private var moveThreshold: Int = -1

        constructor(downOp: (CharacterButtonView) -> Unit
                    , upOp: (CharacterButtonView) -> Unit
                    , actionOp: (CharacterButtonView, Int) -> Unit
        ) {
            onDownOperation = downOp
            onUpOperation = upOp
            onActionOperation = actionOp
        }

        override fun onLongClick(v: View?): Boolean {
            longPressed = true

            if (direction == -1) {
                Log.d(DEBUG_TAG, "OnLong Press Character Btn")
                onActionOperation(v as CharacterButtonView, 0)
                direction = 0
            }
            return true
        }


        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            //Set moveThreshold
            if (moveThreshold == -1) {
                val width = v?.width!!
                val height = v.height

                Log.d(DEBUG_TAG, "Width $width Height $height")

                if (width > height) {
                    moveThreshold = width / 2
                } else {
                    moveThreshold = height / 2
                }
                moveThreshold += 10
            }


            val viewId = v?.id!!

            if (currentViewId != -1 && viewId != currentViewId) {
                return true
            }

            val eventX: Double = event?.x!!.toDouble()
            val eventY: Double = event.y.toDouble()
            when (event.action) {
                //UP
                MotionEvent.ACTION_UP -> {

                    if (currentViewId == viewId) {
                        onUpOperation(v as CharacterButtonView)
                        reset()
                    }

                }
                MotionEvent.ACTION_DOWN -> {
                    if (currentViewId == -1) {
                        v.performClick()
                        currentViewId = viewId
                        onDownOperation(v as CharacterButtonView)
                        downX = eventX
                        downY = eventY
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val angle = Math.toDegrees(atan2(downY - eventY, eventX - downX)).toInt()
                    val distance = sqrt((downX - eventX).pow(2.0) + (downY - eventY).pow(2.0))


                    if (distance > moveThreshold) {
                        when (angle) {
                            in 46..135 -> {
//                                Log.d(DEBUG_TAG, "MOVE UP $distance")
                                onMove(v as CharacterButtonView, 1)
                            }
                            in -44..45 -> {
//                                Log.d(DEBUG_TAG, "MOVE RIGHT $distance")
                                onMove(v as CharacterButtonView, 2)

                            }
                            in -134..-45 -> {
//                                Log.d(DEBUG_TAG, "MOVE DOWN $distance")
                                onMove(v as CharacterButtonView, 3)

                            }
                            in -181..-135, in 136..181 -> {

//                                Log.d(DEBUG_TAG, "MOVE LEFT $distance")
                                onMove(v as CharacterButtonView, 4)
                            }
                        }
                    } else {
                        //if below distance Threshold and is longpressed
                        if (longPressed) {
                            onMove(v as CharacterButtonView, 0)
                        }
                    }
                    return true
                }
            }
            return false
        }

        private fun onMove(v: CharacterButtonView, inputDirection: Int) {
            longPressed = true
            if (direction != inputDirection) {
                direction = inputDirection
                onActionOperation(v, inputDirection)
            }
        }

        private fun reset() {
            currentViewId = -1
            downX = 0.0
            downY = 0.0
            direction = -1
            longPressed = false
        }

    }
}