package com.example.app_mobile.ui.ticket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Adapter.ListTicketAdapeter;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.R;
import com.example.app_mobile.ui.supermarche.SupermarcheActivity;

import java.util.ArrayList;

public class TicketsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView backButton;
    private ListTicketAdapeter ticketAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        Log.d("ticketlist", "Activity created");
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerViewTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton.setOnClickListener(v -> {
            // Retrieve the ticketId from the Intent
            Long ticketId = getIntent().getLongExtra("ticketId", -1);

            if (ticketId != -1) {
                // Create an intent to start SupermarcheActivity
                Intent intent = new Intent(TicketsActivity.this, SupermarcheActivity.class);
                // Pass the ticketId to SupermarcheActivity if necessary
                intent.putExtra("ticketId", ticketId);
                // To ensure that SupermarcheActivity is the one returned to and not recreated
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent); // Start SupermarcheActivity
                finish(); // Close the current activity
            } else {
                // Navigate back to SupermarcheActivity without the ticketId if not valid
                Intent intent = new Intent(TicketsActivity.this, SupermarcheActivity.class);
                // Ensure the previous activity is cleared from the stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent); // Start SupermarcheActivity
                finish(); // Close the current activity
            }
        });

        // Retrieve the list of tickets from the intent
        ArrayList<Ticket> ticketList = getIntent().getParcelableArrayListExtra("tickets");


        if (ticketList != null) {
            Log.d("ticketlist", "Tickets received: " + ticketList.toString());

            // Initialize the adapter with the ticket list and an OnItemClickListener
            ticketAdapter = new ListTicketAdapeter(ticketList, new ListTicketAdapeter.OnItemClickListener() {

                @Override
                public void onItemClick(Ticket ticket) {
                    // Handle the click event, e.g., navigate to the ticket detail activity
                    Intent intent = new Intent(TicketsActivity.this, TicketDetailsActivity.class);
                    intent.putExtra("ticket", ticket);
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(ticketAdapter);
        } else {
            Log.e("ticketlist", "Ticket list is null!");
        }
    }
}