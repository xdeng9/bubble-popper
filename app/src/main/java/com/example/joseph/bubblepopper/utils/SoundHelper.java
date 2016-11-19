package com.example.joseph.bubblepopper.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;

import com.example.joseph.bubblepopper.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by administrator on 10/30/16.
 */

public class SoundHelper {

    private MediaPlayer mPlayer;
    private SoundPool mSoundPool;
    private int mSoundID;
    private boolean mLoaded;
    private float mVolume;

    public SoundHelper(Activity activity) {

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = actVolume / maxVolume;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
        } else {
            //noinspection deprecation
            mSoundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded = true;
            }
        });
        mSoundID = mSoundPool.load(activity, R.raw.pop, 1);
    }

   
//     public void playSound() {
//         if (mLoaded) {
//             mSoundPool.play(mSoundID, mVolume, mVolume, 1, 0, 1f);
//         }
//     }

    public void setupMusicPlayer(Context context){
        mPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.bgsound);
        mPlayer.setVolume(.5f,.5f);
        mPlayer.setLooping(true);
    }

    public void playMusic(){
        if (mPlayer !=null){
            mPlayer.start();
        }
    }

    public void pauseMusic(){
        if (mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.pause();
        }
    }
}
