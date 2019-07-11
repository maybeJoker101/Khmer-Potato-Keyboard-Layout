package com.maybejoker101.khmerpotatokeyboard.config

import android.content.Context
import android.content.SharedPreferences
import com.maybejoker101.khmerpotatokeyboard.database.SingletonHolder


class ViewConfig(
        var buttonHeight: Int = 40,
        var buttonMainFontSize: Int = 16,
        var buttonOtherFontSize: Int = 10,

        var buttonBackgroundColor: String = "#000000",
        var buttonBackgroundClickedColor: String = "#505050",
        var buttonMiddleTextColor: String = "#FFFFFF",
        var buttonOtherTextColor: String = "#aaaaaa",
        //popup config

        var popupMiddleTextSize: Int = 20,
        var popupPressedTextSize: Int = 24,
        var popupOtherTextSize: Int = 16,
        var popupHeightAndWidth: Int = 100,
        var popupBackGroundColor: String = "#aaaaaa") {


    companion object : SingletonHolder<ViewConfig, Context>({
        val sharePref: SharedPreferences = it.getSharedPreferences(SharePref.SHARE_PREF_NAME, Context.MODE_PRIVATE)

        val buttonHeight: Int
        val buttonMainFontSize: Int
        val buttonOtherFontSize: Int

        val buttonBackgroundColor: String
        val buttonBackgroundClickedColor: String
        val buttonMiddleTextColor: String
        val buttonOtherTextColor: String
        //popup config

        val popupMiddleTextSize: Int
        val popupPressedTextSize: Int
        val popupOtherTextSize: Int
        val popupHeightAndWidth: Int
        val popupBackGroundColor: String
        //check existence
        if (!sharePref.contains(SharePref.BUTTON_HEIGHT)) {
            val viewConfig = ViewConfig()

            sharePref
                    .edit()
                    .putInt(SharePref.BUTTON_HEIGHT, viewConfig.buttonHeight)
                    .putInt(SharePref.BUTTON_MAIN_FONT_SIZE, viewConfig.buttonMainFontSize)
                    .putInt(SharePref.BUTTON_OTHER_fONT_SIZE, viewConfig.buttonOtherFontSize)
                    .putString(SharePref.BUTTON_BACKGROUND_COLOR, viewConfig.buttonBackgroundColor)
                    .putString(SharePref.BUTTON_BACKGROUND_CLICKED_COLOR, viewConfig.buttonBackgroundClickedColor)
                    .putString(SharePref.BUTTON_MIDDLE_TEXT_COLOR, viewConfig.buttonMiddleTextColor)
                    .putString(SharePref.BUTTON_OTHER_TEXT_COLOR, viewConfig.buttonOtherTextColor)

                    .putInt(SharePref.POPUP_MIDDLE_TEXT_SIZE, viewConfig.popupMiddleTextSize)
                    .putInt(SharePref.POPUP_PRESSED_TEXT_SIZE, viewConfig.popupPressedTextSize)
                    .putInt(SharePref.POPUP_OTHER_TEXT_SIZE, viewConfig.popupOtherTextSize)
                    .putInt(SharePref.POPUP_HEIGHT_WIDTH, viewConfig.popupHeightAndWidth)
                    .putString(SharePref.POPUP_BACKGROUND_COLOR, viewConfig.popupBackGroundColor)
                    .apply()
            buttonHeight = viewConfig.buttonHeight
            buttonMainFontSize = viewConfig.buttonMainFontSize
            buttonOtherFontSize = viewConfig.buttonOtherFontSize

            buttonBackgroundColor = viewConfig.buttonBackgroundColor
            buttonBackgroundClickedColor = viewConfig.buttonBackgroundClickedColor
            buttonMiddleTextColor = viewConfig.buttonMiddleTextColor
            buttonOtherTextColor = viewConfig.buttonOtherTextColor
            //popup config

            popupMiddleTextSize = viewConfig.popupMiddleTextSize
            popupPressedTextSize = viewConfig.popupPressedTextSize
            popupOtherTextSize = viewConfig.popupOtherTextSize
            popupHeightAndWidth = viewConfig.popupHeightAndWidth
            popupBackGroundColor = viewConfig.popupBackGroundColor


        } else {
            buttonHeight = sharePref.getInt(SharePref.BUTTON_HEIGHT, 0)
            buttonMainFontSize = sharePref.getInt(SharePref.BUTTON_MAIN_FONT_SIZE, 0)
            buttonOtherFontSize = sharePref.getInt(SharePref.BUTTON_OTHER_fONT_SIZE, 0)

            buttonBackgroundColor = sharePref.getString(SharePref.BUTTON_BACKGROUND_COLOR, "")!!
            buttonBackgroundClickedColor = sharePref.getString(SharePref.BUTTON_BACKGROUND_CLICKED_COLOR, "")!!
            buttonMiddleTextColor = sharePref.getString(SharePref.BUTTON_MIDDLE_TEXT_COLOR, "")!!
            buttonOtherTextColor = sharePref.getString(SharePref.BUTTON_OTHER_TEXT_COLOR, "")!!
            //popup config

            popupMiddleTextSize = sharePref.getInt(SharePref.POPUP_MIDDLE_TEXT_SIZE, 0)
            popupPressedTextSize = sharePref.getInt(SharePref.POPUP_PRESSED_TEXT_SIZE, 0)
            popupOtherTextSize = sharePref.getInt(SharePref.POPUP_OTHER_TEXT_SIZE, 0)
            popupHeightAndWidth = sharePref.getInt(SharePref.POPUP_HEIGHT_WIDTH, 0)
            popupBackGroundColor = sharePref.getString(SharePref.POPUP_BACKGROUND_COLOR, "")!!
        }



        ViewConfig(
                buttonHeight = buttonHeight,
                buttonMainFontSize = buttonMainFontSize,
                buttonOtherFontSize = buttonOtherFontSize,
                buttonBackgroundColor = buttonBackgroundColor,
                buttonBackgroundClickedColor = buttonBackgroundClickedColor,
                buttonMiddleTextColor = buttonMiddleTextColor,
                buttonOtherTextColor = buttonOtherTextColor,

                popupMiddleTextSize = popupMiddleTextSize,
                popupPressedTextSize = popupPressedTextSize,
                popupHeightAndWidth = popupHeightAndWidth,
                popupOtherTextSize = popupOtherTextSize,
                popupBackGroundColor = popupBackGroundColor
        )
    })

}