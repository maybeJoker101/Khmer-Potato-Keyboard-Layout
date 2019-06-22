package com.namae0Two.khmeralternativekeyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
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
