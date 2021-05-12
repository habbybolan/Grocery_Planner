package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentGroceryDetailBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 */
public class GroceryIngredientsFragment extends NonCategoryListFragment<GroceryIngredient> implements GroceryIngredientsView {

    private FragmentGroceryDetailBinding binding;
    private GroceryIngredientsListener groceryIngredientsListener;
    private Grocery grocery;
    private CustomToolbar customToolbar;

    @Inject
    GroceryIngredientsPresenter groceryIngredientsPresenter;

    private GroceryIngredientsFragment() {}

    public static GroceryIngredientsFragment getInstance(@NonNull Grocery grocery) {
        GroceryIngredientsFragment fragment = new GroceryIngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Grocery.GROCERY, grocery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        groceryIngredientsListener = (GroceryIngredientsListener) context;
        attachListener(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().groceryDetailSubComponent(new GroceryDetailModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grocery_detail, container, false);
        initLayout();
        setBottomActionBar();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_grocery_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        groceryIngredientsPresenter.searchIngredients(grocery, search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        // todo:
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteGrocery();
                    }
                }, "Grocery")
                .build();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        groceryIngredientsPresenter.setView(this);
        if (getArguments() != null) {
            grocery = getArguments().getParcelable(Grocery.GROCERY);
            if (getArguments().containsKey(Ingredient.INGREDIENT)) {
                listItems = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
                showList(listItems);
            } else
                // only the grocery is saved, retrieve its associated Ingredients
                groceryIngredientsPresenter.createIngredientList((Grocery) getArguments().getParcelable(Grocery.GROCERY));
        }
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new GroceryIngredientsAdapter(listItems, this, getContext());
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    /**
     * Sets up bottom action bar clickers
     */
    private void setBottomActionBar() {
        // on click for adding an Ingredient from floating action button
        View view = binding.groceryDetailBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> groceryIngredientsListener.createNewItem());

        ImageButton imgBtnAddIngredients = view.findViewById(R.id.btn_add_multiple);
        imgBtnAddIngredients.setOnClickListener(l -> groceryIngredientsListener.gotoAddIngredients());

        ImageButton imgBtnInfoLevel = view.findViewById(R.id.btn_info_level);
        imgBtnInfoLevel.setOnClickListener(l -> alertDialogInfoLevel());
    }

    private void alertDialogInfoLevel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick info level")
                .setItems(GroceryIngredientsAdapter.INFO_VIEW_LIST, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GroceryIngredientsAdapter ingredientsAdapter = (GroceryIngredientsAdapter) adapter;
                        ingredientsAdapter.setInfoView(which);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Delete the Grocery and leave the Fragment
     */
    private void deleteGrocery() {
        groceryIngredientsPresenter.deleteGrocery(grocery);
        groceryIngredientsListener.onGroceryDeleted();
    }

    @Override
    public void deleteSelectedItems() {
        groceryIngredientsPresenter.deleteIngredients(grocery, listItemsChecked);
    }

    public void reloadList() {
        groceryIngredientsPresenter.createIngredientList(grocery);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Ingredient.INGREDIENT, (ArrayList<? extends Parcelable>) listItems);
        outState.putParcelable(Grocery.GROCERY, grocery);
    }

    @Override
    public void onChecklistSelected(GroceryIngredient groceryIngredient) {
        groceryIngredientsPresenter.updateGroceryIngredientSelected(grocery, groceryIngredient);
    }

    @Override
    public SortType getSortType() {
        return null;
    }

    public interface GroceryIngredientsListener extends ItemListener<GroceryIngredient> {

        void createNewItem();
        void onGroceryDeleted();
        void gotoAddIngredients();
    }
}
