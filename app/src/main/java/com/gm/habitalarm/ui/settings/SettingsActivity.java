package com.gm.habitalarm.ui.settings;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;

import com.gm.habitalarm.MainApplication;
import com.gm.habitalarm.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item) {
        boolean ret = true;

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                MainApplication app = (MainApplication) (getApplication().getApplicationContext());
                app.soundOn();
                break;
            default:
                ret = super.onOptionsItemSelected(item);
                break;
        }
        return ret;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat switchDarkThemePref = findPreference(getString(R.string.dark_theme_key_value));
            SwitchPreferenceCompat switchSoundPref = findPreference(getString(R.string.sound_key_value));

            MainApplication app = (MainApplication) (getActivity().getApplication());

            switchDarkThemePref.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        boolean bDarkTheme = (boolean) newValue;
                        app.soundOn();
                        if (bDarkTheme) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        return true;
                    });

            switchSoundPref.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        boolean switchOn = (boolean) newValue;
                        if (switchOn) {
                            app.sound = true;
                            app.soundOn();
                        } else {
                            app.sound = false;
                        }
                        return true;
                    });
        }

    }

}