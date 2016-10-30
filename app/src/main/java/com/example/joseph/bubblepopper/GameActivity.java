package com.example.joseph.bubblepopper;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.os.Build.VERSION_CODES.M;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends AppCompatActivity implements Bubble.BubbleListener {

    private static final int MIN_ANIMATION_DELAY = 500;
    private static final int MAX_ANIMATION_DELAY = 1500;
    private static final int MIN_ANIMATION_DURATION = 1000;
    private static final int MAX_ANIMATION_DURATION = 8000;
    private int BUBBLES_PER_LEVEL=5;
    private ViewGroup mContentView;
    private int mScreenWidth, mScreenHeight, mLevel,mScore;
    private int starsLeft = 5;
    private List<Bubble> mBubbles = new ArrayList<>();
    private boolean mPlaying;

    private TextView scoreTextView, levelTextView;
    private ImageView star1, star2, star3, star4, star5;
    private Button goButton;
    private int mBubblesPopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreTextView = (TextView) findViewById(R.id.score_display);
        levelTextView = (TextView) findViewById(R.id.level_display);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        goButton = (Button) findViewById(R.id.go_button);

        getWindow().setBackgroundDrawableResource(R.drawable.water_background);
        mContentView = (ViewGroup) findViewById(R.id.activity_game);
        setFullScreenMode();

        ViewTreeObserver viewTreeObserver = mContentView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mScreenHeight = mContentView.getHeight();
                    mScreenWidth = mContentView.getWidth();
                }
            });
        }
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFullScreenMode();
            }
        });

        updateDisplay();
    }

    private void setFullScreenMode() {
        ViewGroup root = (ViewGroup) findViewById(R.id.activity_game);
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreenMode();
    }

    private void resetGame(){
        setFullScreenMode();
        mScore = 0;
        mLevel = 0;
        starsLeft = 5;
        star1.setVisibility(View.VISIBLE);
        star2.setVisibility(View.VISIBLE);
        star3.setVisibility(View.VISIBLE);
        star4.setVisibility(View.VISIBLE);
        star5.setVisibility(View.VISIBLE);
        startLevel();
    }

    private void startLevel() {
        mLevel++;
        updateDisplay();
        BubbleLauncher launcher = new BubbleLauncher();
        launcher.execute(mLevel);
        mPlaying = true;
        mBubblesPopped = 0;
    }

    public void startGame(View view) {
        if(mPlaying){

        }else if(starsLeft==0){
            resetGame();
        }else
            startLevel();
    }

    private void finishLevel(){
        Toast.makeText(this, String.format("Level %d completed!", mLevel), Toast.LENGTH_SHORT).show();
        mPlaying = false;
        goButton.setText("Continue");
    }

    @Override
    public void popBubble(Bubble bubble, boolean touched) {
        mContentView.removeView(bubble);
        mBubbles.remove(bubble);
        
        mBubblesPopped++;

        if (touched){
            mScore++;
        }else {
                switch (starsLeft){
                    case 1: star1.setVisibility(View.INVISIBLE);
                        gameOver();
                        break;
                    case 2: star2.setVisibility(View.INVISIBLE);
                        break;
                    case 3: star3.setVisibility(View.INVISIBLE);
                        break;
                    case 4: star4.setVisibility(View.INVISIBLE);
                        break;
                    case 5: star5.setVisibility(View.INVISIBLE);
                        break;
                }
                starsLeft--;
        }
        updateDisplay();
        
        if(mBubblesPopped ==  BUBBLES_PER_LEVEL){
            finishLevel();
        }
    }

    private void gameOver(){
        Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show();
        for (Bubble bubble : mBubbles){
            mContentView.removeView(bubble);
            bubble.stopAnimate(true);
        }
        mBubbles.clear();
        mPlaying = false;
        goButton.setText("Play again");
    }

    private void updateDisplay() {
        scoreTextView.setText(String.valueOf(mScore));
        levelTextView.setText(String.valueOf(mLevel));
    }

    private class BubbleLauncher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MIN_ANIMATION_DELAY,
                    (MAX_ANIMATION_DELAY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int balloonsLaunched = 0;
            while (mPlaying && balloonsLaunched < BUBBLES_PER_LEVEL) {

//              Get a random horizontal position for the next balloon
                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(mScreenWidth - 200);
                publishProgress(xPosition);
                balloonsLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchBubble(xPosition);
        }

    }

    private void launchBubble(int x) {

        Bubble bubble = new Bubble(this, 150);
        mBubbles.add(bubble);

//      Set balloon vertical position and dimensions, add to container
        bubble.setX(x);
        bubble.setY(mScreenHeight + bubble.getHeight());
        mContentView.addView(bubble);

        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (mLevel * 1000));
        bubble.releaseBubble(mScreenHeight, duration);

    }
}
