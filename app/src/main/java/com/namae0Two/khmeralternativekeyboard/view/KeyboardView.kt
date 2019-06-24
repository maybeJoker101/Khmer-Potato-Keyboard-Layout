package com.namae0Two.khmeralternativekeyboard.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.KeyboardData

class KeyboardView(context: Context) : LinearLayout(context), View.OnTouchListener {


    companion object {
        val DEBUG_TAG = "keyboard"

    }

    var keyboardData: KeyboardData
    var popupView: CardView
    var popupWindows :PopupWindow
    init {
        id = generateViewId()

        //Read Keyboard Data
        keyboardData = KeyboardData.keyboardDataInstanceFromAsset(context)

        val inflater: LayoutInflater =  LayoutInflater.from(context)
        popupView  = inflater.inflate(R.layout.popup_preview_layout,null,false) as CardView
//        popupView.layoutParams.height = Util.getPixelFromDp(100,context)
//        popupView.layoutParams.width = Util.getPixelFromDp(100,context)

        val measureSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED)
        popupView.measure(measureSpec,measureSpec)
        popupWindows = PopupWindow(context)
        popupWindows.width = popupView.measuredWidth
        popupWindows.height = popupView.measuredHeight
        popupWindows.contentView = popupView

        popupWindows.isClippingEnabled = false



        for (row in keyboardData.rows) {

            val rowParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            val rowLayout = LinearLayout(context)
            rowLayout.layoutParams = rowParams
            rowLayout.orientation = HORIZONTAL
            addView(rowLayout)
            for (button in row.buttons) {

                val button = CharacterButtonView(context, button, row.height)
                button.setOnTouchListener(this)
                rowLayout.addView(button)
            }
        }
        orientation = VERTICAL


    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val c = v as CharacterButtonView
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                c.changeBackground(true, context)
                layoutAndShowPopupWindow(c)
            }//Do Something
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                c.changeBackground(false, context)
                popupWindows.dismiss()
            }
            MotionEvent.ACTION_OUTSIDE -> return false
        }
        Log.d(CharacterButtonView.DEBUG_TAG, event?.action.toString())
        return true
    }

    fun layoutAndShowPopupWindow(view: CharacterButtonView){

        popupView.findViewById<TextView>(R.id.popup_middle_big).text = view.buttonData.middle
        popupView.findViewById<TextView>(R.id.popup_middle_primary).text = view.buttonData.middle
        popupView.findViewById<TextView>(R.id.popup_top).text = view.buttonData.top
        popupView.findViewById<TextView>(R.id.popup_right).text = view.buttonData.right
        popupView.findViewById<TextView>(R.id.popup_bottom).text = view.buttonData.bottom
        popupView.findViewById<TextView>(R.id.popup_left).text = view.buttonData.left

        val x = view.left + (view.width - popupView.width)/2
        val y = view.top - popupView.height
        popupWindows.showAtLocation(this,Gravity.CENTER,0,-400)

    }

}