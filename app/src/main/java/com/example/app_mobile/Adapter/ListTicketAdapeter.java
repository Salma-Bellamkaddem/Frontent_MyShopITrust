package com.example.app_mobile.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.R;
import com.example.app_mobile.ui.ticket.TicketDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListTicketAdapeter extends RecyclerView.Adapter<ListTicketAdapeter.TicketViewHolder> {

    private List<Ticket> ticketList;
    private OnItemClickListener onItemClickListener;

    // Interface pour gérer les clics
    public interface OnItemClickListener {
        void onItemClick(Ticket ticket);
    }

    // Constructor prenant la liste des tickets et le listener en paramètre
    public ListTicketAdapeter(List<Ticket> ticketList, OnItemClickListener onItemClickListener) {
        this.ticketList = ticketList != null ? ticketList : new ArrayList<>(); // Protection contre null
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);

        // Log pour le débogage
        Log.d("TicketAdapter", "Binding ticket: " + ticket.getNomTicket());

        String startDate = ticket.getStartDate();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        String formattedDate = null;
        try {
            if (startDate != null) {
                Date date = inputFormat.parse(startDate);
                if (date != null) {
                    formattedDate = outputFormat.format(date);
                }
            }
        } catch (ParseException e) {
            Log.e("TicketAdapter", "Erreur de parsing de la date", e);
            formattedDate = "Date invalide";
        }

        // Configuration des TextViews
        holder.ticketNameTextView.setText(ticket.getNomTicket());
        holder.ticketDateTextView.setText(formattedDate);

        // Gestion du clic sur l'élément du ticket
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                Intent intent = new Intent(holder.itemView.getContext(), TicketDetailsActivity.class);
                intent.putExtra("ticket", ticket); // Passer l'objet Ticket via Parcelable

                // Récupération de la liste des produits
                List<Produit> produits = ticket.getProduits();

                // Vérification et logging de la liste des produits
                if (produits == null) {
                    Log.e("TicketAdapter", "Erreur: La liste des produits est nulle pour le ticket: " + ticket.getNomTicket());
                } else if (produits.isEmpty()) {
                    Log.w("TicketAdapter", "Avertissement: La liste des produits est vide pour le ticket: " + ticket.getNomTicket());
                } else {
                    Log.d("TicketAdapter", "Nombre de produits associés au ticket '" + ticket.getNomTicket() + "': " + produits.size());
                    Log.d("TicketAdapter", "tostring  produits associés au ticket '" + ticket.getProduits());

                    intent.putParcelableArrayListExtra("produits", new ArrayList<>(produits)); // Utilisez ArrayList pour Parcelable
                }

                // Lancement de l'activité TicketDetailsActivity
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView ticketNameTextView;
        TextView ticketDateTextView;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketNameTextView = itemView.findViewById(R.id.TicketNameTextView);
            ticketDateTextView = itemView.findViewById(R.id.ticketDateTextView);
        }
    }
}
