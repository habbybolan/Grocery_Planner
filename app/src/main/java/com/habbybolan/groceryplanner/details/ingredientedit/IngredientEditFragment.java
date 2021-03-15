package com.habbybolan.groceryplanner.details.ingredientedit;

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
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.models.FoodType;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;
import com.habbybolan.groceryplanner.ui.PopupBuilder;

import javax.inject.Inject;

public class IngredientEditFragment extends Fragment implements IngredientEditView{

    private FragmentIngredientEditBinding binding;
    private IngredientEditListener ingredientEditListener;

    private IngredientHolder ingredientHolder;
    private Ingredient ingredient;
    private FoodTypeAdapter adapter;

    @Inject
    IngredientEditPresenter ingredientEditPresenter;

    public IngredientEditFragment() {}

    public static IngredientEditFragment getInstance(IngredientHolder ingredientHolder, Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putParcelable(IngredientHolder.INGREDIENT_HOLDER, ingredientHolder);
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
        ((GroceryApp) getActivity().getApplication()).getAppComponent().groceryDetailSubComponent(new GroceryDetailModule(), new IngredientEditModule()).inject(this);
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        ingredientEditPresenter.setView(this);
        if (getArguments() != null) {
            ingredientHolder = getArguments().getParcelable(IngredientHolder.INGREDIENT_HOLDER);
            ingredient = getArguments().getParcelable(Ingredient.INGREDIENT);
            setViews();
            setTextListeners();
            setClickers();
            setFoodTypeGrid();
        }
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
     * Ask the user to confirm deleting the Ingredient
     */
    private void deleteIngredientCheck() {
        // todo: add Toolbar
        if (ingredientEditPresenter.isNewIngredient(ingredient)) {
            // if there ingredient is new, then leave the fragment and don't save the Ingredient
            ingredientEditListener.onDoneEditing();
        } else {
            // otherwise, the Ingredient exists in the database, ask to delete and leave fragment
            PopupBuilder.createDeleteDialogue(getLayoutInflater(), "Ingredient", binding.ingredientEditContainer, getContext(), new PopupBuilder.DeleteDialogueInterface() {
                @Override
                public void deleteItem() {
                    deleteIngredient();
                }
            });
        }
    }

    /**
     * Initiate the EditTexts with the correct starting values of the ingredient object.
     */
    private void setViews() {
        binding.setName(ingredient.getName());
        if (ingredient.getPrice() != 0) binding.setPrice(String.valueOf(ingredient.getPrice()));
        if (ingredient.getPricePer() != 0)binding.setPricePer(String.valueOf(ingredient.getPricePer()));
        binding.setPriceType(ingredient.getPriceType());
        if (ingredient.getQuantity() != 0)binding.setQuantity(String.valueOf(ingredient.getQuantity()));
        binding.setQuantityType(ingredient.getQuantityType());
        isViewChanged(false);
    }

    /**
     * Set up clicker functionality for the save button, saving any changes to the database.
     * Set up clicker functionality for price type and quantity type.
     */
    private void setClickers() {

        // button for saving the changes/new Ingredient to the database
        binding.btnSave.setOnClickListener(v -> saveChangedToLocal());

        // button for un-doing any changes made, but not saved, to the Ingredient
        binding.btnCancel.setOnClickListener(l -> {
            isViewChanged(false);
            setViews();
            adapter.resetSelected(ingredient.getFoodType());
        });

        // popup for selecting price type
        binding.txtPriceType.setOnClickListener(v -> {
            createPriceTypePopup();
        });

        // popup for selecting quantity type
        binding.txtQuantityType.setOnClickListener(v -> {
            createQuantityTypePopup();
        });
    }

    /**
     * Set the text listeners to store if a change in text happened.
     */
    private void setTextListeners() {
        binding.editTxtName.addTextChangedListener(textWatcher);
        binding.editTxtPrice.addTextChangedListener(textWatcher);
        binding.editTxtPricePer.addTextChangedListener(textWatcher);
        binding.txtPriceType.addTextChangedListener(textWatcher);
        binding.editTxtQuantity.addTextChangedListener(textWatcher);
        binding.txtQuantityType.addTextChangedListener(textWatcher);
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
        String name = binding.editTxtName.getText().toString();
        String price = binding.editTxtPrice.getText().toString();
        String pricePer = binding.editTxtPricePer.getText().toString();
        String priceType = binding.txtPriceType.getText().toString();
        String quantity = binding.editTxtQuantity.getText().toString();
        String quantityType = binding.txtQuantityType.getText().toString();

        // if the Ingredient values were changed, then check if the values were valid
        if (Ingredient.isValidName(name)) {
            // if the values were valid, then save the new user created Ingredient
            setEditTextIntoIngredient(name, price, pricePer, priceType, quantity, quantityType, adapter.getSelectedFoodType());
            ingredientEditPresenter.updateIngredient(ingredientHolder, ingredient);
            onEditSaved();
        } else {
            onSavingFailed("Not a valid Ingredient");
        }
    }

    /**
     * Set the values of the saved Ingredient.
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
        ingredientEditListener.onDoneEditing();
    }

    @Override
    public void editStoreFailure(String message) {
        Toast.makeText(getContext(), "Failure to edit Ingredient", Toast.LENGTH_LONG).show();
    }

    /**
     * Once the Ingredient is saved, then display message, destroy this fragment, and go back to the DetailsFragment.
     */
    public void onEditSaved() {
        Toast.makeText(getContext(), "Changes saved locally", Toast.LENGTH_SHORT).show();
        ingredientEditPresenter.destroy();
        ingredientEditListener.onDoneEditing();
    }

    public void onSavingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        void onDoneEditing();
    }

    public interface FoodTypeListener {

        /**
         * When any value has changed inside the adapter, onChange is called.
         */
        void onChange();
    }
}
