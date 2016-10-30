package com.example.joseph.bubblepopper.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Joseph on 10/29/2016.
 */

public class PixelConverter {

    public static int pixelsToDp(int px, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics());
    }
}
