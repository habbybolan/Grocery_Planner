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
import com.habbybolan.groceryplanner.models.Nutrition;

import java.util.ArrayList;
import java.util.List;

public class RecipeNutritionAdapter extends RecyclerView.Adapter<RecipeNutritionAdapter.ViewHolder>{

    // Holds the nutrition values before save button pressed
    private List<Nutrition> prevNutritionList;
    // keeps track of the changes in text and stores into its nutrition objects
    private List<Nutrition> currNutritionList;
    private PropertyChangedInterface propertyChangedInterface;

    public RecipeNutritionAdapter(List<Nutrition> prevNutritionList, PropertyChangedInterface propertyChangedInterface) {
        this.prevNutritionList = prevNutritionList;
        this.currNutritionList = new ArrayList<>();
        copyPrevIntoCurr();
        this.propertyChangedInterface = propertyChangedInterface;
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
        Nutrition nutrition = currNutritionList.get(position);
        holder.bind(nutrition);
    }

    @Override
    public int getItemCount() {
        return currNutritionList.size();
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
                    if (s.length() != 0) {
                        currNutritionList.get(getAdapterPosition()).setAmount(Integer.parseInt(s.toString()));
                        propertyChangedInterface.onPropertyChanged();
                    }
                }
            });

            binding.recipeNutritionType.setOnClickListener(l -> {
                propertyChangedInterface.onRecipeTypeSelected(currNutritionList.get(getAdapterPosition()), getAdapterPosition(), binding.recipeNutritionType);
            });
        }

        public void bind(Nutrition nutrition) {
            binding.setNutritionTag(nutrition.getName());
            if (nutrition.getAmount() != 0) binding.setNutritionAmount(String.valueOf(nutrition.getAmount()));
            else binding.setNutritionAmount("");
            binding.setNutritionTypeTag(nutrition.getName() + " Type");
            if (nutrition.getMeasurement() != null) binding.setNutritionType(nutrition.getMeasurement());
            else binding.setNutritionType("");
        }
    }

    /**
     * Deep copy the PrevNutritionList into CurrNutritionList
     */
    private void copyPrevIntoCurr() {
        currNutritionList.clear();
        for (Nutrition nutrition : prevNutritionList) {
            currNutritionList.add(nutrition.clone());
        }
    }

    public void saveChanges() {
        prevNutritionList.clear();
        prevNutritionList.addAll(currNutritionList);
    }
    public void cancelChanges() {
        copyPrevIntoCurr();
        notifyDataSetChanged();
    }

    public List<Nutrition> getCurrNutritionList() {
        return currNutritionList;
    }
}
