package com.example.app_mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ProductViewHolder> {
    private Context context;
    private List<Produit> productList;

    // Constructor to initialize the context and product list
    public ProduitAdapter(Context context, List<Produit> productList) {
        this.context = context;
        this.productList = productList;
    }
    // Add a new product to the list
    public void addProduct(Produit produit) {
        this.productList.add(produit);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual product item
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the current product
        Produit produit = productList.get(position);

        // Set product name and price to TextViews
        holder.productNameTextView.setText(produit.getNom());
        holder.productPriceTextView.setText(String.format(" %.2f", produit.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class for the RecyclerView
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productPriceTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.nomTextView);
            productPriceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}