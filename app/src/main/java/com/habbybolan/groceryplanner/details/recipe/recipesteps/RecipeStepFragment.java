package com.habbybolan.groceryplanner.details.recipe.recipesteps;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.CreateIngredientHolderDetailsBinding;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeStepBinding;
import com.habbybolan.groceryplanner.details.recipe.recipedetailactivity.RecipeDetailActivity;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.Step;
import com.habbybolan.groceryplanner.ui.CreatePopupWindow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeStepFragment extends Fragment implements RecipeStepView {

    @Inject
    RecipeStepPresenter recipeStepPresenter;

    private List<Step> steps = new ArrayList<>();
    private FragmentRecipeStepBinding binding;
    private RecipeStepAdapter adapter;
    private Recipe recipe;
    private RecipeStepListener recipeStepListener;
    private Toolbar toolbar;

    private static final String TAG = "RecipeStepFragment";


    public RecipeStepFragment() {}

    public static RecipeStepFragment getInstance(@NonNull Recipe recipe) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putParcelable(Recipe.RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeStepListener = (RecipeStepListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule(), new IngredientEditModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_step, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        toolbar = binding.toolbarRecipeSteps.toolbar;
        toolbar.inflateMenu(R.menu.menu_ingredient_holder_details);
        toolbar.setTitle(getString(R.string.title_recipe_steps));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        return true;
                    case R.id.action_sort:
                        showSortPopup(getActivity().findViewById(R.id.action_sort));
                        return true;
                    case R.id.action_delete:
                        deleteRecipe();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeStepPresenter.setView(this);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Recipe.RECIPE);
            if (getArguments().containsKey(Step.STEP)) {
                steps = getArguments().getParcelableArrayList(Step.STEP);
            } else
                // only the grocery saved, must retrieve its associated Ingredients
                recipeStepPresenter.createStepList((Recipe) getArguments().getParcelable(Recipe.RECIPE));
        }
    }

    /**
     * Menu popup for giving different ways to sort the list.
     * @param v     The view to anchor the popup menu to
     */
    private void showSortPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.inflate(R.menu.popup_sort_grocery_list);
        popup.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.popup_alphabetically_grocery_list:
                    Toast.makeText(getContext(), "alphabetically", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_test_grocery_list:
                    Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    private void initLayout() {
        adapter = new RecipeStepAdapter(steps, this);
        RecyclerView rv = binding.stepsList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        binding.btnAddStep.setOnClickListener(v -> addNewStep());
    }

    /**
     * Delete the recipe and end the RecipeDetailActivity.
     */
    private void deleteRecipe() {
        PopupWindow popupWindow = new PopupWindow();
        View clickableView = CreatePopupWindow.createPopupDeleteCheck(binding.recipeStepContainer, "recipe", popupWindow);
        clickableView.setOnClickListener(v -> {
            recipeStepPresenter.deleteRecipe(recipe);
            recipeStepListener.onRecipeDeleted();
            popupWindow.dismiss();
        });
    }

    private void addNewStep() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Write a Recipe list name");
        final CreateIngredientHolderDetailsBinding groceryBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.create_ingredient_holder_details, null, false);
        builder.setView(groceryBinding.getRoot());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = groceryBinding.editTxtGroceryName.getText().toString();
                Step step = new Step(recipe.getId(), description, steps.size()+1);
                recipeStepPresenter.addNewStep(recipe, step);
                steps.add(step);
                adapter.notifyItemInserted(steps.size()-1);
            }
        });
        builder.show();
    }

    @Override
    public void showStepList(List<Step> steps) {
        Log.i(TAG, "showStepList: " + this.steps);
        this.steps.clear();
        this.steps.addAll(steps);
        Log.i(TAG, "showStepList: " + this.steps);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe Steps");
    }

    /**
     * Listener interface implemented by {@link RecipeDetailActivity}
     */
    public interface RecipeStepListener {

        void onRecipeDeleted();
    }
}
