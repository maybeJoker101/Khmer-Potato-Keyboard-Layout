package com.namae0Two.khmeralternativekeyboard.view.list

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.namae0Two.khmeralternativekeyboard.R

class WordViewHolder(inflater: LayoutInflater, parent: ViewGroup, val listener: (Int) -> Unit) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.word_layout, parent, false)) {

    private var wordContent: TextView? = null
    private var contentParent: LinearLayout? = null
    private var border: View? = null

    init {
        Log.d("ViewHolder", "View Holder Init")
        wordContent = itemView.findViewById(R.id.wordContent)
        contentParent = itemView.findViewById(R.id.wordLayoutParent)
        border = itemView.findViewById(R.id.wordBorder)

        //color
    }

    fun bind(word: String, position: Int) {
        wordContent?.text = word
        contentParent?.setOnClickListener { listener(position) }
    }


}