package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.nutrition;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeEditNutritionBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class OnlineRecipeEditNutritionFragment extends Fragment implements OnlineRecipeNutritionContract.NutritionAdapterView{

    private FragmentOnlineRecipeEditNutritionBinding binding;
    private OnlineRecipeNutritionContract.NutritionListener listener;
    private OnlineRecipeEditNutritionAdapter adapter;
    private CustomToolbar customToolbar;

    public OnlineRecipeEditNutritionFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnlineRecipeNutritionContract.NutritionListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_edit_nutrition, container, false);
        adapter = new OnlineRecipeEditNutritionAdapter(listener.getRecipe().getNutritionList(), this);
        binding.recipeNutritionList.setAdapter(adapter);
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getResources().getString(R.string.nutrition_title), getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(l -> {
            getActivity().onBackPressed();
        });
    }

    @Override
    public void onNutritionTypeSelected(TextView view, Nutrition nutrition) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MeasurementType.createMeasurementTypeMenu(popupMenu);
        popupMenu.setOnMenuItemClickListener(item -> {
            String type = item.getTitle().toString();
            nutrition.setMeasurementId(Nutrition.getMeasurementId(type));
            view.setText(type);
            return true;
        });
        popupMenu.show();
    }
}