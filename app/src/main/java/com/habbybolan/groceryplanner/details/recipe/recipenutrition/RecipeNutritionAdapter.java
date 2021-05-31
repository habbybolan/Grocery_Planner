package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeNutritionDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.util.List;

public class RecipeNutritionAdapter extends RecyclerView.Adapter<RecipeNutritionAdapter.ViewHolder>{

    // Nutrition list
    private List<Nutrition> nutritionList;
    private RecipeNutritionContract.NutritionChangedListener listener;

    public RecipeNutritionAdapter(List<Nutrition> nutritionList, RecipeNutritionContract.NutritionChangedListener listener) {
        this.nutritionList = nutritionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeNutritionDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_nutrition_details, parent, false);
        return new RecipeNutritionAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Nutrition nutrition = nutritionList.get(position);
        holder.bind(nutrition);
    }

    @Override
    public int getItemCount() {
        return nutritionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeNutritionDetailsBinding binding;

        ViewHolder(RecipeNutritionDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.recipeNutritionAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    // value either added or changed
                    int amount = s.toString().length() == 0 ? 0 : Integer.parseInt(s.toString());
                    Nutrition nutrition = nutritionList.get(getAdapterPosition());
                    nutrition.setAmount(amount);
                    listener.nutritionAmountChanged(nutrition);
                }
            });

            binding.btnRemoveNutrition.setOnClickListener(l -> {
                Nutrition nutrition = nutritionList.get(getAdapterPosition());
                nutrition.setAmount(0);
                nutrition.setMeasurementId(null);
                listener.nutritionAmountChanged(nutrition);
                notifyItemChanged(getAdapterPosition());
            });

            binding.recipeNutritionType.setOnClickListener(l -> {
                Nutrition nutrition = nutritionList.get(getAdapterPosition());
                listener.onRecipeTypeSelected(nutrition, binding.recipeNutritionType);
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

    public List<Nutrition> getCurrNutritionList() {
        return nutritionList;
    }
}
