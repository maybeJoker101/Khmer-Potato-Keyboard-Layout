package com.maybejoker101.khmerpotatokeyboard.activity.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWord
import io.reactivex.Completable

class DictionaryViewModel : ViewModel() {


    val original: MutableList<DictionaryWord> = mutableListOf()
    val filtered: MutableList<DictionaryWord> = mutableListOf()
    val oldFiltered: MutableList<DictionaryWord> = mutableListOf()

    fun addAll(dictionaryWords: List<DictionaryWord>) {
        original.addAll(dictionaryWords)
        oldFiltered.addAll(original)
        Log.d(DictionaryViewModel::class.java.simpleName, "size ${original.size}")
    }

    fun search(query: String): Completable = Completable.create {
        val wanted = original.filter {
            it.word!!.contains(query)
        }.toList()

        filtered.clear()
        filtered.addAll(wanted)

        Log.d(DictionaryViewModel::class.java.simpleName, "size ${filtered.size}")
        it.onComplete()
    }
}