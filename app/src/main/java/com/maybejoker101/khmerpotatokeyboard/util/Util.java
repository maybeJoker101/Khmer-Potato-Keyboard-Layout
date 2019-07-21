package com.maybejoker101.khmerpotatokeyboard.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import static android.util.DisplayMetrics.DENSITY_DEFAULT;

public class Util {

    public static int getPixelFromDp(int dp, Context context){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int getPixelFromDp(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public static int getPixelFromSp(int sp ,Context context ){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int getDpFromPixel(int pixel, Context context) {
        return pixel / context.getResources().getDisplayMetrics().densityDpi / DENSITY_DEFAULT;
    }

}
