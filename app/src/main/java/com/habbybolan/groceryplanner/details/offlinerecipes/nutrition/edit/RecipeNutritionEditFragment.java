package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeNutritionBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

public class RecipeNutritionEditFragment
        extends Fragment implements RecipeNutritionContract.NutritionEditView {

    @Inject
    RecipeNutritionContract.PresenterEdit presenter;
    private FragmentRecipeNutritionBinding binding;
    private RecipeNutritionMyListener recipeNutritionListener;
    private CustomToolbar customToolbar;
    private RecipeNutritionEditAdapter adapter;

    public RecipeNutritionEditFragment() {}

    public static RecipeNutritionEditFragment getInstance(long recipeId) {
        RecipeNutritionEditFragment fragment = new RecipeNutritionEditFragment();
        Bundle args = new Bundle();
        args.putLong(OfflineRecipe.RECIPE, recipeId);
        fragment.setArguments(args);
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
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(OfflineRecipe.RECIPE)) {
            presenter.setupPresenter(this, bundle.getLong(OfflineRecipe.RECIPE));
        }
        setToolbar();
        return binding.getRoot();
    }

    private void setList() {
        adapter = new RecipeNutritionEditAdapter(presenter.getRecipe().getAllNutritionList(), new RecipeNutritionContract.NutritionChangedListener() {

            @Override
            public void nutritionAmountChanged(Nutrition nutrition) {
                presenter.nutritionAmountChanged(nutrition);
            }

            @Override
            public void onRecipeTypeSelected(@NonNull Nutrition nutrition, TextView v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                MeasurementType.createMeasurementTypeMenu(popupMenu);
                popupMenu.setOnMenuItemClickListener(item -> {
                    String type = item.getTitle().toString();
                    nutrition.setMeasurement(MeasurementType.getMeasurementId(type));
                    v.setText(type);
                    presenter.nutritionMeasurementChanged(nutrition);
                    return true;
                });
                popupMenu.show();
            }

            @Override
            public void invalidAction(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView rv = binding.recipeNutritionList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.nutrition_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeNutritionListener.onSwapViewNutrition();
                    }
                })
                .addSyncIcon(new CustomToolbar.SyncCallback() {
                    @Override
                    public void syncClicked() {
                        recipeNutritionListener.onSync();
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
        recipeNutritionListener = (RecipeNutritionMyListener) context;
    }

    @Override
    public void updateRecipe() {
        presenter.loadUpdatedRecipe();
    }

    @Override
    public void displayUpdatedRecipe() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setupRecipeViews() {
        setList();
    }

    public interface RecipeNutritionMyListener extends RecipeNutritionContract.RecipeNutritionMyRecipeListener {
        void onRecipeDeleted();
    }
}
