package com.namae0Two.khmeralternativekeyboard.khmer

//Type of the character as enum class
enum class CharacterType {
    INDEPENDENT_ALPHABET,// include both consonants and independent vowels this two can act
    //as base of a khmerword cluster
    DEPENDENT_VOWEL,
    CONSONANT_SHIFTER,
    ABOVE_DIACRITIC,
    AFTER_DIACRITIC,
    DIGITS_AND_SIGN,
    SUBSCRIPT_SIGN
}