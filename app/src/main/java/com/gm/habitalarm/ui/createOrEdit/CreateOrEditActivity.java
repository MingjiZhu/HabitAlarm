package com.gm.habitalarm.ui.createOrEdit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.gm.habitalarm.R;
import com.gm.habitalarm.databinding.ActivityCreateOrEditBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateOrEditActivity extends AppCompatActivity {

    private static final String INTENT_KEY = "com.gm.habitalarm.selected_habit_id";
    private CreateOrEditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(CreateOrEditViewModel.class);
        viewModel.isSaved().observe(
                this,
                (isSaved) -> {
                    if(isSaved) {
                        finishAfterTransition();
                    }
                }
        );

        long habitId = getIntent().getLongExtra(INTENT_KEY, 0);
        if(habitId != 0) {
            viewModel.getHabit().observe(
                    this,
                    habit -> viewModel.initializeValuesWithHabit(habit)
            );
            viewModel.setHabitId(habitId);
            setTitle(R.string.edit);
        }

        ActivityCreateOrEditBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_create_or_edit);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finishAfterTransition();
        return true;
    }

    public static Intent createIntent(Context packageContext, long habitId) {
        return new Intent(packageContext, CreateOrEditActivity.class)
                .putExtra(INTENT_KEY, habitId);
    }
}
