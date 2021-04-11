package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentIngredientListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.IngredientListModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import javax.inject.Inject;


public class IngredientListFragment extends NonCategoryListFragment<Ingredient> {

    private FragmentIngredientListBinding binding;
    private Toolbar toolbar;
    private IngredientListListener listener;

    @Inject
    IngredientListPresenter presenter;

    public IngredientListFragment() {}

    public static IngredientListFragment newInstance() {
        IngredientListFragment fragment = new IngredientListFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (IngredientListListener) context;
        attachListener(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().ingredientListSubComponent(new IngredientListModule()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_list, container, false);
        setToolbar();
        initRecycler();
        initClickers();
        return binding.getRoot();
    }

    private void initClickers() {
        // on click for adding an item from floating action button
        View view = binding.ingredientListBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> listener.createNewItem());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        presenter.setView(this);
        // display Ingredients directly if saved, otherwise pull list data and display
        if (getArguments() != null) {
            showList(savedInstanceState.getParcelable(Ingredient.INGREDIENT));
        } else {
            presenter.createIngredientList();
        }
    }

    /**
     * initiates the recycler viewer
     */
    private void initRecycler() {
        adapter = new IngredientListAdapter(listItems, this);
        RecyclerView rv = binding.rvIngredientList;
        rv.setAdapter(adapter);
    }

    /**
     * Set up the toolbar
     */
    private void setToolbar() {
        toolbar = binding.toolbarIngredientList.toolbar;
        toolbar.inflateMenu(R.menu.menu_ingredient_holder_list);
        toolbar.setTitle(getString(R.string.title_grocery_list));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        return true;
                    case R.id.action_sort:
                        showSortPopup(getActivity().findViewById(R.id.action_sort));
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

    public void reloadList() {
        presenter.createIngredientList();
    }


    @Override
    public void deleteSelectedItems() {
        presenter.deleteIngredients(listItemsChecked);
    }

    public interface IngredientListListener extends ItemListener<Ingredient> {
        void createNewItem();
    }
}
