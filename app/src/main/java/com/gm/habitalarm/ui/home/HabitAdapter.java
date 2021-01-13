package com.gm.habitalarm.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.habitalarm.R;
import com.gm.habitalarm.data.Habit;
import com.gm.habitalarm.data.HabitWithRepetition;
import com.gm.habitalarm.data.Repetition;
import com.gm.habitalarm.databinding.ListItemHabitBinding;

import java.util.Arrays;

public class HabitAdapter extends ListAdapter<HabitWithRepetition, HabitAdapter.HabitViewHolder> {

    public interface Callbacks {
        void onHabitSelected(long habitId);
    }

    private HabitViewModel viewModel;

    public HabitAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Context context = recyclerView.getContext();
        viewModel = new ViewModelProvider((ViewModelStoreOwner)context).get(HabitViewModel.class);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_habit;
    }

    @Override
    public HabitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ListItemHabitBinding binding =
                DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        binding.checkbox.setOnClickListener(v -> {
            viewModel.saveOrUpdateRepetition(
                        binding.getRepetition(),
                        binding.getHabit().id,
                        ((CheckBox) v).isChecked());
        });

        return new HabitViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final HabitViewHolder holder, int position) {
        HabitWithRepetition habitWithRepetition = getItem(position);
        holder.bind(habitWithRepetition);
    }

    public class HabitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListItemHabitBinding binding;

        public HabitViewHolder(ListItemHabitBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Callbacks callbacks = (Callbacks) v.getContext();
            if(callbacks != null) {
                long habitId = binding.getHabit().id;
                callbacks.onHabitSelected(habitId);
            }
        }

        public void bind(HabitWithRepetition habitWithRepetition) {

            Repetition repetition = habitWithRepetition.repetition;

            binding.setHabit(habitWithRepetition.habit);
            binding.setRepetition(repetition);

            int state = 0;
            if(repetition != null) {
                state = repetition.state;
            }
            binding.setRepetitionState(state);
        }
    }

    public static final DiffUtil.ItemCallback<HabitWithRepetition> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<HabitWithRepetition>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull HabitWithRepetition oldItem,
                        @NonNull HabitWithRepetition newItem) {

                    if(oldItem.habit.id != newItem.habit.id) {
                        return false;
                    }

                    Repetition r1 = oldItem.repetition;
                    Repetition r2 = newItem.repetition;
                    if (r1 == null && r2 == null) {
                        return true;
                    }

                    if(r1 != null && r2 != null) {
                        return r1.id == r2.id;
                    }

                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull HabitWithRepetition oldItem,
                        @NonNull HabitWithRepetition newItem) {

                    Habit h1 = oldItem.habit;
                    Habit h2 = newItem.habit;
                    Repetition r1 = oldItem.repetition;
                    Repetition r2 = newItem.repetition;

                    boolean isHabitEqual = false;
                    if(h1.name.equals(h2.name) && Arrays.equals(h1.days, h2.days)) {

                        if(h1.notifyingTime == null && h2.notifyingTime == null) {
                            isHabitEqual = true;
                        }

                        if((h1.notifyingTime != null && h2.notifyingTime != null)
                                && h1.notifyingTime.equals(h2.notifyingTime))
                        {
                            isHabitEqual = true;
                        }
                    }

                    boolean isRepetitionEqual = false;
                    if(r1 == null && r2 == null) {
                        isRepetitionEqual = true;
                    }

                    if(r1 != null && r2 != null) {
                        if(r1.state == r2.state && r1.date.equals(r2.date)) {
                            isRepetitionEqual = true;
                        }
                    }

                    return isHabitEqual && isRepetitionEqual;
                }
    };
}