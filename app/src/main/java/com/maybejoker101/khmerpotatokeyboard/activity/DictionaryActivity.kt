package com.maybejoker101.khmerpotatokeyboard.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.maybejoker101.khmerpotatokeyboard.R
import com.maybejoker101.khmerpotatokeyboard.activity.adapter.DictionaryViewModel
import com.maybejoker101.khmerpotatokeyboard.activity.adapter.DictionaryWordAdapter
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWord
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWordDatabase
import com.maybejoker101.khmerpotatokeyboard.database.ioThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import java.util.concurrent.TimeUnit

class DictionaryActivity : AppCompatActivity() {


    var dictionaryViewModel: DictionaryViewModel? = null

    var dictionaryRecycleView: RecyclerView? = null

    var dictionaryAdapter: DictionaryWordAdapter? = null

    var searchEditText: EditText? = null

    val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        val dictionaryDatabase = DictionaryWordDatabase.getInstance(applicationContext)

        dictionaryViewModel = ViewModelProviders.of(this).get(DictionaryViewModel::class.java)



        ioThread {
            dictionaryViewModel!!.addAll(dictionaryDatabase.dictionaryWordDao().getAllUserWord().sortedBy {
                it.word
            })
            dictionaryAdapter!!.addAllDictionaryWords(dictionaryViewModel!!.original)
            mainHandler.post {
                dictionaryAdapter!!.notifyDataSetChanged()

            }
        }
        dictionaryAdapter = DictionaryWordAdapter(tryDeleteDictionaryWordLamda)


        dictionaryRecycleView = findViewById(R.id.dictionaryRecycleView)
        dictionaryRecycleView!!.layoutManager = LinearLayoutManager(this)
        dictionaryRecycleView!!.adapter = dictionaryAdapter

        searchEditText = findViewById(R.id.searchDictionaryEditText)
        searchEditText!!.isSelected = false


        searchEditText!!.textChanges()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {

                    if (it.isEmpty()) {
                        mainHandler.post { returnToDefaultData() }
                    } else {
                        var text = it.toString()
                        text = text.replace("\u200B", "")
                        dictionaryViewModel!!
                                .search(text)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    mainHandler.post {
                                        dictionaryAdapter!!.dictionaryWordList.clear()
                                        dictionaryAdapter!!.addAllDictionaryWords(dictionaryViewModel!!.filtered)
                                        dictionaryAdapter!!.notifyDataSetChanged()
                                    }
                                }
                    }
                }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun returnToDefaultData() {
        dictionaryAdapter!!.dictionaryWordList.clear()
        dictionaryAdapter!!.addAllDictionaryWords(dictionaryViewModel!!.original)
        dictionaryAdapter!!.notifyDataSetChanged()
    }

    val tryDeleteDictionaryWordLamda = { position: Int ->
        tryDeleteWord(position)
    }

    fun tryDeleteWord(position: Int) {

        val word = dictionaryAdapter!!.dictionaryWordList[position]
        val dictionaryDatabase = DictionaryWordDatabase.getInstance(applicationContext)

        //delete word from database
        ioThread {
            dictionaryDatabase.dictionaryWordDao().delete(word)
            mainHandler.post {
                dictionaryAdapter!!.dictionaryWordList.removeAt(position)
                dictionaryAdapter!!.notifyDataSetChanged()
            }
        }
    }

}
