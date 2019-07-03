package com.namae0Two.khmeralternativekeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;

import com.namae0Two.khmeralternativekeyboard.view.KeyboardView;

public class KhmerKeyboardService extends InputMethodService {

    private KeyboardView mKeyboardLayout;



    @Override
    public View onCreateInputView() {

        mKeyboardLayout = new KeyboardView(getApplicationContext(), this);


        return mKeyboardLayout;
    }

    @Override
    public void onComputeInsets(Insets outInsets) {
        super.onComputeInsets(outInsets);

        //this set to enable popup window top of the keyboardl ayout by pass touch through the top
        //placeholder view
        outInsets.visibleTopInsets = mKeyboardLayout.getKeyboardContent().getTop();
        outInsets.contentTopInsets = mKeyboardLayout.getKeyboardContent().getTop();
    }
}
