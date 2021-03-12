package com.habbybolan.groceryplanner.details.grocerydetails.ingredientChecklist;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientChecklistDetailsBinding;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public class IngredientChecklistAdapter extends RecyclerView.Adapter<IngredientChecklistAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private String listType;
    private IngredientChecklistFragment.ChecklistOnClick onClickListener;

    public IngredientChecklistAdapter(List<Ingredient> ingredients, String listType, IngredientChecklistFragment.ChecklistOnClick onClickListener) {
        this.ingredients = ingredients;
        this.listType = listType;
        this.onClickListener = onClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientChecklistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientChecklistDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_checklist_details, parent, false);
        return new IngredientChecklistAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientChecklistAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private IngredientChecklistDetailsBinding binding;

        ViewHolder(IngredientChecklistDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.ingredientChecklistBox.setOnClickListener(l -> {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Ingredient ingredient = ingredients.get(getAdapterPosition());
                        removeIngredient(getAdapterPosition());
                        onClickListener.onIngredientClicked(ingredient);
                    }
                }, 200);
            });
        }

        public void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            if (ingredient.hasPrice()) binding.setIngredientPrice(ingredient.getPrice());
            if (ingredient.hasPricePer()) binding.setIngredientPricePer(ingredient.getPricePer());
            if (ingredient.hasPriceType()) binding.setIngredientPriceType(ingredient.getPriceType());
            if (ingredient.hasQuantity()) binding.setIngredientQuantity(ingredient.getQuantity());
            if (ingredient.hasQuantityType()) binding.setIngredientQuantityType(ingredient.getQuantityType());
            // set the Checked list items as always checks, unchecked items as always unchecked
            binding.ingredientChecklistBox.setChecked(listType.equals(Ingredient.INGREDIENT_CHECKED));
        }
    }

    /**
     * Remove an ingredient from ingredients and update the RecyclerView
     * @param position  Index of the ingredient to delete from ingredients
     */
    private void removeIngredient(int position) {
        ingredients.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Add a new ingredient to the bottom of ingredients and update the RecyclerView
     * @param ingredient    Ingredient to insert
     */
    public void insertIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        notifyItemInserted(ingredients.size()-1);
    }
}
