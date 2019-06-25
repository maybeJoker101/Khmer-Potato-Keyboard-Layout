package com.namae0Two.khmeralternativekeyboard.data


enum class ButtonType (val type:Int) {
    ALPHABET_TYPE(0),
    NUMBER_AND_SIGN (1),
    BACKSPACE(2),
    ENTER(3),
    SPACE(4),
    KEYBOARD_SWITCH(5);

    companion object{
        private val map :Map<Int,ButtonType> = values().associateBy(ButtonType::type)
        fun fromInt(input:Int) :ButtonType? = map[input]
    }

}