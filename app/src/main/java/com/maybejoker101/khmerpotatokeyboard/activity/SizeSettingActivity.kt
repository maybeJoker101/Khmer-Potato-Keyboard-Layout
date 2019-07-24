package com.maybejoker101.khmerpotatokeyboard.activity

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NavUtils
import com.maybejoker101.khmerpotatokeyboard.R
import com.maybejoker101.khmerpotatokeyboard.config.SharePref
import com.maybejoker101.khmerpotatokeyboard.config.ViewConfig
import com.maybejoker101.khmerpotatokeyboard.data.ButtonData
import com.maybejoker101.khmerpotatokeyboard.data.ButtonType
import com.maybejoker101.khmerpotatokeyboard.util.Util
import com.maybejoker101.khmerpotatokeyboard.view.button.CharacterButtonView
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar

class SizeSettingActivity : AppCompatActivity() {


    var viewConfig: ViewConfig? = null

    var buttonHeightTickBar: TickSeekBar? = null
    var buttonMainFontSizeTickBar: TickSeekBar? = null
    var buttonOtherFontSizeTickBar: TickSeekBar? = null

    var popupHeightAndWidthTickBar: TickSeekBar? = null
    var popupMiddleTextSizeTickBar: TickSeekBar? = null
    var popupOtherTextSizeTickBar: TickSeekBar? = null

    //Lamda for changing config and sharepref

    val changeConfigLamda = { value: Int, prefKey: String ->
        changeSharePrefAndUpdateView(value, prefKey)
    }

    private var keyButtonFrame: LinearLayout? = null
    private var keyButton: CharacterButtonView? = null
    private var keyButton2: CharacterButtonView? = null

    private var popUpParent: ConstraintLayout? = null
    private var popupMiddleText: TextView? = null
    private var popupTopText: TextView? = null
    private var popupRightText: TextView? = null
    private var popupBottomText: TextView? = null
    private var popupLeftText: TextView? = null
    var previewButtonWidth: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_setting)



        viewConfig = ViewConfig.getInstance(applicationContext)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        previewButtonWidth = width / 8

        getReference()
        settingPreview()

        //setValue
        setStoredConfigValue()


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

    fun settingPreview() {
        keyButtonFrame = findViewById(R.id.characterButtonFrame)

        //Popup Card
        popUpParent = findViewById(R.id.popUpParentLayout)
        popupTopText = findViewById(R.id.popup_top)
        popupMiddleText = findViewById(R.id.popup_middle_primary)
        popupRightText = findViewById(R.id.popup_right)
        popupBottomText = findViewById(R.id.popup_bottom)
        popupLeftText = findViewById(R.id.popup_left)

        //set color
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


        keyButton!!.layoutParams.width = previewButtonWidth!!
        keyButton2!!.layoutParams.width = previewButtonWidth!!
        keyButton2!!.changeBackground(true)

        keyButton!!.middleText.textSize = viewConfig!!.buttonMainFontSize.toFloat()
        keyButton!!.topText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()
        keyButton!!.rightText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()
        keyButton!!.bottomText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()
        keyButton!!.leftText.textSize = viewConfig!!.buttonOtherFontSize.toFloat()


        keyButtonFrame?.addView(keyButton)
        keyButtonFrame?.addView(keyButton2)
    }

    fun getReference() {
        //reference
        buttonHeightTickBar = findViewById(R.id.buttonHeightTickBar)
        buttonMainFontSizeTickBar = findViewById(R.id.buttonMainFontSizeTickBar)
        buttonOtherFontSizeTickBar = findViewById(R.id.buttonOtherFontSizeTickBar)

        popupHeightAndWidthTickBar = findViewById(R.id.popupWidthAndHeightTickBar)
        popupMiddleTextSizeTickBar = findViewById(R.id.popupMIddleTextTickBar)
        popupOtherTextSizeTickBar = findViewById(R.id.popupOtherTextSizeTickBar)
        //listener
        buttonHeightTickBar!!.onSeekChangeListener = ConfigSeekChangeListener(changeConfigLamda, SharePref.BUTTON_HEIGHT)
        buttonMainFontSizeTickBar!!.onSeekChangeListener = ConfigSeekChangeListener(changeConfigLamda, SharePref.BUTTON_MAIN_FONT_SIZE)
        buttonOtherFontSizeTickBar!!.onSeekChangeListener = ConfigSeekChangeListener(changeConfigLamda, SharePref.BUTTON_OTHER_fONT_SIZE)

        popupHeightAndWidthTickBar!!.onSeekChangeListener = ConfigSeekChangeListener(changeConfigLamda, SharePref.POPUP_HEIGHT_WIDTH)
        popupMiddleTextSizeTickBar!!.onSeekChangeListener = ConfigSeekChangeListener(changeConfigLamda, SharePref.POPUP_MIDDLE_TEXT_SIZE)
        popupOtherTextSizeTickBar!!.onSeekChangeListener = ConfigSeekChangeListener(changeConfigLamda, SharePref.POPUP_OTHER_TEXT_SIZE)


    }


    fun setStoredConfigValue() {
        buttonHeightTickBar!!.setProgress(viewConfig!!.buttonHeight.toFloat())
        buttonMainFontSizeTickBar!!.setProgress(viewConfig!!.buttonMainFontSize.toFloat())
        buttonOtherFontSizeTickBar!!.setProgress(viewConfig!!.buttonOtherFontSize.toFloat())

        popupHeightAndWidthTickBar!!.setProgress(viewConfig!!.popupHeightAndWidth.toFloat())
        popupMiddleTextSizeTickBar!!.setProgress(viewConfig!!.popupMiddleTextSize.toFloat())
        popupOtherTextSizeTickBar!!.setProgress(viewConfig!!.popupOtherTextSize.toFloat())


        val widthHeight = viewConfig!!.popupHeightAndWidth
        val layoutPaarams = FrameLayout.LayoutParams(Util.getPixelFromDp(widthHeight, applicationContext), Util.getPixelFromDp(widthHeight, applicationContext))

        popUpParent!!.layoutParams = layoutPaarams

        val middlePopupSize = viewConfig!!.popupMiddleTextSize.toFloat()
        val otherPopupSize = viewConfig!!.popupOtherTextSize.toFloat()
        popupMiddleText!!.textSize = middlePopupSize
        popupTopText!!.textSize = otherPopupSize
        popupRightText!!.textSize = otherPopupSize
        popupBottomText!!.textSize = otherPopupSize
        popupLeftText!!.textSize = otherPopupSize


    }


    fun changeSharePrefAndUpdateView(value: Int, prefKey: String) {
        val sharePref = getSharedPreferences(SharePref.SHARE_PREF_NAME, Context.MODE_PRIVATE)
        Log.d(ConfigSeekChangeListener::class.java.simpleName, "changeSharePrefAndUpdateView = ${value}")


        val heightOrWidth = Util.getPixelFromDp(value, applicationContext)
        val textSize = value

        val sizeFloat = textSize.toFloat()
        when (prefKey) {
            SharePref.BUTTON_HEIGHT -> {
                viewConfig!!.buttonHeight = value
                val layoutParams = LinearLayout.LayoutParams(previewButtonWidth!!, heightOrWidth)
                val layoutParams2 = LinearLayout.LayoutParams(previewButtonWidth!!, heightOrWidth)

                keyButton!!.layoutParams = layoutParams
                keyButton2!!.layoutParams = layoutParams2

            }
            SharePref.BUTTON_MAIN_FONT_SIZE -> {
                viewConfig!!.buttonMainFontSize = textSize
                keyButton!!.middleText.textSize = sizeFloat
                keyButton2!!.middleText.textSize = sizeFloat

            }
            SharePref.BUTTON_OTHER_fONT_SIZE -> {
                viewConfig!!.buttonOtherFontSize = textSize
                keyButton!!.topText.textSize = sizeFloat
                keyButton2!!.topText.textSize = sizeFloat
                keyButton!!.rightText.textSize = sizeFloat
                keyButton2!!.rightText.textSize = sizeFloat
                keyButton!!.bottomText.textSize = sizeFloat
                keyButton2!!.bottomText.textSize = sizeFloat
                keyButton!!.leftText.textSize = sizeFloat
                keyButton2!!.leftText.textSize = sizeFloat

            }
            SharePref.POPUP_HEIGHT_WIDTH -> {
                viewConfig!!.popupHeightAndWidth = value
                val layoutParams = FrameLayout.LayoutParams(heightOrWidth, heightOrWidth)
                popUpParent!!.layoutParams = layoutParams

            }
            SharePref.POPUP_MIDDLE_TEXT_SIZE -> {
                viewConfig!!.popupMiddleTextSize = textSize
                viewConfig!!.popupPressedTextSize = textSize + 4
                popupMiddleText!!.textSize = sizeFloat
            }
            SharePref.POPUP_OTHER_TEXT_SIZE -> {
                viewConfig!!.popupOtherTextSize = textSize
                popupTopText!!.textSize = sizeFloat
                popupRightText!!.textSize = sizeFloat
                popupBottomText!!.textSize = sizeFloat
                popupLeftText!!.textSize = sizeFloat
            }
        }
        if (prefKey == SharePref.POPUP_MIDDLE_TEXT_SIZE) {
            sharePref.edit().putInt(prefKey, value).putInt(SharePref.POPUP_PRESSED_TEXT_SIZE, value + 4).apply()

        } else {
            sharePref.edit().putInt(prefKey, value).apply()
        }
    }

    class ConfigSeekChangeListener(var changeConfig: (Int, String) -> Unit, val sharePrefKey: String) : OnSeekChangeListener {


        override fun onSeeking(seekParams: SeekParams?) {
        }

        override fun onStartTrackingTouch(seekBar: TickSeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: TickSeekBar?) {
            Log.d(ConfigSeekChangeListener::class.java.simpleName, "Seekbar = ${seekBar!!.progress}")
            changeConfig(seekBar.progress, sharePrefKey)

        }

    }
}
