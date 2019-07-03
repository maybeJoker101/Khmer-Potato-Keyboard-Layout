package com.namae0Two.khmeralternativekeyboard.view

import android.content.Context
import android.support.v4.content.ContextCompat
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
        val resource = context!!.resources

        textColor = ContextCompat.getColor(context, R.color.colorKeyContentPrimaryDefault)

        textSize = resource.getInteger(R.integer.keyContentPrimarySizeNoUnit).toFloat()

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
        setBackgroundResource(R.color.colorKeyBackgroundDefault)

    }
}