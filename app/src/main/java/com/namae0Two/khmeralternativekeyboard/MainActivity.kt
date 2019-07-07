package com.namae0Two.khmeralternativekeyboard

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.namae0Two.khmeralternativekeyboard.database.DictionaryWordDatabase
import com.namae0Two.khmeralternativekeyboard.database.ioThread

class MainActivity : AppCompatActivity() {
    private var database: DictionaryWordDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (database == null) {
            database = DictionaryWordDatabase.getInstance(application)
        }
        ioThread {
            //invoke onCreate
            database!!.dictionaryWordDao().getAll()
        }
    }

    override fun onStop() {
        Log.d("Filling", "Deleting Database")
        applicationContext.deleteDatabase("dictionary.db")

        super.onStop()
    }
}
