package com.namae0Two.khmeralternativekeyboard.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonData
import com.namae0Two.khmeralternativekeyboard.util.Util

class SpaceButtonView(context: Context?, buttonData: ButtonData, rowHeight: Int) : KeyboardButton(context, buttonData) {

    var textColor: Int
    var textSize: Float

    var spaceText: TextView

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
        spaceText = TextView(context)

        //View Content

        spaceText.text = buttonData.middle

        //Color
        spaceText.setTextColor(textColor)
        //Size
        spaceText.textSize = textSize


        //layout params
        val layoutParams: LayoutParams = generateDefaultLayoutParams()

        layoutParams.bottomToBottom = id
        layoutParams.endToEnd = id
        layoutParams.startToStart = id
        layoutParams.topToTop = id

        spaceText.layoutParams = layoutParams

        addView(spaceText)

        //Background
        setBackgroundResource(R.color.colorKeyBackgroundDefault)

    }

}
