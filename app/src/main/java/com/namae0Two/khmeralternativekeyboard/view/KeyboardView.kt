package com.namae0Two.khmeralternativekeyboard.view

import android.content.Context
import android.os.Build
import android.os.Handler
import android.support.constraint.ConstraintLayout
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

class KeyboardView(context: Context) : ConstraintLayout(context), View.OnTouchListener {


    companion object {
        val DEBUG_TAG = "keyboardView"

    }


    var keyboardData: KeyboardData
    var popupView: CardView
    var popupWindows: PopupWindow


    //Child
    var topPlaceholder: View
    var keyboardContent: LinearLayout

    //functioning parameter
    private var keyPressedViewId = -1
    //params to make sure that popup windows is shown for atleast 200ms
    private var dimissable = true

    init {
        id = generateViewId()

        layoutParams = LayoutParams(MATCH_PARENT, 500)

        //Read Keyboard Data
        keyboardData = KeyboardData.keyboardDataInstanceFromAsset(context)


        ///PopupWindows
        val inflater: LayoutInflater = LayoutInflater.from(context)
        popupView = inflater.inflate(R.layout.popup_preview_layout, null, false) as CardView
//        popupView.layoutParams.height = Util.getPixelFromDp(100,context)
//        popupView.layoutParams.width = Util.getPixelFromDp(100,context)

        val measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        popupView.measure(measureSpec, measureSpec)
        popupWindows = PopupWindow(context)
        popupWindows.width = popupView.measuredWidth
        popupWindows.height = popupView.measuredHeight
        popupWindows.contentView = popupView

        popupWindows.isClippingEnabled = false

        //Children = KeyboardContent and top placeholder

        val topPlaceholderParams = LayoutParams(MATCH_PARENT, 500)
        val keyboardContentParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        keyboardContent = LinearLayout(context)
        keyboardContent.id = View.generateViewId()
        keyboardContentParams.bottomToBottom = id
        keyboardContent.layoutParams = keyboardContentParams
        keyboardContent.orientation = LinearLayout.VERTICAL


        topPlaceholder = View(context)
        //Color Debug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            topPlaceholder.setBackgroundColor(context.getColor(R.color.background_material_dark))
        }
        topPlaceholder.id = View.generateViewId()

        topPlaceholderParams.bottomToTop = keyboardContent.id

        topPlaceholder.layoutParams = topPlaceholderParams


        addView(topPlaceholder)
        addView(keyboardContent)


        //End Children


        for (row in keyboardData.rows) {

            val rowParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            val rowLayout = LinearLayout(context)
            rowLayout.layoutParams = rowParams
            rowLayout.orientation = LinearLayout.HORIZONTAL
            for (button in row.buttons) {

                val button = CharacterButtonView(context, button, row.height)
                button.setOnTouchListener(this)
                rowLayout.addView(button)
            }
            keyboardContent.addView(rowLayout)
        }


    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {


        val c = v as CharacterButtonView

        val mask = event?.actionMasked

        Log.d(DEBUG_TAG,"Mask is "+ mask.toString())
        when (mask) {
            MotionEvent.ACTION_DOWN -> {
                if (keyPressedViewId == -1) {
                    layoutAndShowPopupWindow(c)
                }
            }//Do Something
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (keyPressedViewId == v.id) {
                    dismissPopupWindows(c)
                }
            }
            MotionEvent.ACTION_OUTSIDE -> return false
        }
        return true
    }

    fun layoutAndShowPopupWindow(view: CharacterButtonView) {
        //make sure only one keyPressed press at a time
        keyPressedViewId = view.id
        //change color of keyPressed key
        view.changeBackground(true)

        //content of popupWindow
        popupView.findViewById<TextView>(R.id.popup_middle_big).text = view.buttonData.middle
        popupView.findViewById<TextView>(R.id.popup_middle_primary).text = view.buttonData.middle
        popupView.findViewById<TextView>(R.id.popup_top).text = view.buttonData.top
        popupView.findViewById<TextView>(R.id.popup_right).text = view.buttonData.right
        popupView.findViewById<TextView>(R.id.popup_bottom).text = view.buttonData.bottom
        popupView.findViewById<TextView>(R.id.popup_left).text = view.buttonData.left
        var viewLocation = IntArray(2)
        view.getLocationInWindow(viewLocation)

        val view_x = viewLocation[0]
        val view_y = viewLocation[1]
        val view_width = view.width
        val parent_width = this.width

        Log.d(DEBUG_TAG, "x = " + view_x + " y = " + view_y + " width = " + view_width + " Parent = "+ parent_width)


        var x = view_x + (  view.width-popupWindows.width)/2
        val y = view_y - 300

        if (x < 0 ) {
            x = 0
        }else if (x > parent_width - popupWindows.width ){
            x = parent_width  - popupWindows.width
        }




        //show Popup Windows
        popupWindows.isTouchable = false
        popupWindows.showAtLocation(view, Gravity.NO_GRAVITY, x, y)


        //TODO Check Wether Dimissable is needed or not
//        dimissable = false
//
//        Handler().postDelayed({
//            dimissable = true
//        },10)

    }

    fun dismissPopupWindows(view: CharacterButtonView) {
        //set keyPressed back to not keyPressed
        // if dismissable dismiss else rerun this function again after 50ms
//        if(dimissable) {

            keyPressedViewId = -1
            //change color back
            view.changeBackground(false)
            //dismiss popupwindwos
            popupWindows.dismiss()

//        }
//        else {
//            Handler().postDelayed({
//                dismissPopupWindows(view)
//            },10)
//        }
    }

}