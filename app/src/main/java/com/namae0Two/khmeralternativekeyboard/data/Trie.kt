package com.namae0Two.khmeralternativekeyboard.data

import android.content.Context

class Trie(var root: TrieNode = TrieNode()) {


    companion object {
        fun trieFromSearchNode(searchNode: TrieNode): Trie {

            return Trie(searchNode)
        }

        fun loadTrieFromAsset(context: Context?): Trie {
            val trie = Trie()

            context!!.assets.open("wordData.txt").bufferedReader().forEachLine {

                val splitted = it.split("\t")

                val word = splitted[0].trim()

                if (word.isNotEmpty()) {
                    trie.addWord(word, splitted[1].toInt())
                }
            }


            return trie
        }
    }

    //Add Word to the Trie Structure
    fun addWord(word: String, count: Int = 0) {
        var currentNode = root

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
        currentNode.count += count

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

    class TrieNode(var character: String = "", var parent: TrieNode? = null, var isWord: Boolean = false) {

        var count: Int = -1
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

