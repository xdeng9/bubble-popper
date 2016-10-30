package com.example.joseph.bubblepopper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.joseph.bubblepopper.utils.PixelConverter;

import static android.R.attr.value;

/**
 * Created by Joseph on 10/29/2016.
 */

public class Bubble extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator mAnimator;
    private BubbleListener mListener;
    private boolean mPopped;

    public Bubble (Context context){
        super(context);
    }

    public Bubble (Context context, int height){
        super(context);
        this.setImageResource(R.drawable.bubble);

        mListener = (BubbleListener) context;

        int dpHeight = PixelConverter.pixelsToDp(height, context);
        int dpWidth = dpHeight;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }

    public void releaseBubble(int screenHeight, int duration){
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(0f, screenHeight);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(!mPopped){
            mListener.popBubble(this, false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setY((Float) animation.getAnimatedValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!mPopped && event.getAction()==MotionEvent.ACTION_DOWN){
            mListener.popBubble(this, true);
            mPopped = true;
            mAnimator.cancel();
        }
        return super.onTouchEvent(event);
    }

    public void stopAnimate(boolean popped){
        mPopped = popped;
        mAnimator.cancel();
    }

    public interface BubbleListener{
        public void popBubble(Bubble bubble, boolean touched);
    }
}
