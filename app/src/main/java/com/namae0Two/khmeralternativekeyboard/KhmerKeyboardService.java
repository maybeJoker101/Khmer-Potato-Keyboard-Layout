package com.namae0Two.khmeralternativekeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.widget.LinearLayout;

public class KhmerKeyboardService extends InputMethodService {

    private LinearLayout keyboardLayout;


    @Override
    public View onCreateInputView() {

        keyboardLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.keyboard_layout,null);

        return keyboardLayout;
    }


}
