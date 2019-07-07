package com.namae0Two.khmeralternativekeyboard.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary_words")
data class DictionaryWord(
        @PrimaryKey(autoGenerate = true) val wordId: Long? = null,
        @ColumnInfo(name = "word") val word: String?,
        @ColumnInfo(name = "count") val count: Int = 0,
        @ColumnInfo(name = "used_count") val usedCount: Int = 0,
        @ColumnInfo(name = "personal") val personal: Int = 0
) {
    companion object {
        fun getDictionaryWordsFromAsset(context: Context?): List<DictionaryWord> {
            val wordList = arrayListOf<DictionaryWord>()
            context!!.assets.open("wordData.txt").bufferedReader().forEachLine {

                val splitted = it.split("\t")

                val word = splitted[0].trim()

                if (word.isNotEmpty()) {
                    wordList.add(DictionaryWord(word = word, count = splitted[1].toInt()))
                }
            }
            return wordList.toList()
        }
    }
}
