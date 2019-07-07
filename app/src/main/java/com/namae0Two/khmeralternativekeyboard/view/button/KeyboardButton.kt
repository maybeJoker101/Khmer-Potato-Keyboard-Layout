package com.namae0Two.khmeralternativekeyboard.view.button

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonData

abstract class KeyboardButton(context: Context?, var buttonData: ButtonData) : androidx.constraintlayout.widget.ConstraintLayout(context) {
    fun changeBackground(pressed: Boolean) {
        if (pressed) {
            setBackgroundResource(R.color.colorKeyBackgroundDefaultClicked)
        } else if (!pressed) {
            setBackgroundResource(R.color.colorKeyBackgroundDefault)

        }
    }
}