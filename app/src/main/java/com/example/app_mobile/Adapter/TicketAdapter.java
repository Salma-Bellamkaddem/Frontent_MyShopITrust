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

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private Context context;
    private List<Produit> produitList;
    TextView productNameTextView, productPriceTextView;
    public TicketAdapter(Context context, List<Produit> produitList) {
        this.context = context;
        this.produitList = produitList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Produit produit = produitList.get(position);
        holder.productNameTextView.setText(produit.getNom());
        holder.productPriceTextView.setText(String.format("%.2f MAD", produit.getPrice()));
    }

    @Override
    public int getItemCount() {
        return produitList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, productPriceTextView;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            //productNameTextView = itemView.findViewById(R.id.productNameTextView);
           // productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
        }
    }
}