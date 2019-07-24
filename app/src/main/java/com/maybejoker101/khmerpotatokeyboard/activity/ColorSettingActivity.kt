package com.maybejoker101.khmerpotatokeyboard.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NavUtils
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.maybejoker101.khmerpotatokeyboard.R
import com.maybejoker101.khmerpotatokeyboard.config.SharePref
import com.maybejoker101.khmerpotatokeyboard.config.ViewConfig
import com.maybejoker101.khmerpotatokeyboard.data.ButtonData
import com.maybejoker101.khmerpotatokeyboard.data.ButtonType
import com.maybejoker101.khmerpotatokeyboard.view.button.CharacterButtonView


class ColorSettingActivity : AppCompatActivity() {


    private var viewConfig: ViewConfig? = null


    private var middleTextColorView: View? = null
    private var otherTextColorView: View? = null
    private var keyBackgroundColorView: View? = null
    private var keyBackgroundClickedColorView: View? = null
    private var popUpColorView: View? = null

    private var keyButtonFrame: LinearLayout? = null
    private var keyButton: CharacterButtonView? = null
    private var keyButton2: CharacterButtonView? = null

    private var popUpParent: ConstraintLayout? = null
    private var popupMiddleText: TextView? = null
    private var popupTopText: TextView? = null
    private var popupRightText: TextView? = null
    private var popupBottomText: TextView? = null
    private var popupLeftText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_setting)
        viewConfig = ViewConfig.getInstance(applicationContext)

        middleTextColorView = findViewById(R.id.viewTextMiddleColor)
        otherTextColorView = findViewById(R.id.viewTextOtherColor)
        keyBackgroundColorView = findViewById(R.id.viewTextKeyBackgroundColor)
        keyBackgroundClickedColorView = findViewById(R.id.viewTextKeyBackgroundClickedColor)
        popUpColorView = findViewById(R.id.viewPopupBackgroundColor)


        keyButtonFrame = findViewById(R.id.characterButtonFrame)

        //Popup Card
        popUpParent = findViewById(R.id.popUpParentLayout)
        popupTopText = findViewById(R.id.popup_top)
        popupMiddleText = findViewById(R.id.popup_middle_primary)
        popupRightText = findViewById(R.id.popup_right)
        popupBottomText = findViewById(R.id.popup_bottom)
        popupLeftText = findViewById(R.id.popup_left)

        //set color

        middleTextColorView?.setBackgroundColor(Color.parseColor(viewConfig!!.buttonMiddleTextColor))
        otherTextColorView?.setBackgroundColor(Color.parseColor(viewConfig!!.buttonOtherTextColor))
        keyBackgroundColorView?.setBackgroundColor(Color.parseColor(viewConfig!!.buttonBackgroundColor))
        keyBackgroundClickedColorView?.setBackgroundColor(Color.parseColor(viewConfig!!.buttonBackgroundClickedColor))
        popUpColorView?.setBackgroundColor(Color.parseColor(viewConfig!!.popupBackGroundColor))

        popUpParent!!.setBackgroundColor(Color.parseColor(viewConfig!!.popupBackGroundColor))
        popupMiddleText!!.setTextColor(Color.parseColor(viewConfig!!.buttonMiddleTextColor))
        popupTopText!!.setTextColor(Color.parseColor(viewConfig!!.buttonOtherTextColor))
        popupRightText!!.setTextColor(Color.parseColor(viewConfig!!.buttonOtherTextColor))
        popupBottomText!!.setTextColor(Color.parseColor(viewConfig!!.buttonOtherTextColor))
        popupLeftText!!.setTextColor(Color.parseColor(viewConfig!!.buttonOtherTextColor))

        popupMiddleText!!.textSize = viewConfig!!.popupMiddleTextSize.toFloat()
        popupTopText!!.textSize = viewConfig!!.popupOtherTextSize.toFloat()
        popupRightText!!.textSize = viewConfig!!.popupOtherTextSize.toFloat()
        popupBottomText!!.textSize = viewConfig!!.popupOtherTextSize.toFloat()
        popupLeftText!!.textSize = viewConfig!!.popupOtherTextSize.toFloat()


        //Keybutton
        val buttonData = ButtonData(ButtonType.ALPHABET_TYPE, 1)
        buttonData.middle = "ក"
        buttonData.top = "ខ"
        buttonData.right = "គ"
        buttonData.bottom = "ឃ"
        buttonData.left = "ង"

        keyButton = CharacterButtonView(context = applicationContext, buttonData = buttonData, rowHeight = viewConfig!!.buttonHeight)
        keyButton2 = CharacterButtonView(context = applicationContext, buttonData = buttonData, rowHeight = viewConfig!!.buttonHeight)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        keyButton!!.layoutParams.width = width / 8
        keyButton2!!.layoutParams.width = width / 8
        keyButton2!!.changeBackground(true)

        keyButton!!.middleText.textSize = viewConfig!!.buttonMainFontSize.toFloat()
        keyButton!!.topText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()
        keyButton!!.rightText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()
        keyButton!!.bottomText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()
        keyButton!!.leftText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()


        keyButtonFrame?.addView(keyButton)
        keyButtonFrame?.addView(keyButton2)

    }

    fun changeMiddleTextColor(v: View?) {
        showColorPickerDialog(viewConfig!!.buttonMiddleTextColor, SharePref.BUTTON_MIDDLE_TEXT_COLOR)
    }

    fun changeOtherTextColor(v: View?) {
        showColorPickerDialog(viewConfig!!.buttonOtherTextColor, SharePref.BUTTON_OTHER_TEXT_COLOR)

    }

    fun changeKeyBackgroundColor(v: View?) {
        showColorPickerDialog(viewConfig!!.buttonBackgroundColor, SharePref.BUTTON_BACKGROUND_COLOR)

    }

    fun changeKeyBackgroundClickedColor(v: View?) {
        showColorPickerDialog(viewConfig!!.buttonBackgroundClickedColor, SharePref.BUTTON_BACKGROUND_CLICKED_COLOR)

    }

    fun changePopupBackgroundColor(v: View?) {
        showColorPickerDialog(viewConfig!!.popupBackGroundColor, SharePref.POPUP_BACKGROUND_COLOR)

    }


    fun showColorPickerDialog(lastColor: String, pref: String) {
        val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this)
        colorPickerDialog.hideOpacityBar()
        colorPickerDialog.hideHexaDecimalValue()
        colorPickerDialog.setLastColor(lastColor)
        colorPickerDialog.setOnColorPickedListener { color, hexVal ->
            changePreference(hexVal, pref)
            // Make use of the picked color here
        }
        colorPickerDialog.show()
    }

    fun changePreference(newValue: String, preference: String) {

        val sharePref = getSharedPreferences(SharePref.SHARE_PREF_NAME, Context.MODE_PRIVATE)

        val parseColor = Color.parseColor(newValue)



        when (preference) {
            SharePref.BUTTON_MIDDLE_TEXT_COLOR -> {
                viewConfig!!.buttonMiddleTextColor = newValue
                middleTextColorView!!.setBackgroundColor(parseColor)
                keyButton!!.middleText.setTextColor(parseColor)
                keyButton2!!.middleText.setTextColor(parseColor)
                popupMiddleText!!.setTextColor(parseColor)


            }
            SharePref.BUTTON_OTHER_TEXT_COLOR -> {
                viewConfig!!.buttonOtherTextColor = newValue
                otherTextColorView!!.setBackgroundColor(parseColor)
                keyButton!!.topText.setTextColor(parseColor)
                keyButton2!!.topText.setTextColor(parseColor)

                keyButton!!.rightText.setTextColor(parseColor)
                keyButton2!!.rightText.setTextColor(parseColor)

                keyButton!!.bottomText.setTextColor(parseColor)
                keyButton2!!.bottomText.setTextColor(parseColor)

                keyButton!!.leftText.setTextColor(parseColor)
                keyButton2!!.leftText.setTextColor(parseColor)

                popupTopText!!.setTextColor(parseColor)
                popupRightText!!.setTextColor(parseColor)
                popupBottomText!!.setTextColor(parseColor)
                popupLeftText!!.setTextColor(parseColor)

            }
            SharePref.BUTTON_BACKGROUND_COLOR -> {
                viewConfig!!.buttonBackgroundColor = newValue
                keyBackgroundColorView!!.setBackgroundColor(parseColor)
                keyButton!!.setBackgroundColor(parseColor)
            }
            SharePref.BUTTON_BACKGROUND_CLICKED_COLOR -> {
                viewConfig!!.buttonBackgroundClickedColor = newValue
                keyBackgroundClickedColorView!!.setBackgroundColor(parseColor)
                keyButton2!!.setBackgroundColor(parseColor)

            }
            SharePref.POPUP_BACKGROUND_COLOR -> {
                viewConfig!!.popupBackGroundColor = newValue

                popUpColorView!!.setBackgroundColor(parseColor)
                popUpParent!!.setBackgroundColor(parseColor)


            }
        }

        sharePref.edit().putString(preference, newValue).apply()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
