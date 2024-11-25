package com.example.app_mobile.ui.supermarche;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Apis.TypeSupermarcheApi;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.Entities.TypeSupermarche;
import com.example.app_mobile.R;
import com.example.app_mobile.Adapter.RCAdapter;

import com.example.app_mobile.retrofit.RetrofitService;
import com.example.app_mobile.ui.home.MainActivity;
//import com.example.app_mobile.ui.ticket.TicketViewModel;
import com.example.app_mobile.ui.ticket.TicketsActivity;

import java.util.ArrayList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupermarcheActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView backButton;
    private ArrayList<TypeSupermarche> modelArrayList;
    private RCAdapter rcAdapter;
    private TypeViewModel typeSuperMarcheviewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarche);

        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);

        // Initialisation du ViewModel
        typeSuperMarcheviewModel = new ViewModelProvider(this).get(TypeViewModel.class);

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        modelArrayList = new ArrayList<TypeSupermarche>();

        // Afficher le ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Charger les données des supermarchés avec Retrofit
        RetrofitService retrofitService = new RetrofitService();
        TypeSupermarcheApi typeSupermarcheApi = retrofitService.getRetrofit().create(TypeSupermarcheApi.class);
        Call<List<TypeSupermarche>> call = typeSupermarcheApi.getAllSupermarches();
        call.enqueue(new Callback<List<TypeSupermarche>>() {
            @Override
            public void onResponse(Call<List<TypeSupermarche>> call, Response<List<TypeSupermarche>> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    List<TypeSupermarche> types = response.body();
                    modelArrayList.clear();
                    modelArrayList.addAll(types);
                    rcAdapter = new RCAdapter(SupermarcheActivity.this, modelArrayList, typeSupermarche -> {
                        // Lorsqu'un élément est cliqué, lancer l'activité des tickets
                        fetchTicketsAndOpenActivity(typeSupermarche.getId());
                    });

                    recyclerView.setAdapter(rcAdapter);
                    Log.d("SupermarcheActivity", "Types de supermarché chargés avec succès: " + types.size() + " types.");

                    // Optionnel : loguer la réponse JSON pour débogage
                    try {
                        Log.d("JSON Response", types.toString());
                    } catch (Exception e) {
                        Log.e("JSON Response", "Erreur lors de l'enregistrement de la réponse JSON", e);
                    }
                } else {
                    Log.e("SupermarcheActivity", "Échec du chargement des types de supermarché: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TypeSupermarche>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("SupermarcheActivity", "Erreur réseau: " + t.getMessage());
            }
        });

        // Gérer le bouton de retour
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SupermarcheActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void fetchTicketsAndOpenActivity(Long supermarcheId) {
        // Implémentez ici la logique pour récupérer les tickets et démarrer l'activité des tickets
        RetrofitService retrofitService = new RetrofitService();
        TypeSupermarcheApi typeSupermarcheApi = retrofitService.getRetrofit().create(TypeSupermarcheApi.class);
        Call<List<Ticket>> call = typeSupermarcheApi.getTicketsBySupermarcheId(supermarcheId);
        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ticket> ticketList = response.body();
                    Intent intent = new Intent(SupermarcheActivity.this, TicketsActivity.class);
                    Log.e("ticketlist", ticketList.toString());

                    intent.putParcelableArrayListExtra("tickets", new ArrayList<>(ticketList));
                    startActivity(intent);
                } else {
                    Toast.makeText(SupermarcheActivity.this, "Échec du chargement des tickets: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toast.makeText(SupermarcheActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
