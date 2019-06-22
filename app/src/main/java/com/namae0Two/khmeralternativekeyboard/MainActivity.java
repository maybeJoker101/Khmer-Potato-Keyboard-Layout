package com.namae0Two.khmeralternativekeyboard;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.namae0Two.khmeralternativekeyboard.data.CharacterButtonData;
import com.namae0Two.khmeralternativekeyboard.util.Util;
import com.namae0Two.khmeralternativekeyboard.view.CharacterButtonKoltin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CharacterButtonData data = new CharacterButtonData();
        data.setMiddle("A");
        data.setTop("B");
        data.setRight("C");
        data.setLeft("E");
        data.setBottom("D");

        CharacterButtonKoltin button = new CharacterButtonKoltin(getApplicationContext(),data);

        int height = Util.getPixelFromDp(100,getApplicationContext());

        button.setLayoutParams(new LinearLayout.LayoutParams(height,height));

        ConstraintLayout mainlayout = (ConstraintLayout)findViewById(R.id.mainlayout);
        mainlayout.addView(button);

        ConstraintSet s = new ConstraintSet();
        s.clone(mainlayout);

        s.connect(button.getId(), ConstraintSet.TOP, mainlayout.getId(), ConstraintSet.TOP, 0);
        s.connect(button.getId(), ConstraintSet.END, mainlayout.getId(), ConstraintSet.END, 0);
        s.connect(button.getId(), ConstraintSet.BOTTOM, mainlayout.getId(), ConstraintSet.BOTTOM, 0);
        s.connect(button.getId(), ConstraintSet.START, mainlayout.getId(), ConstraintSet.START, 0);

        mainlayout.setConstraintSet(s);
    }
}
