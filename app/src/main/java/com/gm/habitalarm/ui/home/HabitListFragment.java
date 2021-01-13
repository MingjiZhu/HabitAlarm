package com.gm.habitalarm.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.habitalarm.MainApplication;
import com.gm.habitalarm.R;

public class HabitListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int columnCount = 1;

    private HabitViewModel habitViewModel;

    public HabitListFragment() {
    }

    public static HabitListFragment newInstance(int columnCount) {
        HabitListFragment fragment = new HabitListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainApplication app = (MainApplication) (getActivity().getApplication());
        app.soundOn();
        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }

            HabitAdapter habitAdapter = new HabitAdapter();
            recyclerView.setAdapter(habitAdapter);

            habitViewModel.habitsWithRepetitions.observe(
                    getViewLifecycleOwner(),
                    habitsWithRepetitions -> {
                        habitAdapter.submitList(habitsWithRepetitions);
                    }
            );
        }

        return view;
    }
}