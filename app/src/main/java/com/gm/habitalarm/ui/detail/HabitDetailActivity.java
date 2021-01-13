package com.gm.habitalarm.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.gm.habitalarm.R;
import com.gm.habitalarm.databinding.ActivityHabitDetailBinding;
import com.gm.habitalarm.ui.createOrEdit.CreateOrEditActivity;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HabitDetailActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private static final String INTENT_KEY = "com.gm.habitalarm.selected_habit_id";

    private static final int POS_WEEK = 0;
    private static final int POS_MONTH = 1;

    private ActivityHabitDetailBinding binding;
    private HabitDetailViewModel viewModel;
    private long habitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(HabitDetailViewModel.class);

        habitId = getIntent().getLongExtra(INTENT_KEY, 0);
        viewModel.getHabitWithRepetitions().observe(
                this,
                habitWithRepetitions -> {
                    if(habitWithRepetitions != null) {
                        binding.habitChart.setHabitWithRepetitions(habitWithRepetitions);
                        getSupportActionBar().setTitle(habitWithRepetitions.habit.name.toUpperCase());
                    }
                }
        );
        viewModel.setHabitId(habitId);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_habit_detail);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        List<String> categories = new ArrayList<>();
        categories.add("Week");
        categories.add("Month");

        // Create adapter for spinner
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case POS_WEEK:
                binding.habitChart.setIsMonth(false);
                break;
            case POS_MONTH:
                binding.habitChart.setIsMonth(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_habit_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(CreateOrEditActivity.createIntent(this, habitId));
                break;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_message)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            viewModel.removeHabit();
                            onBackPressed();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            default:
                finishAfterTransition();
                break;
        }

        return true;
    }

    public static Intent createIntent(Context packageContext, long habitId) {
        return new Intent(packageContext, HabitDetailActivity.class)
                .putExtra(INTENT_KEY, habitId);
    }
}