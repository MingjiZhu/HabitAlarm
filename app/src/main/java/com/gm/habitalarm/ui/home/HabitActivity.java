package com.gm.habitalarm.ui.home;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.gm.habitalarm.MainApplication;
import com.gm.habitalarm.R;
import com.gm.habitalarm.ui.createOrEdit.CreateOrEditActivity;
import com.gm.habitalarm.ui.detail.HabitDetailActivity;
import com.gm.habitalarm.ui.settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalTime;
import java.util.Calendar;

import androidx.preference.PreferenceManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HabitActivity extends AppCompatActivity implements HabitAdapter.Callbacks {

    private HabitViewModel viewModel;

    @Override
    public void onHabitSelected(long habitId) {
        startActivity(HabitDetailActivity.createIntent(this, habitId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        viewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = sharedPref.getBoolean("dark_theme", false);

        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        getSupportActionBar().hide();

        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav);
        MainApplication app = (MainApplication)(getApplication().getApplicationContext());
        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_adding:
                    startNewActivity(bottomNavView, CreateOrEditActivity.class);
                    app.soundOn();
                    break;
                case R.id.navigation_settings:
                    startNewActivity(bottomNavView, SettingsActivity.class);
                    app.soundOn();
                    break;
            }
            return true;
        });
    }

    private void startNewActivity(View view, Class<?> activityClass) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityOptions options = ActivityOptions
                    .makeScaleUpAnimation(
                            view,
                            0,
                            0,
                            view.getWidth(),
                            view.getHeight());

            startActivity(new Intent(this, activityClass), options.toBundle());
        } else {
            startActivity(new Intent(this, activityClass));
        }
    }
}