package com.namae0Two.khmeralternativekeyboard.khmer

class KhmerWord(var inputWord: String = "") {

    var wordClusters: MutableList<KhmerWordCluster> = mutableListOf()

    init {
        inputWord = inputWord.trim()
        generateClusters()
    }

    //generate clusters for the givens inputWord
    private fun generateClusters() {
        var isSubscript = false

        //init first cluster
        var clusterIndex = -1

        for (i in 0 until inputWord.length) {
            val character = inputWord[i].toString()

            //if current character is not in spelling alphabet throw error
            if (!KhmerLang.isInSpellingAlphabet(character)) {
                throw IllegalArgumentException("Input character must be in spelling alphabet")

            }

            //First Cluster
            if (clusterIndex == -1) {
                clusterIndex += 1

                addCluster(character,isSubscript)
                continue
            }
            ////////

            //check for subscript
            if (character == KhmerLang.SUBSCRIPT_SIGN) {
                isSubscript = true
                continue
            }

            ///addCharacter
            //if character cannot is added create new cluster
            if (!wordClusters[clusterIndex].addCharacter(character, isSubscript)) {
                //create new cluster
                clusterIndex += 1
                addCluster(character,isSubscript)
                continue

            }else {
                //Reset is subscript
                isSubscript = false
            }


        }


    }

    //add a new cluster to the array
    //used during generation
    private fun addCluster(base:String,isSubscript:Boolean){
        val cluster = KhmerWordCluster()

        if (cluster.addCharacter(base, isSubscript)) {
            wordClusters.add(cluster)
        } else {
            ///Error base cannot is other then independent alphabet
            throw IllegalArgumentException(" base cannot is other then independent alphabet")
        }
    }

    //get the Khmer Word from this object
    //result might be different from the input
    // as it is subjected to subscript sorting and character ordering

    fun getKhmerWord():String{
        var result = ""
        for(cluster in wordClusters){
            result += cluster.getCluster()
        }

        return result
    }

    fun addKhmerCharacterToWord(character:String ):KhmerWord{
        val newWord = getKhmerWord() + character

        return KhmerWord(newWord)
    }

}