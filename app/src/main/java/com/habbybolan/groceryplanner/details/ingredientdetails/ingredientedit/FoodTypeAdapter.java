package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientFoodTypeDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;

import java.util.List;

public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.ViewHolder> {

    private List<FoodType> foodTypes;
    private String selectedFoodType;
    private IngredientEditFragment.FoodTypeListener listener;

    /**
     * FoodType constructor.
     * @param foodTypes         List of FoodType strings
     * @param selectedFoodType  The currently selected type of the food type
     */
    public FoodTypeAdapter(List<FoodType> foodTypes, String selectedFoodType, IngredientEditFragment.FoodTypeListener listener) {
        this.foodTypes = foodTypes;
        this.selectedFoodType = selectedFoodType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientFoodTypeDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_food_type_details, parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public long getItemId(int position) {
        return foodTypes.get(position).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodType foodType = foodTypes.get(position);
        holder.bind(foodType);
    }

    @Override
    public int getItemCount() {
        return foodTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        IngredientFoodTypeDetailsBinding binding;

        ViewHolder(IngredientFoodTypeDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.foodTypeContainer.setOnClickListener(l -> {
                selectFoodType(getAdapterPosition());
                listener.onChange();
            });
        }

        public void bind(FoodType foodType) {
            binding.setType(foodType.getType());
            binding.setImageResource(foodType.getImageResource());
            // if this item is selected, alter its view
            if (foodType.getType().equals(selectedFoodType)) {
                binding.setIsSelected(true);
            } else {
                binding.setIsSelected(false);
            }
        }
    }

    /**
     * Clicking food type calls on listener onClick and sets the selected item to at position.
     * @param position  Index in FoodTypes of the item selected.
     */
    private void selectFoodType(int position) {
        FoodType foodType = foodTypes.get(position);
        selectedFoodType = foodType.getType();
        notifyDataSetChanged();
    }

    String getSelectedFoodType() {
        return selectedFoodType;
    }

    void resetSelected(FoodType selectedFoodType) {
        if (selectedFoodType == null) this.selectedFoodType = FoodType.OTHER;
        else this.selectedFoodType = selectedFoodType.getType();
        notifyDataSetChanged();
    }
}
