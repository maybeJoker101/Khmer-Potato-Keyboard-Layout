package com.maybejoker101.khmerpotatokeyboard

import android.content.Context
import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import com.maybejoker101.khmerpotatokeyboard.config.SharePref
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWordDatabase
import com.maybejoker101.khmerpotatokeyboard.view.KeyboardView

class KhmerKeyboardService : InputMethodService() {

    private var mKeyboardLayout: KeyboardView? = null
    private var database: DictionaryWordDatabase? = null

    private var sharePref: SharedPreferences? = null
    private var prefListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
    override fun onCreateInputView(): View {

        if (database == null) {
            database = DictionaryWordDatabase.getInstance(application)
        }

        mKeyboardLayout = KeyboardView(applicationContext, this)

        if (sharePref == null) {
            prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                run {
                    setInputView(onCreateInputView())
                }
            }
            sharePref = getSharedPreferences(SharePref.SHARE_PREF_NAME, Context.MODE_PRIVATE)

            sharePref!!.registerOnSharedPreferenceChangeListener(prefListener)


        }

        return mKeyboardLayout!!
    }

    override fun onComputeInsets(outInsets: Insets) {
        super.onComputeInsets(outInsets)

        //this set to enable popup window top of the keyboardl ayout by pass touch through the top
        //placeholder view
        outInsets.visibleTopInsets = mKeyboardLayout!!.keyboardContent.top
        outInsets.contentTopInsets = mKeyboardLayout!!.keyboardContent.top
    }

    override fun onEvaluateFullscreenMode(): Boolean {
        return false
    }
    override fun onDestroy() {
        sharePref!!.unregisterOnSharedPreferenceChangeListener(prefListener)
        super.onDestroy()
    }
}