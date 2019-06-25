package com.namae0Two.khmeralternativekeyboard.view

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonData
import com.namae0Two.khmeralternativekeyboard.util.Util

class CharacterButtonView(context: Context?, var buttonData: ButtonData, var rowHeight:Int) : ConstraintLayout(context) {

    companion object {
        val  DEBUG_TAG = "CHARACTER_BUTTON"

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

        val lp = LinearLayout.LayoutParams(0, Util.getPixelFromDp(rowHeight,context))
        lp.weight = buttonData.weight.toFloat()
        layoutParams = lp




        val resource = context!!.resources
        //dimensions
        startEndMargin  = resource.getInteger(R.integer.leftAndRightKeyButtonMarginNoUnit).toFloat()
        middleTextSize = resource.getInteger(R.integer.keyContentPrimarySizeNoUnit).toFloat()
        otherTextSize = resource.getInteger(R.integer.keyContentSecondarySizeNoUnit).toFloat()

        //color
        middleColor = ContextCompat.getColor(context, R.color.colorKeyContentPrimaryDefault)
        otherColor = ContextCompat.getColor(context, R.color.colorKeyContentSecondaryDefault)

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

        setBackgroundResource(R.color.colorKeyBackgroundDefault)



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


        //Left
        val leftParams = leftText.layoutParams as LayoutParams
        leftParams.startToStart = id
        leftParams.topToTop = id
        leftParams.bottomToBottom = id
        leftParams.marginStart = startEndMargin.toInt()

    }


    fun changeBackground(touch:Boolean,context:Context){

        if (touch) {
            setBackgroundResource(R.color.colorKeyBackgroundDefaultClicked)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = 5.0f
            }
        }
        else if (!touch){
            setBackgroundResource(R.color.colorKeyBackgroundDefault)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = 0.0f
            }

        }
    }
    //TODO Testing Popup

    private fun popup(context: Context) : PopupWindow{
        val inflater:LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_preview_layout,null)



        val focusable = false

        val popupWindow = PopupWindow(view, 200,200,focusable);
        popupWindow.isClippingEnabled = false
        popupWindow.showAtLocation(this, Gravity.CENTER,0,0)

        return popupWindow
    }
}