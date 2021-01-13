package com.gm.habitalarm;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.SoundPool;

import androidx.preference.PreferenceManager;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MainApplication extends Application {

    public SharedPreferences sharedPref;
    public boolean sound;
    private static final int MAX_SOUND_COUNT = 3;
    private static int soundId;
    private static SoundPool soundPool;

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sound = sharedPref.getBoolean("sound", false);

        soundPool = new SoundPool.Builder().setMaxStreams(MAX_SOUND_COUNT).build();
        soundId = soundPool.load(this, R.raw.sound, 1);

        soundOn();
    }

    public void soundOn() {
        if (sound) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        soundRelease();
    }

    public void soundRelease(){
        soundPool.release();
    }
}
