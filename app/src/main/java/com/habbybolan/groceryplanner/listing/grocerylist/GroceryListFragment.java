package com.habbybolan.groceryplanner.listing.grocerylist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.ListFragment;
import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.CreateIngredientHolderDetailsBinding;
import com.habbybolan.groceryplanner.databinding.FragmentGroceryListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.GroceryListModule;
import com.habbybolan.groceryplanner.models.Grocery;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Fragment for displaying the list of Grocery object names. Can edit the names of the lists, or enter
 * the list to see the Grocery contents.
 */
public class GroceryListFragment extends ListFragment<Grocery> implements ListViewInterface<Grocery> {

    @Inject
    GroceryListPresenter groceryListPresenter;

    private GroceryListListener groceryListListener;
    private FragmentGroceryListBinding binding;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grocery_list, container, false);
        initLayout();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ingredient_holder_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_sort:
                showSortPopup(getActivity().findViewById(R.id.action_sort));
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

        // on click for swapping to Recipe List
        ImageButton gotoRecipeButton = view.findViewById(R.id.btn_goto_other_list);
        gotoRecipeButton.setOnClickListener(v -> groceryListListener.gotoRecipeList());
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
        final CreateIngredientHolderDetailsBinding groceryBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.create_ingredient_holder_details, null, false);
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

    // Allows communication between GroceryListActivity and GroceryListFragment
    public interface GroceryListListener extends ItemListener<Grocery> {

        void gotoRecipeList();
    }
}
