package com.maybejoker101.khmerpotatokeyboard.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maybejoker101.khmerpotatokeyboard.R
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWord

class DictionaryViewHolder(inflater: LayoutInflater, parent: ViewGroup, val listener: (Int) -> Unit) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.dictionary_item, parent, false)) {

    private var wordContent: TextView? = null
    private var deleteButton: ImageButton? = null

    init {
        wordContent = itemView.findViewById(R.id.dictionaryText)
        deleteButton = itemView.findViewById(R.id.dictionaryDeleteButton)

    }

    fun bind(word: DictionaryWord, position: Int) {
        wordContent?.text = word.word
        deleteButton?.setOnClickListener { listener(position) }
    }
}