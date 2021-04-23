package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentIngredientListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.IngredientListModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.toolbar.CustomToolbar;

import javax.inject.Inject;


public class IngredientListFragment extends NonCategoryListFragment<Ingredient> {

    private FragmentIngredientListBinding binding;
    private CustomToolbar customToolbar;
    private IngredientListListener listener;
    private SortType sortType = new SortType();

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
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_ingredient_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        presenter.searchIngredientList(search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        presenter.createIngredientList();
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .build();
    }

    public void reloadList() {
        presenter.createIngredientList();
    }


    @Override
    public void deleteSelectedItems() {
        presenter.deleteIngredients(listItemsChecked);
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    public interface IngredientListListener extends ItemListener<Ingredient> {
        void createNewItem();
    }
}
