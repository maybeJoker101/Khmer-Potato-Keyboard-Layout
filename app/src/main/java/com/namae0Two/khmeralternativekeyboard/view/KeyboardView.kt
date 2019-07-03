package com.namae0Two.khmeralternativekeyboard.view

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonType
import com.namae0Two.khmeralternativekeyboard.data.KeyboardData
import com.namae0Two.khmeralternativekeyboard.util.Util

class KeyboardView(context: Context, val inputService: InputMethodService) : ConstraintLayout(context), View.OnTouchListener {


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
    private var characterButtonDirection: Int = -1

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

        ///Touch Listener

        //lamda
        val charBtnDownOp: (CharacterButtonView) -> Unit = {
            onCharacterButtonDown(it)
        }
        val charBtnUpOp: (CharacterButtonView) -> Unit = {
            onCharacterButtonUp(it)
        }
        val chatBtnActionOp: (CharacterButtonView, Int) -> Unit = { view, direction -> onCharacterButtonAction(view, direction) }
        val characterBtnTouchListener: CharacterButtonView.CharacterButtonTouchListener =
                CharacterButtonView.CharacterButtonTouchListener(charBtnDownOp, charBtnUpOp, chatBtnActionOp)

        ///


        for (row in keyboardData.rows) {

            val rowParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            val rowLayout = LinearLayout(context)
            rowLayout.layoutParams = rowParams
            rowLayout.orientation = LinearLayout.HORIZONTAL
            for (buttonData in row.buttons) {

                var button: ConstraintLayout? = null
                when (buttonData.buttonType) {
                    ButtonType.ALPHABET_TYPE, ButtonType.NUMBER_AND_SIGN -> {
                        button = CharacterButtonView(context, buttonData, row.height)
                        button.setOnTouchListener(characterBtnTouchListener)
                        button.setOnLongClickListener(characterBtnTouchListener)

                    }
                    ButtonType.SPACE -> {
                        button = SpaceButtonView(context, buttonData, row.height)
                    }
                    ButtonType.BACKSPACE -> {
                        button = BackSpaceButtonView(context, buttonData, row.height)

                    }
                    ButtonType.ENTER -> {
                        button = EnterButtonView(context, buttonData, row.height)

                    }
                    ButtonType.KEYBOARD_SWITCH -> {
                        button = KeyboardSwitchButtonView(context, buttonData, row.height)

                    }
                }

//                button.setOnTouchListener(this)

                rowLayout.addView(button)
            }
            keyboardContent.addView(rowLayout)
        }


    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {


        val c = v as KeyboardButton

        return when (c.buttonData.buttonType) {
            ButtonType.ALPHABET_TYPE -> {
                true
            }
            ButtonType.NUMBER_AND_SIGN -> {
                onDigitsAndSignButtonTouch(v, event)
            }
            ButtonType.SPACE -> {
                onSpaceButtonTouch(v, event)

            }
            ButtonType.BACKSPACE -> {
                onBackSpaceTouch(v, event)


            }
            ButtonType.ENTER -> {
                onEnterTouch(v, event)


            }
            ButtonType.KEYBOARD_SWITCH -> {
                onKeyboardSwitchTouch(v, event)

            }
        }
    }

    //Character Button Related Function
    fun onCharacterButtonDown(view: CharacterButtonView) {
        //if keypress is other then -1 stop operation return
        if (keyPressedViewId != -1) {
            return
        }
        layoutAndShowPopupWindow(view)
    }

    fun onCharacterButtonUp(view: CharacterButtonView) {
        //Return if the id is different from the first clicked one
        if (keyPressedViewId != view.id) {
            return
        }
        dismissPopupWindows(view)

        var text = ""
        when (characterButtonDirection) {
            0 -> text = view.buttonData.middle
            1 -> text = view.buttonData.top
            2 -> text = view.buttonData.right
            3 -> text = view.buttonData.bottom
            4 -> text = view.buttonData.left

        }
        if (text.isEmpty()) {
            text = view.buttonData.middle
        }
        commitText(text)
    }

    fun onCharacterButtonAction(view: CharacterButtonView, direction: Int) {
        //Return if the id is different from the first clicked one
        if (keyPressedViewId != view.id) {
            return
        }


        changePopupContent(view, direction)
    }
    ///END character Button related function


    //Input Connection Related Method
    fun commitText(text: String) {

        val inputConnection = inputService.currentInputConnection
        //reset direction
        inputConnection.commitText(text, 1)
        characterButtonDirection = -1
    }

    private fun onSpaceButtonTouch(v: View?, event: MotionEvent?): Boolean {
        val spaceButton = v as SpaceButtonView
        val mask = event?.actionMasked

        when (mask) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(DEBUG_TAG, "Space is touched")
                keyPressedViewId = v.id
                spaceButton.changeBackground(true)
            }//Do Something
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.d(DEBUG_TAG, "Space is untouched")
                keyPressedViewId = -1

                spaceButton.changeBackground(false)

            }
            MotionEvent.ACTION_OUTSIDE -> return false
        }
        return true
    }

    private fun onBackSpaceTouch(v: View?, event: MotionEvent?): Boolean {

        return false
    }

    private fun onEnterTouch(v: View?, event: MotionEvent?): Boolean {

        return false
    }

    private fun onDigitsAndSignButtonTouch(v: View?, event: MotionEvent?): Boolean {

        return false
    }

    private fun onKeyboardSwitchTouch(v: View?, event: MotionEvent?): Boolean {

        return false
    }


    //Popup related Function
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
        val viewLocation = IntArray(2)
        view.getLocationInWindow(viewLocation)

        val viewX = viewLocation[0]
        val viewY = viewLocation[1]
        val viewWidth = view.width
        val parentWidth = this.width

        Log.d(DEBUG_TAG, "x = " + viewX + " y = " + viewY + " width = " + viewWidth + " Parent = " + parentWidth)

        var x = viewX + (view.width - popupWindows.width) / 2
        val y = viewY - Util.getPixelFromDp(100, context)

        if (x < 0) {
            x = 0
        } else if (x > parentWidth - popupWindows.width) {
            x = parentWidth - popupWindows.width
        }
        //show Popup Windows
        popupWindows.isTouchable = false
        popupWindows.showAtLocation(view, Gravity.NO_GRAVITY, x, y)

    }

    fun dismissPopupWindows(view: CharacterButtonView) {


        keyPressedViewId = -1
        //change color back
        view.changeBackground(false)
        //dismiss popupwindwos
        resetPopupContent()
        popupWindows.dismiss()


    }

    //Change Popup Content Depend on Direction
    //direction is from 0.. 4,
    //0 middle
    //1 top
    //2 right
    //3 bottom
    //4 left
    fun changePopupContent(view: CharacterButtonView, direction: Int) {

        //Character Button Direction
        characterButtonDirection = direction

        popupView.findViewById<TextView>(R.id.popup_middle_primary).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_top).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_right).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_bottom).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_left).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_middle_big).visibility = View.VISIBLE
        var text = ""
        when (direction) {
            0 -> text = view.buttonData.middle
            1 -> text = view.buttonData.top
            2 -> text = view.buttonData.right
            3 -> text = view.buttonData.bottom
            4 -> text = view.buttonData.left

        }
        if (text.isEmpty()) {
            text = view.buttonData.middle
            // if the
            characterButtonDirection = 0
        }
        Log.d(DEBUG_TAG, "change popupcontent $text")
        popupView.findViewById<TextView>(R.id.popup_middle_big).text = text
    }

    private fun resetPopupContent() {
        popupView.findViewById<TextView>(R.id.popup_middle_big).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_middle_primary).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_top).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_right).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_bottom).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_left).visibility = View.VISIBLE
    }

    ///OnTouch Listener


}