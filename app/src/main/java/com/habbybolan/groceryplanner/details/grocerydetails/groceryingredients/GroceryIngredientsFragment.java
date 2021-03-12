package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentGroceryDetailBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.ui.CreatePopupWindow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 */
public class GroceryIngredientsFragment extends NonCategoryListFragment<Ingredient> implements ListViewInterface<Ingredient> {

    private FragmentGroceryDetailBinding binding;
    private GroceryDetailsListener groceryDetailsListener;
    private Grocery grocery;
    private Toolbar toolbar;

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
        groceryDetailsListener = (GroceryDetailsListener) context;
        attachListener(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().groceryDetailSubComponent(new GroceryDetailModule(), new IngredientEditModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grocery_detail, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        toolbar = binding.toolbarGroceryDetail.toolbar;
        toolbar.inflateMenu(R.menu.menu_ingredient_holder_details);
        toolbar.setTitle(getString(R.string.title_grocery_ingredients));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        return true;
                    case R.id.action_sort:
                        // todo: what to anchor to? - not search
                        showSortPopup(getActivity().findViewById(R.id.action_search));
                        return true;
                    case R.id.action_delete:
                        deleteGrocery();
                        return true;
                    default:
                        return false;
                }
            }
        });
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        groceryIngredientsPresenter.setView(this);
        if (getArguments() != null) {
            grocery = getArguments().getParcelable(Grocery.GROCERY);
            if (getArguments().containsKey(Ingredient.INGREDIENT)) {
                listItems = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
            } else
                // only the grocery saved, must retrieve its associated Ingredients
                groceryIngredientsPresenter.createIngredientList((Grocery) getArguments().getParcelable(Grocery.GROCERY));
        }
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new GroceryIngredientsAdapter(listItems, this);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // on click for adding an Ingredient from floating action button
        View view = binding.groceryDetailBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> groceryDetailsListener.createNewItem());

        // on Click for entering check list mode
        ImageButton imageButton = view.findViewById(R.id.btn_checklist);
        imageButton.setOnClickListener(l -> groceryDetailsListener.gotoChecklist(groceryIngredientsPresenter.getIngredients()));
    }

    /**
     * Delete the grocery from database and go back to the Grocery List
     */
    private void deleteGrocery() {
        PopupWindow popupWindow = new PopupWindow();
        View clickableView = CreatePopupWindow.createPopupDeleteCheck(binding.groceryDetailsContainer, "grocery", popupWindow);
        clickableView.setOnClickListener(v -> {
            groceryIngredientsPresenter.deleteGrocery(grocery);
            groceryDetailsListener.onGroceryDeleted();
            popupWindow.dismiss();
        });
    }

    @Override
    public void deleteSelectedItems() {
        groceryIngredientsPresenter.deleteIngredients(grocery, listItemsChecked);
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Ingredient.INGREDIENT, (ArrayList<? extends Parcelable>) listItems);
        outState.putParcelable(Grocery.GROCERY, grocery);
    }

    public interface GroceryDetailsListener extends ItemListener<Ingredient> {

        void createNewItem();
        void onGroceryDeleted();
        void gotoChecklist(List<Ingredient> ingredients);
    }
}
