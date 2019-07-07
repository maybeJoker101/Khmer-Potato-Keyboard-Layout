package com.namae0Two.khmeralternativekeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import com.namae0Two.khmeralternativekeyboard.database.DictionaryWordDatabase
import com.namae0Two.khmeralternativekeyboard.view.KeyboardView

class KhmerKeyboardService : InputMethodService() {

    private var mKeyboardLayout: KeyboardView? = null
    private var database: DictionaryWordDatabase? = null

    override fun onCreateInputView(): View {

        if (database == null) {
            database = DictionaryWordDatabase.getInstance(application)
        }

        mKeyboardLayout = KeyboardView(applicationContext, this)



        return mKeyboardLayout!!
    }

    override fun onComputeInsets(outInsets: Insets) {
        super.onComputeInsets(outInsets)

        //this set to enable popup window top of the keyboardl ayout by pass touch through the top
        //placeholder view
        outInsets.visibleTopInsets = mKeyboardLayout!!.keyboardContent.top
        outInsets.contentTopInsets = mKeyboardLayout!!.keyboardContent.top
    }

}