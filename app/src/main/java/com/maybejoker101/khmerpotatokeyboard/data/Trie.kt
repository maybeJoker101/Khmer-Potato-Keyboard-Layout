package com.maybejoker101.khmerpotatokeyboard.data

import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWord
import com.maybejoker101.khmerpotatokeyboard.database.DictionaryWordDatabase
import com.maybejoker101.khmerpotatokeyboard.database.ioThread

class Trie(var root: TrieNode = TrieNode()) {


    companion object {
        fun trieFromSearchNode(searchNode: TrieNode): Trie {

            return Trie(searchNode)
        }

//        fun loadTrieFromAsset(context: Context?): Trie {
//            val trie = Trie()
//
//            context!!.assets.open("wordData.txt").bufferedReader().forEachLine {
//
//                val splitted = it.split("\t")
//
//                val word = splitted[0].trim()
//
//                if (word.isNotEmpty()) {
//                    trie.addWord(word, splitted[1].toInt(), 0)
//                }
//            }
//
//
//            return trie
//        }

        fun loadTrieFromDatabase(database: DictionaryWordDatabase): Trie {
            val trie = Trie()
            ioThread {
                database.dictionaryWordDao().getAll().forEach {
                    trie.addWord(it)
                }
            }
            return trie
        }

    }

    //Add Word to the Trie Structure
    fun addWord(dictionaryWord: DictionaryWord) {
        var currentNode = root
        val word = dictionaryWord.word!!
        for (i in 0 until word.length) {

            val character: String = word[i].toString()

            var nextNode: TrieNode

            if (currentNode.children.containsKey(character)) {
                nextNode = currentNode.children[character]!!
            } else {
                nextNode = TrieNode(character, currentNode)
                currentNode.children[character] = nextNode
            }
            currentNode = nextNode
        }
        currentNode.isWord = true
        currentNode.count += dictionaryWord.count
        currentNode.usedCount = dictionaryWord.usedCount
        currentNode.personal = dictionaryWord.personal
        currentNode.id = dictionaryWord.wordId

    }

    //Search the structure for a partial structure which contain th found word as root
    //return null if not found
    fun searchWord(word: String): Trie? {

        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return null
            }
            currentNode = currentNode.children[character]!!
        }
        return trieFromSearchNode(currentNode)
    }

    fun getCount(word: String): Int {
        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return -1
            }
            currentNode = currentNode.children[character]!!
        }

        return currentNode.count
    }

    fun getUsageCount(word: String): Int {
        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return -1
            }
            currentNode = currentNode.children[character]!!
        }

        return currentNode.usedCount
    }

    fun getNode(word: String): TrieNode? {
        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return null
            }
            currentNode = currentNode.children[character]!!
        }
        return currentNode
    }

    fun getDictionaryWord(word: String): DictionaryWord {
        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return DictionaryWord(word = "")
            }
            currentNode = currentNode.children[character]!!
        }

        return DictionaryWord(wordId = currentNode.id
                , word = currentNode.getWordFromNode(),
                count = currentNode.count,
                usedCount = currentNode.usedCount,
                personal = currentNode.personal)
    }

    //check if the input word is a prefix to other word
    fun isPrefix(word: String): Boolean {
        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return false
            }
            currentNode = currentNode.children[character]!!
        }

        return currentNode.children.isNotEmpty()
    }
    //check if the input word is a word

    fun isWord(word: String): Boolean {
        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return false
            }
            currentNode = currentNode.children[character]!!
        }

        return currentNode.isWord
    }

    fun isPrefixOrWord(word: String): Boolean {
        var currentNode = this.root

        for (i in 0 until word.length) {
            val character = word[i].toString()
            if (!currentNode.children.containsKey(character)) {
                return false
            }
            currentNode = currentNode.children[character]!!
        }

        return currentNode.isWord || currentNode.children.isNotEmpty()
    }


    fun getAllWords(): List<String> {
        val result = mutableListOf<String>()

        deepFirstSearch(this.root, result)

        return result.toList()
    }

    private fun deepFirstSearch(node: TrieNode, result: MutableList<String>) {
        if (node.isWord) {
            result.add(node.getWordFromNode())
        }
        node.children.forEach { (_, v) -> deepFirstSearch(v, result) }
    }

    private fun deepFirstSearch(node: TrieNode, result: MutableList<String>, deep: Int) {
        if (deep == 0) {
            return
        }
        if (node.isWord) {
            result.add(node.getWordFromNode())
        }
        node.children.forEach { (_, v) -> deepFirstSearch(v, result, deep - 1) }
    }

    fun getWords(deep: Int): List<String> {
        val result = mutableListOf<String>()
        deepFirstSearch(this.root, result, deep)
        return result.toList()
    }

    class TrieNode(var character: String = "",
                   var parent: TrieNode? = null,
                   var isWord: Boolean = false) {

        var count: Int = 0
        var usedCount: Int = 0
        var id: Long? = null
        var personal: Int = 0 //0 = default data, 1 is user's input

        var children: MutableMap<String, TrieNode> = mutableMapOf()


        fun getWordFromNode(): String {
            var word = ""
            var current: TrieNode = this

            while (current.parent != null) {
                word += current.character
                current = current.parent!!
            }
            return word.reversed()
        }
    }
}

