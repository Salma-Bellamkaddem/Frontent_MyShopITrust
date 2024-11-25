package com.example.app_mobile.ui.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Adapter.ProduitAdapter;
import com.example.app_mobile.Apis.TicketApi;
import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.R;
import com.example.app_mobile.retrofit.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addProduitButton;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private TextView ticketDateTextView;
    private TextView ticketNameTextView;
    private ProduitAdapter produitAdapter;
    private static final int SCAN_REQUEST_CODE = 1;
    // Variable pour stocker le total
    private TextView totalPriceTextView;  // TextView pour afficher le total
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarche_ticket);

        // Initialize views
        addProduitButton = findViewById(R.id.addProduitButton);
        backButton = findViewById(R.id.backButton);
        ticketDateTextView = findViewById(R.id.ticketDateTextView);
        ticketNameTextView = findViewById(R.id.ticketNameTextView);
        recyclerView = findViewById(R.id.recyclerViewTicket);
        totalPriceTextView= findViewById(R.id.totalPrice);
        // Afficher le total initial (qui est 0 au début)
        // Chargement des produits pour un ticket donné
        Long ticketId = getIntent().getLongExtra("ticket_id", -1);
        if (ticketId != -1) {
            loadProductsForTicket(ticketId);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Retrieve ticket from Intent
        Ticket ticket = getIntent().getParcelableExtra("ticket");
        if (ticket != null) {
            Log.e("TicketDetailsActivity", "Ticket received: " + ticket.toString());

            ticketNameTextView.setText(ticket.getNomTicket());

            String formattedDate = formatDate(ticket.getStartDate());
            ticketDateTextView.setText(formattedDate != null ? formattedDate : ticket.getStartDate());

            // Load products associated with this ticket
            loadProductsForTicket(ticket.getId());
        } else {
            Log.e("TicketDetailsActivity", "Ticket is null, cannot load details.");
        }
// Back button listener
        backButton.setOnClickListener(v -> {
            // Vérifier si le ticket est valide
            if (ticket != null) {
                Intent intent = new Intent(TicketDetailsActivity.this, TicketsActivity.class);
                // Passer l'ID du ticket pour l'affichage dans TicketsActivity
                intent.putExtra("ticketId", ticket.getId());
                setResult(RESULT_OK, intent); // Retourner un résultat OK avec l'Intent
                finish(); // Terminer l'activité actuelle
            } else {
                Log.e("TicketDetailsActivity", "Ticket is null, cannot go back with ticket ID.");
            }
        });

        // Set listener for adding product (scanning interface)
        addProduitButton.setOnClickListener(v -> {
            if (ticket != null) {
                Intent intent = new Intent(TicketDetailsActivity.this, ScanActivity.class);
                intent.putExtra("ticketId", ticket.getId());
                Log.e("TicketDetailsActivity","addProduitbutton "+ticket.getId());// Passer l'ID du ticket
                startActivityForResult(intent, SCAN_REQUEST_CODE);  // Demander un résultat après le scan
            } else {
                Log.e("TicketDetailsActivity", "Ticket is null, cannot add product.");
            }
        });

    }
    private void updateTotalPriceDisplay(List<Produit> productList) {
        // Initialiser la variable totalPrice
        double totalPrice = 0.0;

        // Parcourir la liste des produits pour calculer la somme des prix
        for (Produit product : productList) {
            totalPrice += product.getPrice(); // Ajouter le prix du produit au total
        }

        // Mettre à jour le TextView avec le total formaté
        totalPriceTextView.setText(String.format("Total: %.2f MAD", totalPrice));
    }

    // Fetch products associated with the ticket
    private void loadProductsForTicket(Long ticketId) {
        RetrofitService retrofitService = new RetrofitService();
        TicketApi ticketApi = retrofitService.getRetrofit().create(TicketApi.class);
        Call<List<Produit>> call = ticketApi.getProductsByTicketId(ticketId);

        call.enqueue(new Callback<List<Produit>>() {
            @Override
            public void onResponse(Call<List<Produit>> call, Response<List<Produit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Produit> produits = response.body();
                    produitAdapter = new ProduitAdapter(TicketDetailsActivity.this, produits);
                    recyclerView.setAdapter(produitAdapter);
                    Log.d("TicketDetailsActivity", "Produit list loaded successfully, size: " + produits.size());

                    // Calculer et afficher le total des prix
                    updateTotalPriceDisplay(produits); // Appeler la méthode pour calculer le total

                } else {
                    Log.w("TicketDetailsActivity", "Failed to fetch products or no products found.");
                }
            }

            @Override
            public void onFailure(Call<List<Produit>> call, Throwable t) {
                Log.e("TicketDetailsActivity", "Failed to fetch products", t);
            }
        });
    }

    // Date formatting method
    private String formatDate(String startDate) {
        if (startDate == null) return null;

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(startDate);
            return date != null ? outputFormat.format(date) : startDate;
        } catch (ParseException e) {
            Log.e("TicketDetailsActivity", "Error parsing or formatting date", e);
            return startDate;
        }
    }

    // Handle the result of ScanActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Produit addedProduct = data.getParcelableExtra("addedProduct");
            if (addedProduct != null) {
                // Add the new product to the RecyclerView
                produitAdapter.addProduct(addedProduct);
                produitAdapter.notifyDataSetChanged();
                Log.d("TicketDetailsActivity", "Product added and RecyclerView updated");
            }
        }
    }
}
