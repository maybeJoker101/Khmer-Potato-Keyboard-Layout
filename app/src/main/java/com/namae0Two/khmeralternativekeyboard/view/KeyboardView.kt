package com.namae0Two.khmeralternativekeyboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonType
import com.namae0Two.khmeralternativekeyboard.data.KeyboardData
import com.namae0Two.khmeralternativekeyboard.data.Trie
import com.namae0Two.khmeralternativekeyboard.khmer.KhmerLang
import com.namae0Two.khmeralternativekeyboard.khmer.KhmerWord
import com.namae0Two.khmeralternativekeyboard.util.InputTypeUtils
import com.namae0Two.khmeralternativekeyboard.util.Util
import com.namae0Two.khmeralternativekeyboard.view.button.*
import com.namae0Two.khmeralternativekeyboard.view.list.WordListAdapter

class KeyboardView(context: Context, val inputService: InputMethodService) : ConstraintLayout(context) {


    companion object {
        val DEBUG_TAG = "keyboardView"

    }


    var keyboardData: KeyboardData
    var popupView: CardView
    var popupWindows: PopupWindow


    //Child
    var topPlaceholder: View
    var keyboardContent: LinearLayout

    //functioning parameter
    private var keyPressedViewId = -1
    private var characterButtonDirection: Int = -1
    private var keypressed = false
    private val backSpaceHandler = Handler()

    //Word
    var khmerWordTrie: Trie? = null
    val composingWordRecycleView: RecyclerView
    val composingWordAdapter: WordListAdapter

    val possibleWordRecycleView: RecyclerView
    val possibleWordAdapter: WordListAdapter

    var composingText: String = ""
    var composingTextGroup: MutableMap<String, List<String>> = mutableMapOf()

    init {
        id = generateViewId()

        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        //Read Keyboard Data
        keyboardData = KeyboardData.keyboardDataInstanceFromAsset(context)

        //Load Trie Data

        loadTrieData(context)

        //Empty
        composingTextGroup[""] = listOf()

        ///PopupWindows
        val inflater: LayoutInflater = LayoutInflater.from(context)
        popupView = inflater.inflate(R.layout.popup_preview_layout, null, false) as CardView

        val measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        popupView.measure(measureSpec, measureSpec)
        popupWindows = PopupWindow(context)
        popupWindows.width = popupView.measuredWidth
        popupWindows.height = popupView.measuredHeight
        popupWindows.contentView = popupView

        popupWindows.isClippingEnabled = false

        //Children = KeyboardContent and top placeholder

        val topPlaceholderParams = LayoutParams(MATCH_PARENT, 500)
        val keyboardContentParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        keyboardContent = LinearLayout(context)
        keyboardContent.id = View.generateViewId()
        keyboardContentParams.bottomToBottom = id
        keyboardContent.layoutParams = keyboardContentParams
        keyboardContent.orientation = LinearLayout.VERTICAL


        topPlaceholder = View(context)
        //Color Debug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            topPlaceholder.setBackgroundColor(context.getColor(R.color.background_material_dark))
        }
        topPlaceholder.id = View.generateViewId()

        topPlaceholderParams.bottomToTop = keyboardContent.id

        topPlaceholder.layoutParams = topPlaceholderParams


        addView(topPlaceholder)
        addView(keyboardContent)

        //RecycleView
        //possible Word
        possibleWordRecycleView = RecyclerView(context)

        val possibleLamda = { position: Int ->
            possibleWordClick(position)
        }
        possibleWordAdapter = WordListAdapter(possibleLamda)
        buildPossibleWordRecycleView()
        //composingWOrd
        val composingLamda = { position: Int ->
            composingWordClick(position)
        }
        composingWordRecycleView = RecyclerView(context)
        composingWordAdapter = WordListAdapter(composingLamda)

        buildComposingWordRecycleView()

        //End Border
        //End Children
        buildRowContentAndListener()

    }

    fun loadTrieData(context: Context) {

        khmerWordTrie = Trie.loadTrieFromAsset(context)
    }

    fun buildPossibleWordRecycleView() {
        val recycleViewHeight = context.resources.getDimension(R.dimen.keyDefaultHeight).toInt()
        val layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        val recycleViewParams = LinearLayout.LayoutParams(MATCH_PARENT, recycleViewHeight)
        possibleWordRecycleView.setHasFixedSize(true)
        possibleWordRecycleView.setBackgroundResource(R.color.colorKeyBackgroundDefault)
        possibleWordRecycleView.layoutParams = recycleViewParams
        possibleWordRecycleView.layoutManager = layoutManager
        possibleWordRecycleView.adapter = possibleWordAdapter
        keyboardContent.addView(possibleWordRecycleView)

        //Border
        val border = View(context)
        val viewParams = LinearLayout.LayoutParams(MATCH_PARENT, Util.getPixelFromDp(0.75f, context))
        border.setBackgroundResource(R.color.colorKeyContentPrimaryDefault)
        border.layoutParams = viewParams
        keyboardContent.addView(border)
    }

    fun buildComposingWordRecycleView() {
        val recycleViewHeight = context.resources.getDimension(R.dimen.keyDefaultHeight).toInt()
        val layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        val recycleViewParams = LinearLayout.LayoutParams(MATCH_PARENT, recycleViewHeight)
        composingWordRecycleView.setHasFixedSize(true)
        composingWordRecycleView.setBackgroundResource(R.color.colorKeyBackgroundDefault)
        composingWordRecycleView.layoutParams = recycleViewParams
        composingWordRecycleView.layoutManager = layoutManager
        composingWordRecycleView.adapter = composingWordAdapter
        keyboardContent.addView(composingWordRecycleView)

        //Border
        val border = View(context)
        val viewParams = LinearLayout.LayoutParams(MATCH_PARENT, Util.getPixelFromDp(0.75f, context))
        border.setBackgroundResource(R.color.colorKeyContentPrimaryDefault)
        border.layoutParams = viewParams
        keyboardContent.addView(border)
    }


    //adapter listener
    fun possibleWordClick(position: Int) {
        val word = possibleWordAdapter.wordList[position]
        setCurrentComposingText(word)
        commitComposingText()
    }

    fun composingWordClick(position: Int) {
        Toast.makeText(context, composingWordAdapter.wordList[position], Toast.LENGTH_SHORT).show()
        //get clicked Word
        val composingWord = composingWordAdapter.wordList[position]
        //get list of current group
        val composingGroup = composingTextGroup[composingText]
        //if only one left in composingGroup
        if (composingGroup!!.size == 1 && composingGroup.contains(composingWord)) {
            //set clicked word as composing word and commit it to editetxt
            setCurrentComposingText(composingWord)
            commitComposingText()
        } else {
            //reset composing group delete other candidate
            resetComposingWordGroup()
            //set composing group of 1 item only
            Log.d(DEBUG_TAG, "clicked compossing $composingWord")
            composingTextGroup[composingWord] = listOf(composingWord)
            //set composing text to clicked item
            setCurrentComposingText(composingWord)
            //set adapter using current composing text
            setComposingAndPossibleWordAdapter()
        }

    }

    //end adapter listener
    @SuppressLint("ClickableViewAccessibility")
    fun buildRowContentAndListener() {

        ///Touch Listener

        //Character Button listener
        val charBtnDownOp: (CharacterButtonView) -> Unit = {
            onCharacterButtonDown(it)
        }
        val charBtnUpOp: (CharacterButtonView) -> Unit = {
            onCharacterButtonUp(it)
        }
        val chatBtnActionOp: (CharacterButtonView, Int) -> Unit = { view, direction -> onCharacterButtonAction(view, direction) }
        val characterBtnTouchListener: CharacterButtonView.CharacterButtonTouchListener =
                CharacterButtonView.CharacterButtonTouchListener(charBtnDownOp, charBtnUpOp, chatBtnActionOp)

        //End Character Button Listener

        //Backspace button listener

        val backspaceDownOp: () -> Unit = {
            onBackspaceDown()
        }
        val backspaceUpOp: () -> Unit = {
            onBackspaceUp()
        }
        val backspaceLongPressedOp: () -> Unit = {
            onBackspaceLongpressed()
        }
        val backspaceBtnTouchListener: BackSpaceButtonView.BackSpaceTouchListener =
                BackSpaceButtonView.BackSpaceTouchListener(backspaceDownOp, backspaceUpOp, backspaceLongPressedOp)

        //end backspace button listener

        //Space button touch listener
        val spaceDownOp: () -> Unit = {
            onSpaceDown()
        }
        val spaceUpOp: () -> Unit = {
            onSpaceUp()
        }
        val spaceLongPressedOp: () -> Unit = {
            onSpaceLongpressed()
        }

        val spaceBtnTouchListener: SpaceButtonView.SpaceButtonTouchListener =
                SpaceButtonView.SpaceButtonTouchListener(spaceDownOp, spaceUpOp, spaceLongPressedOp)
        //End Space button Touch listener


        //Keyboard Switch Btn Touch Listener
        val switchDownOp: () -> Unit = {
            onSwitchDown()
        }
        val switchUpOp: () -> Unit = {
            onSwitchUp()
        }
        val switchLongPressedOp: () -> Unit = {
            onSwitchLongpressed()
        }

        val switchBtnTouchListener: KeyboardSwitchButtonView.KeyboardSwitchButtonTouchListener =
                KeyboardSwitchButtonView.KeyboardSwitchButtonTouchListener(switchDownOp, switchUpOp, switchLongPressedOp)


        //End Keyboard Switch Btn Touch Listener

        //Enter Button Touch listener
        val enterDownOp: () -> Unit = {
            onEnterDown()
        }
        val enterUpOp: () -> Unit = {
            onEnterUp()
        }
        val enterLongPressedOp: () -> Unit = {
            onEnterLongpressed()
        }

        val enterBtnTouchListener: EnterButtonView.EnterButtonTouchListener =
                EnterButtonView.EnterButtonTouchListener(enterDownOp, enterUpOp, enterLongPressedOp)

        //End Enter button  Touch Listener
        for (row in keyboardData.rows) {

            val rowParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            val rowLayout = LinearLayout(context)
            rowLayout.layoutParams = rowParams
            rowLayout.orientation = LinearLayout.HORIZONTAL
            for (buttonData in row.buttons) {

                var button: ConstraintLayout? = null
                when (buttonData.buttonType) {
                    ButtonType.ALPHABET_TYPE, ButtonType.NUMBER_AND_SIGN -> {
                        button = CharacterButtonView(context, buttonData, row.height)
                        button.setOnTouchListener(characterBtnTouchListener)
                        button.setOnLongClickListener(characterBtnTouchListener)

                    }
                    ButtonType.SPACE -> {
                        button = SpaceButtonView(context, buttonData, row.height)
                        button.setOnTouchListener(spaceBtnTouchListener)
                        button.setOnLongClickListener(spaceBtnTouchListener)
                    }
                    ButtonType.BACKSPACE -> {
                        button = BackSpaceButtonView(context, buttonData, row.height)
                        button.setOnTouchListener(backspaceBtnTouchListener)
                        button.setOnLongClickListener(backspaceBtnTouchListener)

                    }
                    ButtonType.ENTER -> {
                        button = EnterButtonView(context, buttonData, row.height)
                        button.setOnTouchListener(enterBtnTouchListener)
                        button.setOnLongClickListener(enterBtnTouchListener)

                    }
                    ButtonType.KEYBOARD_SWITCH -> {
                        button = KeyboardSwitchButtonView(context, buttonData, row.height)
                        button.setOnTouchListener(switchBtnTouchListener)
                        button.setOnLongClickListener(switchBtnTouchListener)

                    }
                }
                rowLayout.addView(button)
            }
            keyboardContent.addView(rowLayout)
        }
    }


    //Character Button Related Function
    fun onCharacterButtonDown(view: CharacterButtonView) {
        //if another key is pressed return and do nothing
        if (keypressed) {
            return
        }
        //set keypressed
        keypressed = true


        //make sure only one keyPressed press at a time
        keyPressedViewId = view.id
        //change color of keyPressed key
        view.changeBackground(true)
        layoutAndShowPopupWindow(view)
    }

    fun onCharacterButtonUp(view: CharacterButtonView) {
        //if character key is not noted as pressed before stop operation
        if (keyPressedViewId == -1) {
            return
        }
        dismissPopupWindows(view)

        var text = ""
        when (characterButtonDirection) {
            0 -> text = view.buttonData.middle
            1 -> text = view.buttonData.top
            2 -> text = view.buttonData.right
            3 -> text = view.buttonData.bottom
            4 -> text = view.buttonData.left

        }
        if (text.isEmpty()) {
            text = view.buttonData.middle
        }

        val type = view.buttonData.buttonType

        when (type) {
            ButtonType.ALPHABET_TYPE -> {
                //if direction is -1 add all the character to possible composing Text
                if (characterButtonDirection == -1) {
                    addCharactersToCOmposingText(view.buttonData.allChars)
                } else {
                    addCharToComposingText(text)
                }

            }
            ButtonType.NUMBER_AND_SIGN -> {
                commitText(text)

            }
            else -> {
                return
            }
        }



        characterButtonDirection = -1
        keyPressedViewId = -1
        keypressed = false
    }

    fun onCharacterButtonAction(view: CharacterButtonView, direction: Int) {
        changePopupContent(view, direction)
    }
    ///END character Button related function

    //Backspace Button Related Function

    fun onBackspaceDown() {
        //if another key is pressed return and do nothing
        if (keypressed) {
            return
        }
        //set keypressed
        keypressed = true
        deleteText()
    }

    fun onBackspaceUp() {
        keypressed = false
    }

    fun onBackspaceLongpressed() {
        continueDelete()
    }


    //ENd backSpace button Related Function

    //Space Button Related Function

    fun onSpaceDown() {
        //if another key is pressed return and do nothing
        if (keypressed) {
            return
        }
        //set keypressed
        keypressed = true


    }

    fun onSpaceUp() {
        commitText(" ")
        keypressed = false

    }

    fun onSpaceLongpressed() {

    }

    //End Space Button related Function

    //Keyboard Switch Button Related Function

    fun onSwitchDown() {
        //if another key is pressed return and do nothing
        if (keypressed) {
            return
        }
        //set keypressed
        keypressed = true

    }

    fun onSwitchUp() {
        keypressed = false
        val imeManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imeManager.showInputMethodPicker()
    }

    fun onSwitchLongpressed() {

    }

    //End Keyboard Switch button Related Function

    //Enter Button Touch Related Function
    fun onEnterDown() {
        //if another key is pressed return and do nothing
        if (keypressed) {
            return
        }
        //set keypressed
        keypressed = true


    }

    fun onEnterUp() {
        keypressed = false
        val editerInfo = inputService.currentInputEditorInfo
        val imeOptionsAction = InputTypeUtils.getImeOptionsActionIdFromEditorInfo(editerInfo)

        Log.d(DEBUG_TAG, "Current Edit Text IME Option $imeOptionsAction")
        //Thank Stack OverFlowww
        //https://stackoverflow.com/questions/32161133/use-enter-key-on-softkeyboard-to-initiation-an-event
        if (imeOptionsAction == InputTypeUtils.IME_ACTION_CUSTOM_LABEL) {
            inputService.currentInputConnection.performEditorAction(editerInfo.actionId)

        } else if (imeOptionsAction != EditorInfo.IME_ACTION_NONE) {
            inputService.currentInputConnection.performEditorAction(imeOptionsAction)
        } else {
            val code = 10
            commitText(code.toChar().toString())
        }
    }

    fun onEnterLongpressed() {

    }

    //End Enter button  Touch Related Function
    //Map related Function
    fun resetComposingWordGroup() {
        composingTextGroup.clear()
        composingTextGroup[""] = listOf()
    }

    //


    //Input Connection Related Method
    fun commitText(text: String) {
        val inputConnection = inputService.currentInputConnection

        // currently there is composing text
        if (composingText.isNotEmpty()) {
            commitComposingText()
        }

        inputConnection.commitText(text, 1)
    }

    //Add 1 Character to Composing Text
    fun addCharToComposingText(text: String) {
        //get Current composing Text
        val oldComposingText = composingText
        //update to new composing Text
        val newComposingText = composingText + text
        //get old possible list
        val oldPossibleList = composingTextGroup[oldComposingText]
        val possibleList = mutableListOf<String>()

        possibleList.add(newComposingText)
        if (oldPossibleList != null) {
            for (i in 0 until oldPossibleList.size) {
                //add text to old possible
                val newPossible = oldPossibleList[i] + text
                //check if it is prefix or word
                if (khmerWordTrie!!.isPrefixOrWord(newPossible)) {
                    //add it to new possible
                    if (!possibleList.contains(newPossible)) {
                        possibleList.add(newPossible)
                    }
                }
                //if text is consonant
                if (KhmerLang.consonant.contains(text)) {
                    //add subscript and check for prefix
                    val newPossible2 = oldPossibleList[i] + KhmerLang.SUBSCRIPT_SIGN + text
                    if (khmerWordTrie!!.isPrefixOrWord(newPossible2)) {
                        possibleList.add(newPossible2)

                    }
                }

            }
        }


        setCurrentComposingText(newComposingText)

        composingTextGroup[composingText] = possibleList
        setComposingAndPossibleWordAdapter()

    }

    //Add Many Characters to Composing Text
    fun addCharactersToCOmposingText(chracters: List<String>) {


        //get Current composing Text
        val oldComposingText = composingText
        //update to new composing Text
        val newComposingText = composingText + chracters[0]
        //get old possible list
        val oldPossibleList = composingTextGroup[oldComposingText]
        val possibleList = mutableListOf<String>()

        if (oldComposingText.isEmpty()) {
            possibleList.addAll(chracters)

        } else {
            possibleList.add(newComposingText)

            if (oldPossibleList != null) {


                for (character in chracters) {
                    for (i in 0 until oldPossibleList.size) {
                        //add text to old possible
                        val newPossible = oldPossibleList[i] + character
                        //check if it is prefix or word
                        if (khmerWordTrie!!.isPrefixOrWord(newPossible)) {
                            //add it to new possible
                            if (!possibleList.contains(newPossible)) {
                                possibleList.add(newPossible)
                            }
                        }
                        //if text is consonant
                        if (KhmerLang.consonant.contains(character)) {
                            //add subscript and check for prefix
                            val newPossible2 = oldPossibleList[i] + KhmerLang.SUBSCRIPT_SIGN + character
                            if (khmerWordTrie!!.isPrefixOrWord(newPossible2)) {
                                possibleList.add(newPossible2)

                            }
                        }

                    }
                }
            }


        }
        setCurrentComposingText(newComposingText)

        composingTextGroup[composingText] = possibleList
        setComposingAndPossibleWordAdapter()
    }

    fun setCurrentComposingText(text: String) {
        composingText = text
        val current = inputService.currentInputConnection!!
        current.setComposingText(composingText, 1)
//        queryWordFromTrie()

    }

    fun commitComposingText() {
        val inputConnection = inputService.currentInputConnection
        inputConnection.finishComposingText()
        composingText = ""
        setComposingAndPossibleWordAdapter()
    }

    fun deleteText() {
//        inputService.currentInputConnection.sendKeyEvent(KeyEvent(ACTION_DOWN, KEYCODE_DEL))
//        inputService.currentInputConnection.sendKeyEvent(KeyEvent(ACTION_UP, KEYCODE_DEL))


        val currentInput = inputService.currentInputConnection

        val selectedText: CharSequence? = currentInput.getSelectedText(0)

        //if there is currently composing text delete the composing text
        //if there is no selected text
        if (composingText.isNotEmpty() && selectedText.isNullOrEmpty()) {
            //delete old
            val currentComposing = composingText
            composingTextGroup.remove(currentComposing)
            if (currentComposing.length == 1) {
                setCurrentComposingText("")
            } else {
                setCurrentComposingText(composingText.substring(0, composingText.length - 1))
            }
            //check if current composingtext has its array
            if (!composingTextGroup.contains(composingText)) {
                Log.d(DEBUG_TAG, " current composing $composingText  ${composingTextGroup.contains(composingText)}")
                composingTextGroup[composingText] = listOf(composingText)
            }

            setComposingAndPossibleWordAdapter()
            return
        }



        if (!selectedText.isNullOrEmpty()) {
            if (composingText.isNotEmpty()) {
                commitComposingText()
                resetComposingWordGroup()
            }
            currentInput.commitText("", 1)
        } else {
            currentInput.deleteSurroundingText(1, 0)
        }

    }

    fun continueDelete() {
        if (keypressed) {
            deleteText()
            backSpaceHandler.postDelayed({ continueDelete() }, 75)
        }

    }

    fun setComposingAndPossibleWordAdapter() {
        composingWordAdapter.reset()
        possibleWordAdapter.reset()

        Log.d(DEBUG_TAG, "current composing text $composingText")
        val composing = composingTextGroup[composingText]!!
        val currentWord = currentPossibleWord(3)

        val filter = composing.filterNot { word -> currentWord.contains(word) && word != composingText && !khmerWordTrie?.isPrefix(word)!! }
        composingWordAdapter.addAllKhmerWord(filter)
        possibleWordAdapter.addAllKhmerWord(currentWord)
    }
    //End Input connection related

    //Trie Related Function

    fun currentPossibleWord(depth: Int): List<String> {
        val result = mutableListOf<String>()
        //length should be 2 or more
        if (composingText.length >= 2) {
            for (composing in composingTextGroup[composingText]!!) {
                Log.d(DEBUG_TAG, "possible word of $composing")
                if (khmerWordTrie?.isPrefixOrWord(composing)!!) {
                    result.addAll(khmerWordTrie?.searchWord(composing)!!.getWords(depth))
                }
            }
        }

        result.sortWith(compareBy({ it.length }, { khmerWordTrie?.getCount(it)!! * -1 }))
        return result.toList()

    }


    //
    ///ENd Trie Ralated Function

    //Popup related Function
    fun layoutAndShowPopupWindow(view: CharacterButtonView) {
        //content of popupWindow
        popupView.findViewById<TextView>(R.id.popup_middle_big).text = view.buttonData.middle
        popupView.findViewById<TextView>(R.id.popup_middle_primary).text = view.buttonData.middle
        popupView.findViewById<TextView>(R.id.popup_top).text = view.buttonData.top
        popupView.findViewById<TextView>(R.id.popup_right).text = view.buttonData.right
        popupView.findViewById<TextView>(R.id.popup_bottom).text = view.buttonData.bottom
        popupView.findViewById<TextView>(R.id.popup_left).text = view.buttonData.left
        val viewLocation = IntArray(2)
        view.getLocationInWindow(viewLocation)

        val viewX = viewLocation[0]
        val viewY = viewLocation[1]
        val viewWidth = view.width
        val parentWidth = this.width

        Log.d(DEBUG_TAG, "x = " + viewX + " y = " + viewY + " width = " + viewWidth + " Parent = " + parentWidth)

        var x = viewX + (view.width - popupWindows.width) / 2
        val y = viewY - Util.getPixelFromDp(100, context)

        if (x < 0) {
            x = 0
        } else if (x > parentWidth - popupWindows.width) {
            x = parentWidth - popupWindows.width
        }
        //show Popup Windows
        popupWindows.isTouchable = false
        popupWindows.showAtLocation(view, Gravity.NO_GRAVITY, x, y)

    }

    fun dismissPopupWindows(view: CharacterButtonView) {
        //change color back
        view.changeBackground(false)
        //dismiss popupwindwos
        resetPopupContent()
        popupWindows.dismiss()


    }

    //Change Popup Content Depend on Direction
    //direction is from 0.. 4,
    //0 middle
    //1 top
    //2 right
    //3 bottom
    //4 left
    fun changePopupContent(view: CharacterButtonView, direction: Int) {

        //Character Button Direction
        characterButtonDirection = direction

        popupView.findViewById<TextView>(R.id.popup_middle_primary).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_top).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_right).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_bottom).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_left).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_middle_big).visibility = View.VISIBLE
        var text = ""
        when (direction) {
            0 -> text = view.buttonData.middle
            1 -> text = view.buttonData.top
            2 -> text = view.buttonData.right
            3 -> text = view.buttonData.bottom
            4 -> text = view.buttonData.left

        }
        if (text.isEmpty()) {
            text = view.buttonData.middle
            // if the
            characterButtonDirection = 0
        }
        Log.d(DEBUG_TAG, "change popupcontent $text")
        popupView.findViewById<TextView>(R.id.popup_middle_big).text = text
    }

    private fun resetPopupContent() {
        popupView.findViewById<TextView>(R.id.popup_middle_big).visibility = View.INVISIBLE
        popupView.findViewById<TextView>(R.id.popup_middle_primary).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_top).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_right).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_bottom).visibility = View.VISIBLE
        popupView.findViewById<TextView>(R.id.popup_left).visibility = View.VISIBLE
    }

    //End popup related Function

}