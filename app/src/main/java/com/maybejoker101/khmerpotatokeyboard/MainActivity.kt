package com.maybejoker101.khmerpotatokeyboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maybejoker101.khmerpotatokeyboard.activity.ColorSettingActivity
import com.maybejoker101.khmerpotatokeyboard.config.ViewConfig
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWordDatabase
import com.maybejoker101.khmerpotatokeyboard.database.ioThread

class MainActivity : AppCompatActivity() {
    private var database: DictionaryWordDatabase? = null

    private var viewConfig: ViewConfig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewConfig = ViewConfig.getInstance(applicationContext)

        if (database == null) {
            database = DictionaryWordDatabase.getInstance(application)
        }
        ioThread {
            //invoke onCreate
            database!!.dictionaryWordDao().getAll()
        }
    }

    fun onCreditClicked(view: View) {

    }

    fun onColorTextClicked(view: View) {
        val intent: Intent = Intent(applicationContext, ColorSettingActivity::class.java)

        startActivity(intent)

    }

    fun onSizeTextClicked(view: View) {

    }

    fun onDictionaryTextClicked(view: View) {

    }
//    override fun onStop() {
////        Log.d("Filling", "Deleting Database")
////        applicationContext.deleteDatabase("dictionary.db")
//
//        super.onStop()
//    }
}
