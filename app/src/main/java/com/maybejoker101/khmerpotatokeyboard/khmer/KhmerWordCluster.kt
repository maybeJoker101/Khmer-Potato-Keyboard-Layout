package com.maybejoker101.khmerpotatokeyboard.khmer

import android.util.Log
import com.maybejoker101.khmerpotatokeyboard.khmer.KhmerLang.Companion.isAboveDiacritic
import com.maybejoker101.khmerpotatokeyboard.khmer.KhmerLang.Companion.isAfterDiacritic
import com.maybejoker101.khmerpotatokeyboard.khmer.KhmerLang.Companion.isConsonantShifter
import com.maybejoker101.khmerpotatokeyboard.khmer.KhmerLang.Companion.isIndependentAlphabet
import com.maybejoker101.khmerpotatokeyboard.khmer.KhmerLang.Companion.isVowel

class KhmerWordCluster(var base: String = "") {

    var consonantShifter :String = ""
    private val  subscripts :MutableList<String>  = mutableListOf()
    var robatSign:String = ""
    var aboveSign = ""
    var afterSign = ""
    private val  vowels:MutableList<String> = mutableListOf()






    //function to add character to cluster if the character added is not suitable return false
    //example if base is already set and the character adding is also base return false
    // or when base is not set but other character is added as input
    fun addCharacter(char:String,isSubscript:Boolean):Boolean {

        Log.d("KhmerCluser", "Adding $char")
        //Base Checking
        if (base.isEmpty()){
            //check if input is one of the independent alphabet
            return if(isIndependentAlphabet(char)) {
                base = char
                true
            }else {
                false
            }
        }
        //if base if set , check another argument for is subscript
        if(isIndependentAlphabet(char)) {
            if (isSubscript) {
                addSubscript(char)
                return true
            }

            return false
        }
        //Consonant Shifter
        if(isConsonantShifter(char)){
            addConsonantShifter(char)
            return true
        }


        //Vowels
        if (isVowel(char)){
            addVowel(char)
            return true
        }
        //
        //Above diacritic
        if (isAboveDiacritic(char)){
            if (char == KhmerLang.ROBAT){
                addRobat(char)
                return true
            }

            addAboveDiacritic(char)
            return true
        }
        //after diacritic
        if(isAfterDiacritic(char)){
            addAfterDiacritic(char)
            return true
        }
        return false
    }

    private fun addVowel(char:String){

        vowels.add(char)
        vowels.sort()
    }

    private fun addRobat(char:String) {
        robatSign = char
    }
    private fun addAboveDiacritic (char: String) {
        aboveSign = char
    }
    private fun addAfterDiacritic (char: String) {
        afterSign = char
    }
    //Subscript is sorted by alphabetic order
    private fun addSubscript(char:String){
        subscripts.add(char)
        subscripts.sort()
    }

    private fun addConsonantShifter(char: String) {
        consonantShifter = char
    }


    fun getCluster():String{
        var result = base
        //subscript

        for (subscript in subscripts) {
            result += KhmerLang.SUBSCRIPT_SIGN
            result += subscript

        }
        //consonant shifter
        result += consonantShifter
        //Robat
        result += robatSign
        //Vowels
        for(vowel in vowels){
            result += vowel
        }
        //Above Diacritic
        result += aboveSign
        //after diacritic
        result += afterSign


        return result


    }
}