package com.maybejoker101.khmerpotatokeyboard.view.button

import android.content.Context
import android.graphics.Color
import com.maybejoker101.khmerpotatokeyboard.config.ViewConfig
import com.maybejoker101.khmerpotatokeyboard.data.ButtonData

abstract class KeyboardButton(context: Context?, var buttonData: ButtonData) : androidx.constraintlayout.widget.ConstraintLayout(context) {

    val viewConfig: ViewConfig?
    val backgroundColor: Int
    val backgroundColorClicked: Int

    init {
        viewConfig = ViewConfig.getInstance(context!!)
        backgroundColor = Color.parseColor(viewConfig.buttonBackgroundColor)
        backgroundColorClicked = Color.parseColor(viewConfig.buttonBackgroundClickedColor)
    }

    fun changeBackground(pressed: Boolean) {
        if (pressed) {
            setBackgroundColor(backgroundColorClicked)
        } else if (!pressed) {
            setBackgroundColor(backgroundColor)

        }
    }
}