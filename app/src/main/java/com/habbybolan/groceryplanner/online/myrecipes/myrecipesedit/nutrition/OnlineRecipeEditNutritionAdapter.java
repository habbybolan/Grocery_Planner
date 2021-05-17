package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.nutrition;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeNutritionDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.util.List;

public class OnlineRecipeEditNutritionAdapter extends RecyclerView.Adapter<OnlineRecipeEditNutritionAdapter.ViewHolder> {

    private List<Nutrition> nutritionList;
    private OnlineRecipeNutritionContract.NutritionAdapterView view;

    public OnlineRecipeEditNutritionAdapter(List<Nutrition> nutritionList, OnlineRecipeNutritionContract.NutritionAdapterView view) {
        this.nutritionList = nutritionList;
        this.view = view;
    }

    @NonNull
    @Override
    public OnlineRecipeEditNutritionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeNutritionDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_nutrition_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineRecipeEditNutritionAdapter.ViewHolder holder, int position) {
        Nutrition nutrition = nutritionList.get(position);
        holder.bind(nutrition);
    }

    @Override
    public int getItemCount() {
        return nutritionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeNutritionDetailsBinding binding;
        public ViewHolder(RecipeNutritionDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.recipeNutritionType.setOnClickListener(l -> {
                view.onNutritionTypeSelected(binding.recipeNutritionType, nutritionList.get(getAdapterPosition()));
            });
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
