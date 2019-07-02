package com.namae0Two.khmeralternativekeyboard.data

import android.content.Context
import org.json.JSONObject

class KeyboardData {
    var rows: MutableList<RowData> = mutableListOf()

    companion object {
        fun keyboardDataInstanceFromAsset(context: Context?) :KeyboardData{

            val jsonString = context!!.assets.open("keyboard.json").bufferedReader().use {
                it.readText()
            }

            var keyboardData = KeyboardData()
//Keyboard
            val keyboardJsonObjects = JSONObject(jsonString)
//            Rows
            val keyboardRows = keyboardJsonObjects.getJSONArray("rows")

            for ( rowIndex in 0 until keyboardRows.length()) {
                //each row
                val rowJsonObject = keyboardRows.getJSONObject(rowIndex)

                var rowData = RowData(rowJsonObject.getInt("height"))

                val rowButtons = rowJsonObject.getJSONArray("buttons")

                for(buttonIndex in 0 until rowButtons.length() ){

                    //each button
                    val buttonJsonObject = rowButtons.getJSONObject(buttonIndex)

                    val type:Int = buttonJsonObject.getInt("buttonType")
                    val weight :Int  = buttonJsonObject.getInt("weight")

                    val buttonData = ButtonData(ButtonType.fromInt(type)!!, weight)

                    buttonData.middle = buttonJsonObject.getString("middle")
                    buttonData.top = buttonJsonObject.getString("top")
                    buttonData.right = buttonJsonObject.getString("right")
                    buttonData.bottom = buttonJsonObject.getString("bottom")
                    buttonData.left = buttonJsonObject.getString("left")
                    rowData.buttons.add(buttonData)
                }
                keyboardData.rows.add(rowData)
            }
            return keyboardData
        }
    }
}