package com.example.app_mobile.Adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Entities.TypeSupermarche;
import com.example.app_mobile.R;

import java.util.List;
import java.util.Locale;

public class HistoriqueAdapter  extends RecyclerView.Adapter<HistoriqueAdapter.ViewHolder> {

    private List<Pair<TypeSupermarche, Double>> supermarcheTotalList;
    private Context context;
    private OnSupermarcheClickListener onSupermarcheClickListener;

    // Interface for click handling
    public interface OnSupermarcheClickListener {
        void onSupermarcheClick(TypeSupermarche supermarche); // Pass the supermarket when clicked
    }

    public HistoriqueAdapter(Context context, List<Pair<TypeSupermarche, Double>> supermarcheTotalList, OnSupermarcheClickListener listener) {
        this.context = context;
        this.supermarcheTotalList = supermarcheTotalList;
        this.onSupermarcheClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historique_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<TypeSupermarche, Double> item = supermarcheTotalList.get(position);
        TypeSupermarche supermarche = item.first;
        double totalPrice = item.second;

        // Set the supermarket name and total price in the view
        holder.supermarcheName.setText(supermarche.getNom());
        holder.totalPrice.setText(String.format(Locale.getDefault(), "%.2f", totalPrice)); // Display total price formatted as double
        // Handle item click
        holder.itemView.setOnClickListener(v -> onSupermarcheClickListener.onSupermarcheClick(supermarche));
    }
    @Override
    public int getItemCount() {
        return supermarcheTotalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView supermarcheName;
        public TextView totalPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            supermarcheName = itemView.findViewById(R.id.nameSupermarcheTextView); // Assuming you have a TextView with this id
            totalPrice = itemView.findViewById(R.id.priceSupermarcheTextView); // Assuming you have a TextView with this id for total price
        }
    }
}


