package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeNutritionDetailsReadOnlyBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.util.List;

public class RecipeNutritionReadOnlyAdapter extends RecyclerView.Adapter<RecipeNutritionReadOnlyAdapter.ViewHolder>{

    // Nutrition list
    private List<Nutrition> nutritionList;

    public RecipeNutritionReadOnlyAdapter(List<Nutrition> nutritionList) {
        this.nutritionList = nutritionList;
    }

    @NonNull
    @Override
    public RecipeNutritionReadOnlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeNutritionDetailsReadOnlyBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_nutrition_details_read_only, parent, false);
        return new RecipeNutritionReadOnlyAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeNutritionReadOnlyAdapter.ViewHolder holder, int position) {
        Nutrition nutrition = nutritionList.get(position);
        holder.bind(nutrition);
    }

    @Override
    public int getItemCount() {
        return nutritionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeNutritionDetailsReadOnlyBinding binding;

        ViewHolder(RecipeNutritionDetailsReadOnlyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Nutrition nutrition) {
            binding.setNutritionTag(nutrition.getName());
            if (nutrition.getAmount() != 0) binding.setNutritionAmount(String.valueOf(nutrition.getAmount()));
            else binding.setNutritionAmount("");
            binding.setNutritionTypeTag(nutrition.getName() + " Type");
            if (nutrition.getMeasurementType().getMeasurementId() != null) binding.setNutritionType(nutrition.getMeasurementType().getMeasurement());
            else binding.setNutritionType("");
        }
    }
}
