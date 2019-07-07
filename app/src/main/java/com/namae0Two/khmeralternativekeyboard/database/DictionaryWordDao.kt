package com.namae0Two.khmeralternativekeyboard.database

import androidx.room.*

@Dao
interface DictionaryWordDao {

    @Query("SELECT * FROM dictionary_words")
    fun getAll(): List<DictionaryWord>

    @Insert
    fun insertAll(vararg words: DictionaryWord)

    @Insert
    fun insertAll(words: List<DictionaryWord>)

    @Delete
    fun delete(word: DictionaryWord)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: DictionaryWord): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(word: DictionaryWord)

    //https://stackoverflow.com/questions/45677230/android-room-persistence-library-upsert
    @Transaction
    fun upsert(word: DictionaryWord) {
        val id = insert(word)

        if (id == (-1).toLong()) {
            update(word)
        }
    }
}