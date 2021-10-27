package com.habbybolan.groceryplanner.details.myrecipe.nutrition.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeNutritionReadOnlyBinding;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit.RecipeNutritionEditFragment;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

/**
 *
 */
public class RecipeNutritionReadOnlyFragment extends Fragment {

    @Inject
    RecipeNutritionContract.PresenterReadOnly presenter;
    private FragmentRecipeNutritionReadOnlyBinding binding;
    private RecipeNutritionEditFragment.RecipeNutritionListener recipeNutritionListener;
    private CustomToolbar customToolbar;
    private RecipeNutritionReadOnlyAdapter adapter;

    public RecipeNutritionReadOnlyFragment() {}

    public static RecipeNutritionReadOnlyFragment newInstance() {
        RecipeNutritionReadOnlyFragment fragment = new RecipeNutritionReadOnlyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_nutrition_read_only, container, false);
        setToolbar();
        setList();
        return binding.getRoot();
    }

    private void setList() {
        adapter = new RecipeNutritionReadOnlyAdapter(recipeNutritionListener.getMyRecipe().getNutritionList());
        RecyclerView rv = binding.recipeNutritionList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.nutrition_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeNutritionListener.onSwapViewNutrition();
                    }
                })
                .build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    /**
     * Delete the Recipe and leave the Fragment.
     */
    private void deleteRecipe() {
        recipeNutritionListener.onRecipeDeleted();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeNutritionListener = (RecipeNutritionEditFragment.RecipeNutritionListener) context;
    }

    public interface RecipeNutritionListener extends RecipeNutritionContract.RecipeNutritionListener {
    }
}