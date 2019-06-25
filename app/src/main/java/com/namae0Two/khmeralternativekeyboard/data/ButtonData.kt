package com.namae0Two.khmeralternativekeyboard.data


class ButtonData(var buttonType: ButtonType,var weight:Int) {
    var middle: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                allChars.add(value)
            }
        }

    var top: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                allChars.add(value)
            }
        }

    var right: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                allChars.add(value)
            }
        }

    var bottom: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                allChars.add(value)
            }
        }

    var left: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                allChars.add(value)
            }
        }


    var allChars: MutableList<String> = mutableListOf()


}