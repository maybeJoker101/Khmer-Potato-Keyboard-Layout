package com.namae0Two.khmeralternativekeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;

public class KhmerKeyboardService extends InputMethodService {

    private LinearLayout mKeyboardLayout;


    private Keyboard mKeyboard;

    @Override
    public View onCreateInputView() {

        mKeyboardLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.keyboard_layout,null);


        mKeyboard = new Keyboard(mKeyboardLayout,this);

        return mKeyboardLayout;
    }


}
