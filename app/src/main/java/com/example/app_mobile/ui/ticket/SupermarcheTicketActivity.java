package com.example.app_mobile.ui.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Adapter.TicketAdapter;
import com.example.app_mobile.R;

import com.example.app_mobile.ui.supermarche.SupermarcheActivity;
import com.example.app_mobile.ui.supermarche.TypeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SupermarcheTicketActivity extends AppCompatActivity {
//    private FloatingActionButton addTicketButton;
//    private ImageView backButton;
//    private RecyclerView recyclerView;
//    private TicketAdapter ticketAdapter;
//    private TypeViewModel viewModel;
//    private TicketViewModel ticketviewModel;
//    private TextView ticketDateTextView;
//    private List<Produit> produitList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_supermarche_ticket);
//
//        // Initialize ViewModel
//        ticketviewModel = new ViewModelProvider(this).get(TicketViewModel.class);
//        SupermarcheViewModelFactory factory = new SupermarcheViewModelFactory(getApplication());
//        viewModel = new ViewModelProvider(this, factory).get(TypeViewModel.class);
//
//        // Initialize views
//        addTicketButton = findViewById(R.id.addTicketButton);
//        backButton = findViewById(R.id.backButton);
//        recyclerView = findViewById(R.id.recyclerViewTicket);
//        ticketDateTextView = findViewById(R.id.ticketDateTextView);
//
//        // Set LayoutManager for RecyclerView
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Retrieve supermarket ID from intent
//        int supermarcheId = getIntent().getIntExtra("supermarche_id", -1);
//        Log.e("SupermarcheTicketActivity", "Supermarché ID: " + supermarcheId);
//
//        if (supermarcheId != -1) {
//            // Use ViewModel to get tickets by supermarket ID
//            viewModel.getTicketsByTypeSupermarcheId(supermarcheId).observe(this, tickets -> {
//                if (tickets != null && !tickets.isEmpty()) {
//                    Ticket ticket = tickets.get(0); // Get the first ticket
//                    ticketDateTextView.setText(ticket.getFormattedDate());
//                    produitList = ticket.getProduits();
//                    ticketAdapter = new TicketAdapter(this, produitList);
//                    recyclerView.setAdapter(ticketAdapter);
//                } else {
//                    Toast.makeText(SupermarcheTicketActivity.this, "Aucun ticket trouvé pour ce supermarché.", Toast.LENGTH_SHORT).show();
//                    fillTickets(supermarcheId); // Create a new ticket
//                }
//            });
//        } else {
//            Toast.makeText(this, "Erreur: Aucun supermarché sélectionné.", Toast.LENGTH_SHORT).show();
//            redirectToSupermarche();
//        }
//
//        // Add ticket button listener
//        addTicketButton.setOnClickListener(v -> {
//            Intent intent = new Intent(SupermarcheTicketActivity.this, ScanActivity.class);
//            intent.putExtra("supermarche_id", supermarcheId);
//            startActivity(intent);
//        });
//
//        // Back button listener
//        backButton.setOnClickListener(v -> redirectToSupermarche());
//    }
//
//    private void fillTickets(int supermarcheId) {
//        Log.d("SupermarcheTicketActivity", "Début du remplissage des tickets.");
//
//        try {
//            Ticket ticket = new Ticket();
//            ticket.setTypeSupermarcheId(supermarcheId);
//            ticket.setDate(new Date());
//            ticket.setProduits(new ArrayList<>());
//
//            // Insert ticket into the database
//            ticketviewModel.insert(ticket);
//
//            // Log and update UI
//            Log.d("SupermarcheTicketActivity", "Ticket inséré dans la base de données pour le supermarché ID: " + supermarcheId);
//            Toast.makeText(SupermarcheTicketActivity.this, "Nouveau ticket créé.", Toast.LENGTH_SHORT).show();
//
//            // Update the UI directly
//            ticketDateTextView.setText(ticket.getFormattedDate());
//            produitList = ticket.getProduits();
//            ticketAdapter = new TicketAdapter(this, produitList);
//            recyclerView.setAdapter(ticketAdapter);
//
//        } catch (Exception e) {
//            Log.e("SupermarcheTicketActivity", "Erreur lors de la création ou de l'insertion du ticket pour le supermarché ID: " + supermarcheId, e);
//            Toast.makeText(SupermarcheTicketActivity.this, "Erreur lors de la création du ticket.", Toast.LENGTH_SHORT).show();
//        }
//
//        Log.d("SupermarcheTicketActivity", "Fin du remplissage des tickets.");
//    }
//
//
//    // Méthode pour rediriger l'utilisateur vers l'activité Supermarché
//    private void redirectToSupermarche() {
//        Intent intent = new Intent(SupermarcheTicketActivity.this, SupermarcheActivity.class);
//        startActivity(intent);
//        finish(); // Fermer l'activité actuelle pour éviter de revenir en arrière
//    }


}