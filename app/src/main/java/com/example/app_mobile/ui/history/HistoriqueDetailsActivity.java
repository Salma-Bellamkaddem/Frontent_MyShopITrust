package com.example.app_mobile.ui.history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Adapter.ProduitAdapter;
import com.example.app_mobile.Apis.TypeSupermarcheApi;
import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoriqueDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProduits;
    private ProduitAdapter produitAdapter;
    private ImageView backButton;
    private TypeSupermarcheApi supermarcheApi;

    // Get the products from the intent
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_historique_details); // Create this layout
            recyclerViewProduits = findViewById(R.id.recyclerViewProduit);
            recyclerViewProduits.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewProduits.setHasFixedSize(true);
            backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                // Create an intent to start HistoriqueActivity
                Intent intent = new Intent(HistoriqueDetailsActivity.this, HistoriqueActivity.class);
                // Pass any necessary data to HistoriqueActivity
                //intent.putExtra("ticketId", ticketId); // Uncomment if ticketId is needed

                // Ensure that HistoriqueActivity is brought to the front
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // Start HistoriqueActivity
                startActivity(intent);

                // Close the current activity
                finish();
            });

            //ArrayList<Ticket> ticketList = getIntent().getParcelableArrayListExtra("tickets");

            ArrayList<Produit> produits =  getIntent().getParcelableArrayListExtra("produits");

            if (produits != null) {
                Log.d("produitlist", "produit received: " + produits.toString());

                // Set up the RecyclerView with the product list
                produitAdapter = new ProduitAdapter(this, produits);
                recyclerViewProduits.setAdapter(produitAdapter);
            } else {
                Log.e("DetailsHistoriqueActivity", "No products received");
            }
        }
    }


