package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
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
import com.habbybolan.groceryplanner.databinding.FragmentGroceryListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.GroceryListModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Fragment for displaying the list of Grocery object names. Can edit the names of the lists, or enter
 * the list to see the Grocery contents.
 */
public class GroceryListFragment extends NonCategoryListFragment<Grocery> {

    @Inject
    GroceryListPresenter groceryListPresenter;

    private GroceryListListener groceryListListener;
    private FragmentGroceryListBinding binding;
    private CustomToolbar customToolbar;
    private SortType sortType= new SortType();

    public GroceryListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        groceryListListener = (GroceryListListener) context;
        attachListener(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().groceryListSubComponent(new GroceryListModule()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grocery_list, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_grocery_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        groceryListPresenter.searchGroceryList(search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        groceryListPresenter.createGroceryList();
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .build();
    }

    /**
     * Initiate the Recycler View to display list of groceries and bottom action bar.
     */
    private void initLayout() {
        adapter = new GroceryListAdapter(listItems, this);
        RecyclerView rv = binding.groceryList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // on click for adding an item from floating action button
        View view = binding.groceryListBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> onAddGroceryClicked());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        groceryListPresenter.setView(this);
        if (savedInstanceState != null) {
            // pull saved grocery list from bundled save
            showList(savedInstanceState.getParcelableArrayList(Grocery.GROCERY));
        } else {
            // otherwise, pull the list from database and display it.
            groceryListPresenter.createGroceryList();
        }
    }

    /**
     * Creates an Alert dialogue to create a new Grocery element.
     */
    private void onAddGroceryClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Write a Grocery list name");
        final CreatePopupDetailsBinding groceryBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.create_popup_details, null, false);
        builder.setView(groceryBinding.getRoot());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = groceryBinding.editTxtGroceryName.getText().toString();
                if (Grocery.isValidName(name))
                    createGrocery(name);
                else
                    Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    /**
     * Called when category has changed. Stores the category and loads the recipes inside that category.
     */
    public void resetList() {
        groceryListPresenter.createGroceryList();
    }


    /**
     * Creates an empty Grocery with the given name.
     * @param groceryName  The name of the new Grocery to create
     */
    private void createGrocery(String groceryName) {
        groceryListPresenter.addGrocery(new Grocery(groceryName));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Grocery.GROCERY, (ArrayList<? extends Parcelable>) listItems);
    }

    @Override
    public void deleteSelectedItems() {
        groceryListPresenter.deleteGroceries(listItemsChecked);
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    // Allows communication between GroceryListActivity and GroceryListFragment
    public interface GroceryListListener extends ItemListener<Grocery> {

        void gotoRecipeList();
    }
}
