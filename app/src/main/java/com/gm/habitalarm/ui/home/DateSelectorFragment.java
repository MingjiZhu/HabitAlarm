package com.gm.habitalarm.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gm.habitalarm.R;
import com.gm.habitalarm.databinding.FragmentDateSelectorBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DateSelectorFragment extends Fragment {

    private HabitViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        FragmentDateSelectorBinding binding = DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_date_selector,
                        container,
                        false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
