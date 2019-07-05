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
import com.namae0Two.khmeralternativekeyboard.R
import com.namae0Two.khmeralternativekeyboard.data.ButtonType
import com.namae0Two.khmeralternativekeyboard.data.KeyboardData
import com.namae0Two.khmeralternativekeyboard.data.Trie
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

    //Word querying
    var khmerWordTrie: Trie? = null
    val wordRecycleView: RecyclerView
    val wordQueryAdapter: WordListAdapter

    val khmerWords: MutableList<KhmerWord> = mutableListOf()
    init {
        id = generateViewId()

        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        //Read Keyboard Data
        keyboardData = KeyboardData.keyboardDataInstanceFromAsset(context)

        //Load Trie Data

        loadTrieData(context)

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
        val recycleViewHeight = context.resources.getDimension(R.dimen.keyDefaultHeight).toInt()
        val layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        val words = khmerWordTrie!!.getAllWords()


        wordRecycleView = RecyclerView(context)
        wordQueryAdapter = WordListAdapter()


        wordRecycleView.setHasFixedSize(true)
        wordRecycleView.setBackgroundResource(R.color.colorKeyBackgroundDefault)
        wordRecycleView.layoutManager = layoutManager
        wordRecycleView.adapter = wordQueryAdapter
        wordQueryAdapter.addAllKhmerWord(words)
        Log.d(DEBUG_TAG, "Word Count ${words.size}  $recycleViewHeight")
        keyboardContent.addView(wordRecycleView)

        //Border
        val border = View(context)
        val viewParams = LinearLayout.LayoutParams(MATCH_PARENT, Util.getPixelFromDp(0.75f, context))
        border.setBackgroundResource(R.color.colorKeyContentPrimaryDefault)
        border.layoutParams = viewParams
        keyboardContent.addView(border)
        //
        //End Children
        buildRowContentAndListener()

    }

    fun loadTrieData(context: Context) {

        khmerWordTrie = Trie.loadTrieFromAsset(context)
    }


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
        wordQueryAdapter.addKhmerWord("A")
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
        commitText(text)

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

        val editerInfo = inputService.currentInputEditorInfo
        val imeOptionsAction = InputTypeUtils.getImeOptionsActionIdFromEditorInfo(editerInfo)

        Log.d(DEBUG_TAG, "Current Edit Text IME Option $imeOptionsAction")
        //TODO investigate Enter Button
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

    fun onEnterUp() {
        keypressed = false

    }

    fun onEnterLongpressed() {

    }

    //End Enter button  Touch Related Function

    //Input Connection Related Method
    fun commitText(text: String) {

        val inputConnection = inputService.currentInputConnection
        //reset direction
        inputConnection.commitText(text, 1)
    }

    fun deleteText() {
//        inputService.currentInputConnection.sendKeyEvent(KeyEvent(ACTION_DOWN, KEYCODE_DEL))
//        inputService.currentInputConnection.sendKeyEvent(KeyEvent(ACTION_UP, KEYCODE_DEL))

        val currentInput = inputService.currentInputConnection

        val selectedText: CharSequence? = currentInput.getSelectedText(0)

        if (!selectedText.isNullOrEmpty()) {
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
    //End Input connection related


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