package com.namae0Two.khmeralternativekeyboard;

import android.inputmethodservice.InputMethodService;
import android.support.constraint.ConstraintLayout;
import android.widget.LinearLayout;

public class Keyboard {

    public static final String DEBUG_TAG = Keyboard.class.getName();
    private LinearLayout parent;



    //Row 1
    private ConstraintLayout btn1;
    public Keyboard (LinearLayout parent, final InputMethodService service){

        btn1 = parent.findViewById(R.id.keyBtnRow0_0);
    }


}
