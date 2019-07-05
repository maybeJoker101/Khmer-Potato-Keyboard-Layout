package com.namae0Two.khmeralternativekeyboard.view.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.namae0Two.khmeralternativekeyboard.khmer.KhmerWord

class WordListAdapter : RecyclerView.Adapter<WordViewHolder>() {

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
        return WordViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordList[position]
        holder.bind(word)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }
}