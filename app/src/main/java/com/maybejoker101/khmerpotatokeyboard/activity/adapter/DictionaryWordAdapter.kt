package com.maybejoker101.khmerpotatokeyboard.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWord
import com.maybejoker101.khmerpotatokeyboard.view.list.WordViewHolder

class DictionaryWordAdapter(val listener: (Int) -> Unit)
    : RecyclerView.Adapter<DictionaryViewHolder>() {

    val dictionaryWordList: MutableList<DictionaryWord> = mutableListOf()

    fun addAllDictionaryWords(list: List<DictionaryWord>) {
        dictionaryWordList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DictionaryViewHolder(inflater, parent, listener)

    }

    override fun getItemCount(): Int {
        return dictionaryWordList.size
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {

        holder.bind(dictionaryWordList[position], position)

    }

}