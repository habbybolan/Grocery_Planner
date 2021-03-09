package com.habbybolan.groceryplanner.details.recipe.recipesteps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.StepsListDetailsBinding;
import com.habbybolan.groceryplanner.models.Step;

import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private List<Step> steps;
    private RecipeStepView view;

    RecipeStepAdapter(List<Step> steps, RecipeStepView view) {
        this.steps = steps;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeStepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        StepsListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.steps_list_details, parent, false);
        return new RecipeStepAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapter.ViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final StepsListDetailsBinding binding;

        ViewHolder(StepsListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.stepContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo:
                }
            });
        }

        void bind(Step step) {
            binding.setStepNumber(step.getStepNumber());
            binding.setDescription(step.getDescription());
        }
    }
}
