package com.example.app_mobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Apis.TypeSupermarcheApi;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.Entities.TypeSupermarche;
import com.example.app_mobile.R;
import com.example.app_mobile.retrofit.RetrofitService;
import com.example.app_mobile.ui.ticket.TicketsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RCAdapter  extends RecyclerView.Adapter<RCAdapter.RCViewHolder> {
    private Context context;
    private ArrayList<TypeSupermarche> modelArrayList;
    private TypeSupermarcheApi typeSupermarcheApi;
    private OnItemClickListener onItemClickListener;

    public RCAdapter(Context context, ArrayList<TypeSupermarche> modelArrayList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.onItemClickListener = onItemClickListener;
        RetrofitService retrofitService = new RetrofitService();
        this.typeSupermarcheApi = retrofitService.getRetrofit().create(TypeSupermarcheApi.class);
    }
    public interface OnItemClickListener {
        void onItemClick(TypeSupermarche typeSupermarche);
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rc_item, parent, false);
        return new RCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        // Obtenir l'élément actuel de la liste
        TypeSupermarche supermarcheType = modelArrayList.get(position);

        // Configurer les vues du ViewHolder avec les données
        holder.titleTextView.setText(supermarcheType.getNom());

        // Gérer le clic sur un élément de la liste des supermarchés
        holder.itemView.setOnClickListener(v -> {
            // Lorsqu'on clique sur un type, on appelle l'API pour récupérer les tickets associés
            fetchTicketsAndOpenActivity(supermarcheType.getId());
        });
    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    private void fetchTicketsAndOpenActivity(Long supermarcheId) {
        Call<List<Ticket>> call = typeSupermarcheApi.getTicketsBySupermarcheId(supermarcheId);
        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ticket> ticketList = response.body();

                    // Ouvrir une nouvelle activité et passer la liste des tickets
                    Intent intent = new Intent(context, TicketsActivity.class);
                    intent.putParcelableArrayListExtra("tickets", new ArrayList<>(ticketList));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Échec du chargement des tickets: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                // Gérer les erreurs (comme afficher un Toast)
                Toast.makeText(context, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    public static class RCViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public RCViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.rc_title);
        }
    }
}
