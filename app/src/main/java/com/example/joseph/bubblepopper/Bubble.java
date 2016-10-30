package com.example.joseph.bubblepopper;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joseph.bubblepopper.utils.PixelConverter;

/**
 * Created by Joseph on 10/29/2016.
 */

public class Bubble extends ImageView {

    public Bubble (Context context){
        super(context);
    }

    public Bubble (Context context, int height){
        super(context);
        this.setImageResource(R.drawable.bubble);

        int dpHeight = PixelConverter.pixelsToDp(height, context);
        int dpWidth = dpHeight;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }
}
