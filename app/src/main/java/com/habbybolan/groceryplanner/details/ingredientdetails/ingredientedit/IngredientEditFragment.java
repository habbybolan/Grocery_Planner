package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentIngredientEditBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;
import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;
import com.habbybolan.groceryplanner.toolbar.CustomToolbar;

import javax.inject.Inject;

/**
 * Deals with editing an existing Ingredient, or adding a new Ingredient.
 * Ingredient can be apart of an IngredientHolder, or viewed separately through the IngredientListFragment.
 */
public class IngredientEditFragment extends Fragment implements IngredientEditView {

    private FragmentIngredientEditBinding binding;
    private IngredientEditListener ingredientEditListener;

    // Set to the ingredient holder if there is one. NULL otherwise
    private IngredientHolder ingredientHolder;
    private Ingredient ingredient;
    private FoodTypeAdapter adapter;
    private CustomToolbar customToolbar;

    @Inject
    IngredientEditPresenter ingredientEditPresenter;

    public IngredientEditFragment() {}

    /**
     * Create the Fragment with an IngredientHolder
     * @param ingredient    Ingredient to edit inside the IngredientHolder
     * @return              Created fragment
     */
    public static IngredientEditFragment getInstance(IngredientHolder ingredientHolder, Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putParcelable(IngredientHolder.INGREDIENT_HOLDER, ingredientHolder);
        args.putParcelable(Ingredient.INGREDIENT, ingredient);
        IngredientEditFragment fragment = new IngredientEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create the Fragment with no IngredientHolder
     * @param ingredient    Ingredient to edit
     * @return              Created fragment
     */
    public static IngredientEditFragment getInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putParcelable(Ingredient.INGREDIENT, ingredient);
        IngredientEditFragment fragment = new IngredientEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ingredientEditListener = (IngredientEditListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().ingredientEditSubComponent(new IngredientEditModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ingredient_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_edit, container, false);
        setToolbar();
        return binding.getRoot();
    }

    /**
     * @return  True if the ingredient is called from an ingredient Holder.
     */
    private boolean hasIngredientHolder() {
        return ingredientHolder != null;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        ingredientEditPresenter.setView(this);
        if (getArguments() != null) {
            if (getArguments().containsKey(IngredientHolder.INGREDIENT_HOLDER))
                ingredientHolder = getArguments().getParcelable(IngredientHolder.INGREDIENT_HOLDER);
            ingredient = getArguments().getParcelable(Ingredient.INGREDIENT);
            setViews();
            setTextListeners();
            setClickers();
            setFoodTypeGrid();
        }
    }

    /**
     * Sets up the toolbar.
     */
    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder("Ingredient Edit", getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSaveIcon(new CustomToolbar.SaveCallback() {
                    @Override
                    public void saveClicked() {
                        saveChangedToLocal();
                    }
                })
                .addCancelIcon(new CustomToolbar.CancelCallback() {
                    @Override
                    public void cancelClicked() {
                        isViewChanged(false);
                        setViews();
                        adapter.resetSelected(ingredient.getFoodType());
                    }
                })
                .build();
    }

    private void setFoodTypeGrid() {
        FoodType foodType = ingredient == null ? null : ingredient.getFoodType();
        adapter = new FoodTypeAdapter(FoodType.getAllFoodTypes(), foodType != null ? foodType.getType() : FoodType.OTHER, new FoodTypeListener() {
            @Override
            public void onChange() {
                isViewChanged(true);
            }
        });
        adapter.setHasStableIds(true);
        binding.gridFoodTypes.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.gridFoodTypes.setAdapter(adapter);
    }

    /**
     * Initiate the EditTexts with the correct starting values of the ingredient object.
     */
    private void setViews() {
        binding.setName(ingredient.getName());
        if (ingredient.getPrice() != 0) binding.setPrice(String.valueOf(ingredient.getPrice()));
        if (ingredient.getPricePer() != 0)binding.setPricePer(String.valueOf(ingredient.getPricePer()));
        binding.setPriceType(ingredient.getPriceType());
        if (hasIngredientHolder()) {
            if (ingredient.getQuantity() != 0)
                binding.setQuantity(String.valueOf(ingredient.getQuantity()));
            binding.setQuantityType(ingredient.getQuantityType());
        } else {
            hideIngredientHolderViews();
        }
        isViewChanged(false);
    }

    /**
     * Hide the Ingredient Holder specific views.
     */
    private void hideIngredientHolderViews() {
        binding.quantityTag.setVisibility(View.GONE);
        binding.editTxtQuantity.setVisibility(View.GONE);
        binding.quantityTypeTag.setVisibility(View.GONE);
        binding.txtQuantityType.setVisibility(View.GONE);
    }

    /**
     * Set up clicker functionality for price type and quantity type.
     */
    private void setClickers() {
        // popup for selecting price type
        binding.txtPriceType.setOnClickListener(v -> {
            createPriceTypePopup();
        });

        if (hasIngredientHolder()) {
            // popup for selecting quantity type
            binding.txtQuantityType.setOnClickListener(v -> {
                createQuantityTypePopup();
            });
        }
    }

    /**
     * Set the text listeners to store if a change in text happened.
     */
    private void setTextListeners() {
        binding.editTxtName.addTextChangedListener(textWatcher);
        binding.editTxtPrice.addTextChangedListener(textWatcher);
        binding.editTxtPricePer.addTextChangedListener(textWatcher);
        binding.txtPriceType.addTextChangedListener(textWatcher);
        if (hasIngredientHolder()) {
            binding.editTxtQuantity.addTextChangedListener(textWatcher);
            binding.txtQuantityType.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Clicker functionality for selecting a quantity type. Creates an AlertDialogue popup to select
     * from a list of quantity types.
     */
    private void createQuantityTypePopup() {
        setMeasurementType(binding.txtQuantityType);
    }

    /**
     * Clicker functionality for selecting a price type. Creates an AlertDialogue popup to select
     * from a list of price types.
     */
    private void createPriceTypePopup() {
        setMeasurementType(binding.txtPriceType);
    }

    /**
     * Popup for setting the measurement type of the quantity or price.
     * @param v TextView displaying the measurement type
     */
    private void setMeasurementType(TextView v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_measurement_types, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            String type = item.getTitle().toString();
            v.setText(type);
            isViewChanged(true);
            return true;
        });
        popupMenu.show();
    }

    /**
     * If any changed were made, save the changes to the local database by updating/adding the Ingredient.
     */
    private void saveChangedToLocal() {
        // if the Ingredient values were changed, then check if the values were valid
        String name = binding.editTxtName.getText().toString();
        String price = binding.editTxtPrice.getText().toString();
        String pricePer = binding.editTxtPricePer.getText().toString();
        String priceType = binding.txtPriceType.getText().toString();
        String quantity = binding.editTxtQuantity.getText().toString();
        String quantityType = binding.txtQuantityType.getText().toString();

        // if the values were valid, then save the new user created Ingredient
        if (Ingredient.isValidName(name)) {

            if (hasIngredientHolder())
                setEditTextIntoIngredient(name, price, pricePer, priceType, quantity, quantityType, adapter.getSelectedFoodType());
            else
                setEditTextIntoIngredient(name, price, pricePer, priceType, adapter.getSelectedFoodType());
            ingredientEditPresenter.updateIngredient(ingredientHolder, ingredient);
            onEditSaved();
        } else {
            onSavingFailed("Not a valid Ingredient");
        }
    }

    /**
     * Set the values of the saved Ingredient with an ingredient holder.
     * @param name          name for the Ingredient
     * @param price         price for the Ingredient
     * @param pricePer      pricePer  for the Ingredient
     * @param priceType     priceType for the Ingredient
     * @param quantity      quantity for the Ingredient
     * @param quantityType  quantityType for the Ingredient
     */
    private void setEditTextIntoIngredient(String name, String price, String pricePer, String priceType, String quantity, String quantityType, String foodType) {
        ingredient.setName(name);
        if (!price.equals("")) ingredient.setPrice(Integer.parseInt(price));
        else ingredient.setPrice(0);
        if (!pricePer.equals("")) ingredient.setPricePer(Integer.parseInt(pricePer));
        else ingredient.setPricePer(0);
        ingredient.setPriceType(priceType);
        if (!quantity.equals("")) ingredient.setQuantity(Integer.parseInt(quantity));
        else ingredient.setQuantity(0);
        ingredient.setQuantityType(quantityType);
        ingredient.setFoodType(foodType);
    }

    /**
     * Set the values of the saved Ingredient with no ingredient holder.
     * @param name          name for the Ingredient
     * @param price         price for the Ingredient
     * @param pricePer      pricePer  for the Ingredient
     * @param priceType     priceType for the Ingredient
     */
    private void setEditTextIntoIngredient(String name, String price, String pricePer, String priceType, String foodType) {
        ingredient.setName(name);
        if (!price.equals("")) ingredient.setPrice(Integer.parseInt(price));
        else ingredient.setPrice(0);
        if (!pricePer.equals("")) ingredient.setPricePer(Integer.parseInt(pricePer));
        else ingredient.setPricePer(0);
        ingredient.setPriceType(priceType);
        ingredient.setFoodType(foodType);
    }

    /**
     * On changing any of the EditText fields, update that there has been a change.
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // view changed if not the first time loading views
            isViewChanged(true);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // delete the relationship between the IngredientHolder and the Ingredient
    private void deleteIngredient() {
        ingredientEditPresenter.deleteRelationship(ingredientHolder, ingredient);
        ingredientEditPresenter.destroy();
        ingredientEditListener.leaveIngredientEdit();
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Once the Ingredient is saved, then display message, destroy this fragment, and go back to the DetailsFragment.
     */
    private void onEditSaved() {
        Toast.makeText(getContext(), "Changes saved locally", Toast.LENGTH_SHORT).show();
        ingredientEditPresenter.destroy();
        ingredientEditListener.leaveIngredientEdit();
    }

    private void onSavingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // todo: onRestoreInstanceState()
        outState.putParcelable(Ingredient.INGREDIENT, ingredient);
        outState.putParcelable(IngredientHolder.INGREDIENT_HOLDER, ingredientHolder);
    }

    private void isViewChanged(boolean isChanged) {
        binding.setIsChanged(isChanged);
    }

    public interface IngredientEditListener {

        /**
         * Sends screen back to details screen once editing is complete.
         */
        void leaveIngredientEdit();
    }

    public interface FoodTypeListener {

        /**
         * When any value has changed inside the adapter, onChange is called.
         */
        void onChange();
    }
}
