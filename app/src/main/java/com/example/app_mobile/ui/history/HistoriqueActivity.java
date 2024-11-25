package com.example.app_mobile.ui.history;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Adapter.HistoriqueAdapter;
import com.example.app_mobile.Adapter.RCAdapter;
import com.example.app_mobile.Apis.TicketApi;
import com.example.app_mobile.Apis.TypeSupermarcheApi;
import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.Entities.TypeSupermarche;
import com.example.app_mobile.R;
import com.example.app_mobile.retrofit.RetrofitService;
import com.example.app_mobile.ui.home.MainActivity;
import com.example.app_mobile.ui.home.SearchActivity;
import com.example.app_mobile.ui.supermarche.SupermarcheActivity;
import com.example.app_mobile.ui.ticket.TicketsActivity;
import com.example.app_mobile.ui.user.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoriqueActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSupermarche;
    private HistoriqueAdapter supermarcheAdapter;
    private TypeSupermarcheApi supermarcheApi;
    private TicketApi ticketApi;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historique);
        recyclerViewSupermarche = findViewById(R.id.recyclerViewHistorique);
        progressBar = findViewById(R.id.progressBar); // Ensure this matches the ID in your XML

        recyclerViewSupermarche = findViewById(R.id.recyclerViewHistorique);
        recyclerViewSupermarche.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSupermarche.setHasFixedSize(true);
        // Configuration du RecyclerView





         // Ensure you have a ProgressBar in your layout

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();

        supermarcheApi = retrofitService.getRetrofit().create((TypeSupermarcheApi.class));
        ticketApi = retrofitService.getRetrofit().create(TicketApi.class);

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startNewActivity(MainActivity.class);
                return true;
            } else if (itemId == R.id.bottom_search) {
                startNewActivity(SearchActivity.class);
                return true;
            } else if (itemId == R.id.bottom_settings) {
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startNewActivity(ProfileActivity.class);
                return true;
            }
            return false;
        });

        // Load supermarkets and tickets
        loadSupermarches();
    }

    private void loadSupermarches() {
        //progressBar.setVisibility(ProgressBar.VISIBLE);

        supermarcheApi.getAllSupermarches().enqueue(new Callback<List<TypeSupermarche>>() {
            @Override
            public void onResponse(Call<List<TypeSupermarche>> call, Response<List<TypeSupermarche>> response) {
                progressBar.setVisibility(ProgressBar.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<TypeSupermarche> supermarches = response.body();
                    fetchTotalForSupermarches(supermarches);

                } else {
                    Log.e("HistoriqueActivity", "Erreur de réponse lors du chargement des supermarchés: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TypeSupermarche>> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                Log.e("HistoriqueActivity", "Erreur lors de la récupération des supermarchés", t);
            }
        });
    }

    // HistoriqueActivity.java

    private void fetchTotalForSupermarches(List<TypeSupermarche> supermarches) {
        List<Pair<TypeSupermarche, Double>> supermarcheTotalList = new ArrayList<>();

        for (TypeSupermarche supermarche : supermarches) {
            // Récupérer le total pour chaque supermarché
            supermarcheApi.getTotalBySupermarcheId(supermarche.getId()).enqueue(new Callback<Double>() {
                @Override
                public void onResponse(Call<Double> call, Response<Double> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Double totalPrice = response.body();
                        supermarcheTotalList.add(new Pair<>(supermarche, totalPrice));

                        // Vérifier si tous les supermarchés ont été traités
                        if (supermarcheTotalList.size() == supermarches.size()) {
                            // Initialiser l'adaptateur avec la liste des supermarchés et des totaux
                            supermarcheAdapter = new HistoriqueAdapter(HistoriqueActivity.this, supermarcheTotalList, supermarche -> {
                                // Lorsqu'un supermarché est cliqué, récupérer ses produits
                                getProduitsBySupermarche(supermarche.getId());
                            });
                            recyclerViewSupermarche.setAdapter(supermarcheAdapter);
                            Log.d("historiqueActivity", "Types de supermarché chargés avec succès: " + supermarcheTotalList.size() + " types.");

                        }



                    } else {
                        Log.e("HistoriqueActivity", "Erreur de réponse lors du chargement du total pour le supermarché: " + supermarche.getNom());
                    }
                }

                @Override
                public void onFailure(Call<Double> call, Throwable t) {
                    Log.e("HistoriqueActivity", "Erreur lors de la récupération du total pour le supermarché: " + supermarche.getNom(), t);
                }
            });
        }
    }


    // Méthode pour récupérer les produits d'un supermarché via Retrofit
    private void getProduitsBySupermarche(Long supermarcheId) {


        supermarcheApi.getProduitsBySupermarcheId(supermarcheId).enqueue(new Callback<List<Produit>>() {
            @Override
            public void onResponse(Call<List<Produit>> call, Response<List<Produit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Produit> produits = response.body();

                    // Pass the product list to a new activity using an Intent
                    Intent intent = new Intent(HistoriqueActivity.this, HistoriqueDetailsActivity.class);
                    Log.e("produitList",produits.toString());
                    intent.putParcelableArrayListExtra("produits", new ArrayList<>(produits));  // Serialize and pass the products
                    startActivity(intent);
                } else {
                    Toast.makeText(HistoriqueActivity.this, "Échec du chargement des produits: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }




    @Override
            public void onFailure(Call<List<Produit>> call, Throwable t) {
                Log.e("HistoriqueActivity", "Erreur lors de la récupération des produits: " + t.getMessage());
            }
        });
    }

    // Méthode pour afficher les produits dans un dialogue



    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
