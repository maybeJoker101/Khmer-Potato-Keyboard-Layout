package com.namae0Two.khmeralternativekeyboard;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.namae0Two.khmeralternativekeyboard.view.KeyboardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ConstraintLayout mainlayout = (ConstraintLayout)findViewById(R.id.mainlayout);

        //
        KeyboardView keyboardView = new KeyboardView(getApplicationContext());


        ConstraintSet s = new ConstraintSet();
        s.clone(mainlayout);

        s.connect(keyboardView.getId(), ConstraintSet.TOP, mainlayout.getId(), ConstraintSet.TOP, 0);
        s.connect(keyboardView.getId(), ConstraintSet.END, mainlayout.getId(), ConstraintSet.END, 0);
        s.connect(keyboardView.getId(), ConstraintSet.BOTTOM, mainlayout.getId(), ConstraintSet.BOTTOM, 0);
        s.connect(keyboardView.getId(), ConstraintSet.START, mainlayout.getId(), ConstraintSet.START, 0);
        mainlayout.addView(keyboardView);

        s.applyTo(mainlayout);


    }
}
