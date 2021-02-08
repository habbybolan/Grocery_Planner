package com.habbybolan.groceryplanner.listing.grocerylist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.ListAdapter;
import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.GroceryListDetailsBinding;
import com.habbybolan.groceryplanner.models.Grocery;

import java.util.List;

public class GroceryListAdapter extends ListAdapter<GroceryListAdapter.ViewHolder, Grocery> {

    GroceryListAdapter(List<Grocery> groceries, ListViewInterface view) {
        super(view, groceries);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GroceryListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.grocery_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grocery grocery = items.get(position);
        holder.bind(grocery);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final GroceryListDetailsBinding binding;

        ViewHolder(GroceryListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // click listener for clicking on an ingredient
            binding.groceryListContainer.setOnClickListener(v -> {
                setOnCLickItem(binding.groceryCheckBox, getAdapterPosition());
            });

            // click listener for clicking an ingredient's check box
            binding.groceryCheckBox.setOnClickListener(v -> {
                setOnCLickItemCheckBox(binding.groceryCheckBox, getAdapterPosition());
            });

            // on long click, go into select mode
            binding.groceryListContainer.setOnLongClickListener(v -> setOnLongCLickItem(getAdapterPosition()));
        }

        void bind(Grocery grocery) {
            binding.setListName(grocery.getName());
            displayCheckBox(binding.groceryCheckBox);
        }
    }
}
