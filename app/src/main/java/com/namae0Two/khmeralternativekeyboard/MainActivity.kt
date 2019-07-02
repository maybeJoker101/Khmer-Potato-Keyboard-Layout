package com.namae0Two.khmeralternativekeyboard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.data.Trie

class MainActivity : AppCompatActivity() {

    var numberText: TextView? = null
    var trie: Trie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberText = findViewById<TextView>(R.id.numberOfWordText)

        Handler().run {
            trie = Trie.loadTrieFromAsset(applicationContext)

            numberText!!.text = trie!!.getAllWords().size.toString()

        }
    }
}
