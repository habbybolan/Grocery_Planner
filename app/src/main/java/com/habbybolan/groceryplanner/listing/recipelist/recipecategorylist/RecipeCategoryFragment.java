package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.CreatePopupDetailsBinding;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeCategoryBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeCategoryModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.toolbar.CustomToolbar;

import javax.inject.Inject;

public class RecipeCategoryFragment extends NonCategoryListFragment<RecipeCategory> implements RecipeCategoryView {

    private FragmentRecipeCategoryBinding binding;
    private RecipeCategoryListener recipeCategoryListener;
    private CustomToolbar customToolbar;
    private SortType sortType = new SortType();

    @Inject
    RecipeCategoryPresenter recipeCategoryPresenter;

    public RecipeCategoryFragment() {}

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeCategoryListener = (RecipeCategoryListener) context;
        attachListener(getContext());
    }*/

    /*public static RecipeCategoryFragment newInstance(String param1, String param2) {
        RecipeCategoryFragment fragment = new RecipeCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    /**
     * Listeners implemented explicitly inside RecipeListActivity. Manually attach listener.
     * @param recipeCategoryListener    Implemented listener from RecipeListActivity
     */
    public void attachListener(RecipeCategoryListener recipeCategoryListener) {
        this.recipeCategoryListener = recipeCategoryListener;
        itemListener = recipeCategoryListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeCategorySubComponent(new RecipeCategoryModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_ingredient_holder_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_category, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        String[] titleMethods = new String[]{"Category", "Recipe"};
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_category_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        recipeCategoryPresenter.searchRecipeCategories(search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        recipeCategoryPresenter.fetchRecipeCategories();
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .allowClickTitle(new CustomToolbar.TitleSelectCallback() {
                    @Override
                    public void selectTitle(int pos) {
                        switch (pos) {
                            case 0:
                                recipeCategoryListener.gotoRecipeCategories();
                                break;
                            case 1:
                                recipeCategoryListener.gotoRecipeListUnCategorized();
                                break;
                        }
                    }
                }, titleMethods)
                .build();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeCategoryPresenter.setView(this);
        // todo: recover saved values from orientation change
        if (savedInstanceState != null) {
            // pull saved recipe list from bundled save
            listItems = savedInstanceState.getParcelableArrayList(RecipeCategory.RECIPE_CATEGORY);
            adapter.notifyDataSetChanged();
        } else {
            // otherwise, pull the list from database and display it.
            recipeCategoryPresenter.fetchRecipeCategories();
        }
    }

    /**
     * Initiate the Recycler View to display list of recipes and button clickers.
     */
    private void initLayout() {
        adapter = new RecipeCategoryAdapter(listItems, this);
        RecyclerView rv = binding.recipeList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // on click for adding an item from floating action button
        View view = binding.recipeListBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> onAddRecipeCategoryClicked());
    }

    private void onAddRecipeCategoryClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Write a Recipe Category name");
        final CreatePopupDetailsBinding groceryBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.create_popup_details, null, false);
        builder.setView(groceryBinding.getRoot());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = groceryBinding.editTxtGroceryName.getText().toString();
                if (Grocery.isValidName(name))// todo: move method for checking valid names from Grocery to a general class
                    createRecipeCategory(name);
                else
                    Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    /**
     * Reload the list from the database
     */
    public void resetList() {
        recipeCategoryPresenter.fetchRecipeCategories();
    }

    /**
     * Creates an empty Recipe Category with the given name.
     * @param recipeCategoryName  The name of the new Recipe Category to create
     */
    private void createRecipeCategory(String recipeCategoryName) {
        recipeCategoryPresenter.addRecipeCategory(new RecipeCategory(recipeCategoryName));
    }

    @Override
    public void deleteSelectedItems() {
        recipeCategoryPresenter.deleteRecipeCategories(listItemsChecked);
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    @Override
    public void onCategoryListChanged() {
        if (recipeCategoryListener != null) recipeCategoryListener.onCategoryListChange();
    }

    public interface RecipeCategoryListener extends ItemListener<RecipeCategory> {

        void gotoGroceryList();
        void gotoRecipeListUnCategorized();
        void gotoRecipeCategories();
        void onCategoryListChange();
    }
}
