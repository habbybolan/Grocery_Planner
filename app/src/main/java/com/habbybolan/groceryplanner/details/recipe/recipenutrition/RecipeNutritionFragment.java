package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeNutritionBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

public class RecipeNutritionFragment extends Fragment implements RecipeNutritionView {

    @Inject
    RecipeNutritionPresenter recipeNutritionPresenter;
    private FragmentRecipeNutritionBinding binding;
    private RecipeNutritionListener recipeNutritionListener;
    private CustomToolbar customToolbar;
    private RecipeNutritionAdapter adapter;

    public RecipeNutritionFragment() {}

    public static RecipeNutritionFragment newInstance() {
        RecipeNutritionFragment fragment = new RecipeNutritionFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_nutrition, container, false);
        setToolbar();
        setList();
        return binding.getRoot();
    }

    private void setList() {
        adapter = new RecipeNutritionAdapter(recipeNutritionListener.getOfflineRecipe().getNutritionList(), new PropertyChangedInterface() {
            @Override
            public void onPropertyChanged() {
                // todo: remove this if not hiding/showing save/cancel buttons...
            }

            @Override
            public void onRecipeTypeSelected(Nutrition nutrition, int position, TextView v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                MeasurementType.createMeasurementTypeMenu(popupMenu);
                popupMenu.setOnMenuItemClickListener(item -> {
                    String type = item.getTitle().toString();
                    nutrition.setMeasurementId(Nutrition.getMeasurementId(type));
                    v.setText(type);
                    return true;
                });
                popupMenu.show();
            }
        });
        RecyclerView rv = binding.recipeNutritionList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    /**
     * Save the nutrition changes to the Recipe.
     */
    private void saveNutrition() {
        recipeNutritionListener.getOfflineRecipe().updateNutrition(adapter.getCurrNutritionList());
        recipeNutritionPresenter.updateRecipe(recipeNutritionListener.getOfflineRecipe());
        adapter.saveChanges();
    }

    /**
     * Cancel the nutrition changes to the recipe.
     */
    private void cancelNutritionChanges() {
        adapter.cancelChanges();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.nutrition_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSaveIcon(new CustomToolbar.SaveCallback() {
                    @Override
                    public void saveClicked() {
                        saveNutrition();
                    }
                })
                .addCancelIcon(new CustomToolbar.CancelCallback() {
                    @Override
                    public void cancelClicked() {
                        cancelNutritionChanges();
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
        recipeNutritionPresenter.deleteRecipe(recipeNutritionListener.getOfflineRecipe());
        recipeNutritionListener.onRecipeDeleted();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeNutritionListener = (RecipeNutritionListener) context;
    }

    public interface RecipeNutritionListener {
        void onRecipeDeleted();
        OfflineRecipe getOfflineRecipe();
    }
}
