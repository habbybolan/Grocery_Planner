package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.GroceryIngredientListDetailsBinding;
import com.habbybolan.groceryplanner.listfragments.ListAdapter;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.combinedmodels.RecipeWithIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class GroceryIngredientsAdapter extends ListAdapter<GroceryIngredientsAdapter.ViewHolder, GroceryIngredient> {

    private Context context;
    public final static int COLLAPSED = 0;
    public final static int PART_INFO = 1;
    public final static int FULL_INFO = 2;
    public final static String[] INFO_VIEW_LIST = new String[]{"Collapsed", "Part info", "Full info"};

    public int infoView = FULL_INFO;

    GroceryIngredientsAdapter(List<GroceryIngredient> ingredients, GroceryIngredientsView view, Context context) {
        super(view, ingredients);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GroceryIngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.grocery_ingredient_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroceryIngredient groceryIngredient = items.get(position);
        holder.bind(groceryIngredient);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final GroceryIngredientListDetailsBinding binding;

        ViewHolder(GroceryIngredientListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // click listener for clicking on an ingredient
            binding.ingredientListContainer.setOnClickListener(v -> {
                setOnClickItem(binding.ingredientCheckBox, getAdapterPosition());
            });

            // click listener for clicking an ingredient's check box
            binding.ingredientCheckBox.setOnClickListener(v -> {
                setOnClickItemCheckBox(binding.ingredientCheckBox, getAdapterPosition());
            });

            // on long click, go into select mode
            binding.ingredientListContainer.setOnLongClickListener(v -> setOnLongClickItem(getAdapterPosition()));

            // on checking the grocery item, sent to view to be saved in database
            binding.ingredientCheckBoxChecklist.setOnClickListener(l -> {
                GroceryIngredient groceryIngredient = items.get(getAdapterPosition());
                groceryIngredient.setIsChecked(binding.ingredientCheckBoxChecklist.isChecked());
                // update the checked value of the ingredient in the grocery list
                ((GroceryIngredientsView) view).onChecklistSelected(groceryIngredient);
            });
        }

        public void bind(GroceryIngredient groceryIngredient) {
            Ingredient ingredient = groceryIngredient.getIngredient();

            // compact info views
            binding.setIngredientName(ingredient.getName());
            binding.ingredientCheckBoxChecklist.setChecked(groceryIngredient.getIsChecked());
            displayCheckBox(binding.ingredientCheckBox);

            // part info views
            if (infoView == PART_INFO || infoView == FULL_INFO) {
                // set part info views as visible
                binding.iconFoodType.setVisibility(View.VISIBLE);
                binding.txtIngredientQuantity.setVisibility(View.GONE);
                if (ingredient.hasQuantity())
                    binding.setIngredientQuantity(ingredient.getQuantity());
                if (ingredient.hasQuantityMeasId())
                    binding.setIngredientQuantityType(MeasurementType.getMeasurement(ingredient.getQuantityMeasId()));
                binding.setImageResource(ingredient.getFoodType().getImageResource());
            } else {
                binding.iconFoodType.setVisibility(View.GONE);
                binding.txtIngredientQuantity.setVisibility(View.GONE);
            }

            binding.recipeNameHolder.removeAllViews();
            // full info views
            if (infoView == FULL_INFO) {
                for (RecipeWithIngredient recipeWithIngredient : groceryIngredient.getRecipeWithIngredients()) {
                    TextView textView = new TextView(context);
                    textView.setText(recipeWithIngredient.getRecipeName() + ": x" + recipeWithIngredient.getRecipeAmount());
                    binding.recipeNameHolder.addView(textView);
                }
            }
        }
    }

    @IntDef({COLLAPSED, PART_INFO, FULL_INFO})
    @Retention(RetentionPolicy.SOURCE)
    private @interface info_view {}

    public void setInfoView(@info_view int infoView) {
        this.infoView = infoView;
        notifyDataSetChanged();
    }
}
