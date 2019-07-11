package com.maybejoker101.khmerpotatokeyboard.view.list

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maybejoker101.khmerpotatokeyboard.R
import com.maybejoker101.khmerpotatokeyboard.config.ViewConfig

class WordViewHolder(inflater: LayoutInflater, parent: ViewGroup, val listener: (Int) -> Unit) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.word_layout, parent, false)) {

    private var wordContent: TextView? = null
    private var contentParent: LinearLayout? = null
    private var border: View? = null

    init {
        Log.d("ViewHolder", "View Holder Init")
        wordContent = itemView.findViewById(R.id.wordContent)
        contentParent = itemView.findViewById(R.id.wordLayoutParent)
        border = itemView.findViewById(R.id.wordBorder)

        //color
        val viewConfig = ViewConfig.getInstance(parent.context)
        contentParent!!.setBackgroundColor(Color.parseColor(viewConfig.buttonBackgroundColor))
        border!!.setBackgroundColor(Color.parseColor(viewConfig.buttonMiddleTextColor))
        wordContent!!.setTextColor(Color.parseColor(viewConfig.buttonMiddleTextColor))
    }

    fun bind(word: String, position: Int) {
        wordContent?.text = word
        contentParent?.setOnClickListener { listener(position) }
    }


}