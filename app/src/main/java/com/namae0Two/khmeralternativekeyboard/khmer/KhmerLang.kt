package com.namae0Two.khmeralternativekeyboard.khmer

class KhmerLang {


    companion object {
        var consonant: List<String> = listOf(
                "ក",
                "ខ",
                "គ",
                "ឃ",
                "ង",
                "ច",
                "ឆ",
                "ជ",
                "ឈ",
                "ញ",
                "ដ",
                "ឋ",
                "ឌ",
                "ឍ",
                "ណ",
                "ត",
                "ថ",
                "ទ",
                "ធ",
                "ន",
                "ប",
                "ផ",
                "ព",
                "ភ",
                "ម",
                "យ",
                "រ",
                "ល",
                "វ",
                "ស",
                "ហ",
                "ឡ",
                "អ"
        )
        // Base Vowel + diacritic if the vowel has

        var full_vowels: List<String> = listOf(
                "ា",
                "ិ",
                "ី",
                "ឹ",
                "ឺ",
                "ុ",
                "ូ",
                "ួ",
                "ើ",
                "ឿ",
                "ៀ",
                "េ",
                "ែ",
                "ៃ",
                "ោ",
                "ៅ",
                "ុំ",
                "ំ",
                "ាំ",
                "ះ",
                "ុះ",
                "េះ",
                "ោះ"
        )
        //vowel which take only one character
        // this include diacritic aom and as
        var base_vowels: List<String> = listOf(
                "ា",
                "ិ",
                "ី",
                "ឹ",
                "ឺ",
                "ុ",
                "ូ",
                "ួ",
                "ើ",
                "ឿ",
                "ៀ",
                "េ",
                "ែ",
                "ៃ",
                "ោ",
                "ៅ",
                "ំ"  //technically is diacritic due to it also act as vowel it is here

        )

        //Khmer independent Vowels
        var independent_vowels: List<String> = listOf(
                "ឥ",
                "ឦ",
                "ឧ",
                "ឩ",
                "ឪ",
                "ឫ",
                "ឬ",
                "ឭ",
                "ឮ",
                "ឯ",
                "ឰ",
                "ឱ",
                "ឳ",
                "ឲ"
        )
        //Consonant shifter of khmer language
        var consonant_shifters: List<String> = listOf(
                "៉", "៊"
        )

        //diacritic above the base character
        var dependent_above_diacritic: List<String> = listOf(
                "់", // Bantoc
                "៌", // Robat
                "៍", // Toandakhiat
                "៎", // Kakabat
                "៏", // Ahsda
                "័", // Samyok Sannya
                "៑" // Viram
        )

        //diacritic after the character
        var dependent_after_diacritic: List<String> = listOf(
                "ះ", //technically is diacritic, same as above
                "ៈ" // Yuukaleapintu

        )

        //other sign used in the keyboard
        var other_sign: List<String> = listOf(
                "។",
                "៕",
                "៖",
                "ៗ",
                "៘",
                "៛",
                ",",
                "?",
                "(",
                ")",
                "\""

        )

        //Khmer DIgits
        var khmer_digits: List<String> = listOf(
                "០",
                "១",
                "២",
                "៣",
                "៤",
                "៥",
                "៦",
                "៧",
                "៨",
                "៩"
        )
        //Latin Digits
        var latin_digits: List<String> = listOf(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "0"
        )


        val generateMap = fun(): Map<String, CharacterType> {
            val myMutableMap: MutableMap<String, CharacterType> = mutableMapOf()

            myMutableMap.putAll(consonant.associateWith { CharacterType.INDEPENDENT_ALPHABET })

            myMutableMap.putAll(base_vowels.associateWith { CharacterType.DEPENDENT_VOWEL })
            myMutableMap.putAll(independent_vowels.associateWith { CharacterType.INDEPENDENT_ALPHABET })
            myMutableMap.putAll(consonant_shifters.associateWith { CharacterType.CONSONANT_SHIFTER })
            myMutableMap.putAll(dependent_above_diacritic.associateWith { CharacterType.ABOVE_DIACRITIC })
            myMutableMap.putAll(dependent_after_diacritic.associateWith { CharacterType.AFTER_DIACRITIC })
            myMutableMap.putAll(other_sign.associateWith { CharacterType.DIGITS_AND_SIGN })
            myMutableMap.putAll(khmer_digits.associateWith { CharacterType.DIGITS_AND_SIGN })
            myMutableMap.putAll(latin_digits.associateWith { CharacterType.DIGITS_AND_SIGN })



            return myMutableMap.toMap()
        }
        var character_to_type_map: Map<String, CharacterType> = generateMap()

        //Robat
        var ROBAT = "៌" //

        var SUBSCRIPT_SIGN = "្"

        val isInSpellingAlphabet = fun(char:String) :Boolean{
            val charType = character_to_type_map[char]
            return charType == CharacterType.AFTER_DIACRITIC ||
                    charType == CharacterType.ABOVE_DIACRITIC ||
                    charType == CharacterType.CONSONANT_SHIFTER ||
                    charType == CharacterType.INDEPENDENT_ALPHABET ||
                    charType == CharacterType.DEPENDENT_VOWEL||
                    char == SUBSCRIPT_SIGN
        }
    }


}