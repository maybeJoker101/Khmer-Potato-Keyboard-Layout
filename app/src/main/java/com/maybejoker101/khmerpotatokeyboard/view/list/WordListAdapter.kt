package com.maybejoker101.khmerpotatokeyboard.view.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class WordListAdapter(val listener: (Int) -> Unit) : RecyclerView.Adapter<WordViewHolder>() {

    val wordList: MutableList<String> = mutableListOf()

    fun addAllKhmerWord(list: List<String>) {
        wordList.addAll(list)
        notifyDataSetChanged()
    }

    fun addKhmerWord(word: String) {
        wordList.add(word)
        notifyDataSetChanged()
    }

    fun reset() {
        wordList.clear()
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WordViewHolder(inflater, parent, listener)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordList[position]
        holder.bind(word, position)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }
}