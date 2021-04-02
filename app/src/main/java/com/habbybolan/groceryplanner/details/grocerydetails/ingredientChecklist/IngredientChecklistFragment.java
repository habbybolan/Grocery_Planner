package com.habbybolan.groceryplanner.details.grocerydetails.ingredientChecklist;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentIngredientChecklistBinding;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientChecklistFragment extends Fragment {

    private List<Ingredient> ingredientsUnchecked = new ArrayList<>();
    private List<Ingredient> ingredientsChecked = new ArrayList<>();
    private FragmentIngredientChecklistBinding binding;
    private IngredientChecklistListener ingredientChecklistListener;

    private IngredientChecklistAdapter adapterUnchecked;
    private IngredientChecklistAdapter adapterChecked;

    public IngredientChecklistFragment() {}

    public static IngredientChecklistFragment newInstance(List<Ingredient> ingredients) {
        IngredientChecklistFragment fragment = new IngredientChecklistFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Ingredient.INGREDIENT_UNCHECKED, (ArrayList<? extends Parcelable>) ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(Ingredient.INGREDIENT_CHECKED))
                ingredientsUnchecked = getArguments().getParcelableArrayList(Ingredient.INGREDIENT_CHECKED);
            if (getArguments().containsKey(Ingredient.INGREDIENT_UNCHECKED))
                ingredientsUnchecked = getArguments().getParcelableArrayList(Ingredient.INGREDIENT_UNCHECKED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_checklist, container, false);
        adapterUnchecked = new IngredientChecklistAdapter(ingredientsUnchecked, Ingredient.INGREDIENT_UNCHECKED, new ChecklistOnClick() {
            @Override
            public void onIngredientClicked(Ingredient ingredient) {
                adapterChecked.insertIngredient(ingredient);
            }
        });
        binding.rvIngredientChecklistUnchecked.setAdapter(adapterUnchecked);
        adapterChecked = new IngredientChecklistAdapter(ingredientsChecked, Ingredient.INGREDIENT_CHECKED, new ChecklistOnClick() {
            @Override
            public void onIngredientClicked(Ingredient ingredient) {
                adapterUnchecked.insertIngredient(ingredient);
            }
        });
        binding.rvIngredientChecklistChecked.setAdapter(adapterChecked);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ingredientChecklistListener = (IngredientChecklistListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ingredientChecklistListener = null;
    }

    /*
    public interface IngredientChecklistListener {
        void onList
    }*/

    public interface ChecklistOnClick {
        void onIngredientClicked(Ingredient ingredient);
    }

    public interface IngredientChecklistListener {

        void leaveCheckList();
    }
}
