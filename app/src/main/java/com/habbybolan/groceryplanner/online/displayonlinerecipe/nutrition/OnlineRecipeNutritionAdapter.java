package com.habbybolan.groceryplanner.online.displayonlinerecipe.nutrition;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.OnlineRecipeNutritionDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.util.List;

public class OnlineRecipeNutritionAdapter extends RecyclerView.Adapter<OnlineRecipeNutritionAdapter.ViewHolder> {

    private List<Nutrition> nutritionList;

    public OnlineRecipeNutritionAdapter(List<Nutrition> nutritionList) {
        this.nutritionList = nutritionList;
    }

    @NonNull
    @Override
    public OnlineRecipeNutritionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        OnlineRecipeNutritionDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.online_recipe_nutrition_details, parent, false);
        return new OnlineRecipeNutritionAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineRecipeNutritionAdapter.ViewHolder holder, int position) {
        Nutrition nutrition = nutritionList.get(position);
        holder.bind(nutrition);
    }

    @Override
    public int getItemCount() {
        return nutritionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnlineRecipeNutritionDetailsBinding binding;

        ViewHolder(OnlineRecipeNutritionDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Nutrition nutrition) {
            binding.setNutritionTag(nutrition.getName());
            if (nutrition.getAmount() != 0) binding.setNutritionAmount(String.valueOf(nutrition.getAmount()));
            else binding.setNutritionAmount("");
            binding.setNutritionTypeTag(nutrition.getName() + " Type");
            if (nutrition.getMeasurementId() != null) binding.setNutritionType(nutrition.getMeasurement());
            else binding.setNutritionType("");
        }
    }
}
