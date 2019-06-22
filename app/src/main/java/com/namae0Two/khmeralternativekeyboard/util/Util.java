package com.namae0Two.khmeralternativekeyboard.util;

import android.content.Context;
import android.util.TypedValue;

public class Util {

    public static int getPixelFromDp(int dp, Context context){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int getPixelFromSp(int sp ,Context context ){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
