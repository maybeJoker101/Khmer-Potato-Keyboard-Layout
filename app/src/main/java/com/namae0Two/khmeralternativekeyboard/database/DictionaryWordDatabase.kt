package com.namae0Two.khmeralternativekeyboard.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(DictionaryWord::class), version = 1)
abstract class DictionaryWordDatabase : RoomDatabase() {
    abstract fun dictionaryWordDao(): DictionaryWordDao

    companion object : SingletonHolder<DictionaryWordDatabase, Context>({


        Room.databaseBuilder(it, DictionaryWordDatabase::class.java, "dictionary.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        //Io thread
                        ioThread {
                            Log.d(DictionaryWordDatabase::class.java.simpleName, "Filling data")
                            val dictionaryWords = DictionaryWord.getDictionaryWordsFromAsset(it)
                            DictionaryWordDatabase.getInstance(it).dictionaryWordDao()
                                    .insertAll(dictionaryWords)
                        }

                    }
                })
                .build()
    })
}