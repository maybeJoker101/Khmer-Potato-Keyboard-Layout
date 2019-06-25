package com.namae0Two.khmeralternativekeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;

import com.namae0Two.khmeralternativekeyboard.view.KeyboardView;

public class KhmerKeyboardService extends InputMethodService {

    private LinearLayout mKeyboardLayout;



    @Override
    public View onCreateInputView() {

        mKeyboardLayout = new KeyboardView(getApplicationContext());


        return mKeyboardLayout;
    }


}
