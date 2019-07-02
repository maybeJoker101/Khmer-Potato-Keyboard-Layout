package com.namae0Two.khmeralternativekeyboard.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonData

abstract class KeyboardButton(context: Context?, var buttonData: ButtonData) : ConstraintLayout(context) {
    fun changeBackground(pressed: Boolean) {
        if (pressed) {
            setBackgroundResource(R.color.colorKeyBackgroundDefaultClicked)
        } else if (!pressed) {
            setBackgroundResource(R.color.colorKeyBackgroundDefault)

        }
    }
}