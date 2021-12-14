package com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewEditBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes.AddRecipeToGroceryListFragment;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.CustomToolbar;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;

import javax.inject.Inject;

public class RecipeOverviewEditFragment extends Fragment implements RecipeOverviewContract.OverviewEditView {

    @Inject
    RecipeOverviewContract.PresenterEdit presenter;

    private FragmentRecipeOverviewEditBinding binding;
    private RecipeOverviewMyListener recipeOverviewListener;
    private CustomToolbar customToolbar;
    private RecipeCategory selectedRecipeCategory;
    private RecipeTagRecyclerView tagRV;

    public RecipeOverviewEditFragment() {}

    public static RecipeOverviewEditFragment getInstance(long recipeId) {
        RecipeOverviewEditFragment fragment = new RecipeOverviewEditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(OfflineRecipe.RECIPE, recipeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeOverviewListener = (RecipeOverviewMyListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_overview_edit, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(OfflineRecipe.RECIPE)) {
            presenter.setupPresenter(this, bundle.getLong(OfflineRecipe.RECIPE));
        }
        setToolbar();
        return binding.getRoot();
    }

    private void setRecipeGroceryFragment() {
        Fragment RecipeGroceryFragment = AddRecipeToGroceryListFragment.newInstance(presenter.getRecipe());
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.recipesGroceryContainer.getId(), RecipeGroceryFragment).commit();
    }

    /**
     * Set up RecyclerView for displaying the tags added to the recipe
     */
    private void setRV() {
        tagRV = new RecipeTagRecyclerView(presenter.getRecipe().getRecipeTags(), this, binding.recipeOverviewRvTags, getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        // set the text listeners once the initial view values are set
        setEditTextListeners();
    }

    /**
     * Add a text watcher for when user changes serving size, cook time, prep time, recipe name, and description.
     * Calls the new value to be saved by saving the OfflineRecipe into room database.
     */
    private void setEditTextListeners() {
        // text watcher to save the new value
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                saveRecipe();
            }
        };
        binding.recipeOverviewServingSize.addTextChangedListener(textWatcher);
        binding.recipeOverviewName.addTextChangedListener(textWatcher);
        binding.recipeOverviewCookTime.addTextChangedListener(textWatcher);
        binding.recipeOverviewPrepTime.addTextChangedListener(textWatcher);
        binding.recipeOverviewDescription.addTextChangedListener(textWatcher);
    }

    /**
     * Sets the tag clicker for adding a new tag to the recipe.
     */
    private void setTagClicker() {
        binding.recipeOverviewBtnAddTag.setOnClickListener(l -> {
            String tagTitle = binding.recipeOverviewTag.getText().toString();
            presenter.addRecipeTag(tagTitle);
        });
    }

    /**
     * Saves the recipe changes, values directly inside RecipeEntity.
     */
    private void saveRecipe() {
        String name = binding.recipeOverviewName.getText().toString();
        String servingSize = binding.recipeOverviewServingSize.getText().toString();
        String cookTime = binding.recipeOverviewCookTime.getText().toString();
        String prepTime = binding.recipeOverviewPrepTime.getText().toString();
        String description = binding.recipeOverviewDescription.getText().toString();

        // update the offline recipe
        presenter.updateRecipe(name, servingSize, cookTime, prepTime, description, selectedRecipeCategory);
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.overview_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeOverviewListener.onSwapViewOverview();
                    }
                })
                .addSyncIcon(new CustomToolbar.SyncCallback() {
                    @Override
                    public void syncClicked() {
                        recipeOverviewListener.onSync();
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
     * Delete the Recipe and leave the fragment
     */
    private void deleteRecipe() {
        recipeOverviewListener.onRecipeDeleted();
    }

    /**
     * Initiate necessary onClicks and general data.
     */
    private void initClickers() {
        // clicker for selecting category to display all categories
        binding.recipeOverviewCategory.setOnClickListener(v -> {
            // display the recipe categories if they are loaded in
            presenter.displayRecipeCategories();
        });
    }

    /**
     * Sets the display to the current recipe's values.
     */
    private void setDisplayToCurrentRecipe() {
        binding.setName(presenter.getRecipe().getName());

        // if the recipe has no category, display it has no category
        if (presenter.getRecipe().getCategoryId() == null)
            binding.setCategoryName(Category.NO_CATEGORY);
        else
            // otherwise, fetch the categoryId and display it
            presenter.fetchRecipeCategory();

        // set serving size, prep time, cook time, and description
        binding.setServingSize(String.valueOf(presenter.getRecipe().getServingSize()));
        binding.setCookTime(String.valueOf(presenter.getRecipe().getCookTime()));
        binding.setPrepTime(String.valueOf(presenter.getRecipe().getPrepTime()));
        binding.setDescription(presenter.getRecipe().getDescription());
    }

    @Override
    public void createCategoriesAlertDialogue(String[] categoryNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(categoryNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedRecipeCategory = presenter.getRecipeCategory(which);
                binding.setCategoryName(categoryNames[which]);
                saveRecipe();
            }
        });
        builder.show();
    }

    @Override
    public void displayRecipeCategory(RecipeCategory recipeCategory) {
        selectedRecipeCategory = recipeCategory;
        binding.setCategoryName(recipeCategory.getName());
    }

    @Override
    public void displayUpdatedRecipe() {
        // update tags
        tagRV.updateDisplay();
        setDisplayToCurrentRecipe();
    }

    @Override
    public void setupRecipeViews() {
        setRV();
        setTagClicker();
        initClickers();
        setDisplayToCurrentRecipe();
        setRecipeGroceryFragment();
        presenter.loadAllRecipeCategories();
    }

    @Override
    public void updateTagDisplay() {
        tagRV.itemInserted(presenter.getRecipe().getRecipeTags().size()-1);
        binding.recipeOverviewTag.setText("");
    }

    @Override
    public void onDeleteRecipeTag(RecipeTag recipeTag) {
        presenter.deleteRecipeTag(recipeTag);
    }

    @Override
    public void updateRecipe() {
        presenter.loadUpdatedRecipe();
    }

    public interface RecipeOverviewMyListener extends RecipeOverviewContract.RecipeOverviewMyRecipeListener {
        void onRecipeDeleted();
    }
}
