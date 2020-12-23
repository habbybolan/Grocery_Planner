package com.habbybolan.groceryplanner.details.ingredientedit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentIngredientEditBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;

import javax.inject.Inject;

public class IngredientEditFragment extends Fragment implements IngredientEditView{

    private FragmentIngredientEditBinding binding;
    private IngredientEditListener ingredientEditListener;
    private boolean isChanged = false;

    private IngredientHolder ingredientHolder;
    private Ingredient ingredient;

    private final String[] amountTypes = {"lb", "g", "ml"};

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
            initLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_delete:
                showDeletePopup();
                return true;
            default:
                return false;
        }
    }

    /**
     * Shows a popup to ask for confirmation on deleting the grocery list.
     */
    private void showDeletePopup() {
        PopupWindow popupWindow = new PopupWindow(getLayoutInflater().inflate(R.layout.popup_delete, null, false));
        popupWindow.showAtLocation(binding.ingredientEditContainer, Gravity.CENTER, 0, 0);
        ImageView imageView = getView().findViewById(R.id.popup_btn_delete);
        imageView.setOnClickListener(v -> deleteIngredient());
    }

    /**
     * Initiate the EditTexts with the correct starting values of the ingredient object.
     * Set the text listeners to store if a change in text happened.
     * Set up clicker functionality for the save button, saving any changes to the database.
     * Set up clicker functionality for price type and quantity type.
     */
    private void initLayout() {
        binding.setName(ingredient.getName());
        binding.setPrice(String.valueOf(ingredient.getPrice()));
        binding.setPricePer(String.valueOf(ingredient.getPricePer()));
        binding.setPriceType(ingredient.getPriceType());
        binding.setQuantity(String.valueOf(ingredient.getQuantity()));
        binding.setQuantityType(ingredient.getQuantityType());

        setTextListeners();

        binding.btnSave.setOnClickListener(v -> saveChangedToLocal());

        // check if the Ingredient being edited is new.
        if (ingredientEditPresenter.isNewIngredient(ingredient)) {
            // if new, then disable the delete button.
            binding.btnDelete.setEnabled(false);
        } else {
            // otherwise, it exists in the database, so allow delete functionality.
            binding.btnDelete.setOnClickListener(v -> deleteIngredient());
        }

        // popup for selecting price type
        binding.txtPriceType.setOnClickListener(v -> createPriceTypeAlertDialogue());

        // popup for selecting quantity type
        binding.txtQuantityType.setOnClickListener(v -> createQuantityTypeAlertDialogue());
    }

    /**
     * Sets up the
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
    private void createQuantityTypeAlertDialogue() {
        // todo: create and set custom AlertDialogue view
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick a type");
        builder.setItems(amountTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.setQuantityType(amountTypes[which]);
            }
        });
        builder.show();
    }

    /**
     * Clicker functionality for selecting a price type. Creates an AlertDialogue popup to select
     * from a list of price types.
     */
    private void createPriceTypeAlertDialogue() {
        // todo: create and set custom AlertDialogue view
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick a type");
        builder.setItems(amountTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.setPriceType(amountTypes[which]);
            }
        });
        builder.show();
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
        if (isChanged) {
            // if the Ingredient values were changed, then check if the values were valid
            if (Ingredient.isValidIngredient(name, price, pricePer, quantity)) {
                setEditTextIntoIngredient(name, price, pricePer, priceType, quantity, quantityType);
                // if the values were valid, then save the new user created Ingredient
                ingredientEditPresenter.updateIngredient(ingredientHolder, ingredient);
                onEditSaved();
            } else {
                onSavingFailed("Not a valid Ingredient");
            }
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
    private void setEditTextIntoIngredient(String name, String price, String pricePer, String priceType, String quantity, String quantityType) {
        ingredient.setName(name);
        ingredient.setPrice(Integer.parseInt(price));
        ingredient.setPricePer(Integer.parseInt(pricePer));
        ingredient.setPriceType(priceType);
        ingredient.setQuantity(Integer.parseInt(quantity));
        ingredient.setQuantityType(quantityType);
    }

    /**
     * On changing any of the EditText fields, update that there has been a change.
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void deleteIngredient() {
        ingredientEditPresenter.deleteIngredient(ingredientHolder, ingredient);
        ingredientEditPresenter.destroy();
        ingredientEditListener.onDoneEditing();
    }

    @Override
    public void editStoreFailure(String message) {
        // todo:
    }

    /**
     * Once the Ingredient is saved, then display message, destroy this fragment, and go back to GroceryDetailsFragment.
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

    public interface IngredientEditListener {

        /**
         * Sends screen back to details screen once editing is complete.
         */
        void onDoneEditing();
    }
}
