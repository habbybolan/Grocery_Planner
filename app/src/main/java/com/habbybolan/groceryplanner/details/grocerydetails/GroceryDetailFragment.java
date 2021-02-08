package com.habbybolan.groceryplanner.details.grocerydetails;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.ListFragment;
import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentGroceryDetailBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.ui.CreatePopupWindow;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 */
public class GroceryDetailFragment extends ListFragment<Ingredient> implements ListViewInterface<Ingredient> {

    private FragmentGroceryDetailBinding binding;
    private GroceryDetailAdapter adapter;
    private GroceryDetailsListener groceryDetailsListener;
    private Grocery grocery;

    @Inject
    GroceryDetailsPresenter groceryDetailsPresenter;

    private GroceryDetailFragment() {}

    public static GroceryDetailFragment getInstance(@NonNull Grocery grocery) {
        Bundle args = new Bundle();
        args.putParcelable(Grocery.GROCERY, grocery);
        GroceryDetailFragment fragment = new GroceryDetailFragment();
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
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ingredient_holder_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
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
        groceryDetailsPresenter.setView(this);
        if (getArguments() != null) {
            grocery = getArguments().getParcelable(Grocery.GROCERY);
            if (getArguments().containsKey(Ingredient.INGREDIENT)) {
                listItems = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
            } else
                // only the grocery saved, must retrieve its associated Ingredients
                groceryDetailsPresenter.createIngredientList((Grocery) getArguments().getParcelable(Grocery.GROCERY));
        }
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new GroceryDetailAdapter(listItems, this);
        attachAdapter(adapter);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // button clicker to add a new ingredient to the grocery
        binding.btnAddIngredient.setOnClickListener(v ->
                groceryDetailsListener.createNewItem());
    }

    /**
     * Delete the grocery from database and go back to the Grocery List
     */
    private void deleteGrocery() {
        PopupWindow popupWindow = new PopupWindow();
        View clickableView = CreatePopupWindow.createPopupDeleteCheck(binding.groceryDetailsContainer, "grocery", popupWindow);
        clickableView.setOnClickListener(v -> {
            groceryDetailsPresenter.deleteGrocery(grocery);
            groceryDetailsListener.onGroceryDeleted();
            popupWindow.dismiss();
        });
    }

    /*@Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemCheckBoxSelected(Ingredient ingredient) {
        ingredientsChecked.add(ingredient);
    }

    @Override
    public void onItemCheckBoxUnSelected(Ingredient ingredient) {
        ingredientsChecked.remove(ingredient);
        if (ingredientsChecked.size() == 0) exitSelectedMode();
    }

    @Override
    public void enterSelectMode() {
        groceryDetailsListener.hideToolbar();
        createActionMode();
    }

    @Override
    public void exitSelectedMode() {
        destroyActionMode();
        groceryDetailsListener.showToolbar();
        adapter.exitSelectedMode();
        listItemsChecked.clear();
    }

    @Override
    public void createActionMode() {
        actionMode = getActivity().startActionMode(actionModeCallback);
    }

    @Override
    public void destroyActionMode() {
        if (actionMode != null) actionMode.finish();
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_ingredient_context, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_ingredient_context_delete:
                    Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                    exitSelectedMode();
                    return true;
                case R.id.action_ingredient_context_cancel:
                    exitSelectedMode();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            exitSelectedMode();
        }
    };*/

    public interface GroceryDetailsListener extends ItemListener<Ingredient>{
        void onGroceryDeleted();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Ingredient.INGREDIENT, (ArrayList<? extends Parcelable>) listItems);
        outState.putParcelable(Grocery.GROCERY, grocery);
    }
}
