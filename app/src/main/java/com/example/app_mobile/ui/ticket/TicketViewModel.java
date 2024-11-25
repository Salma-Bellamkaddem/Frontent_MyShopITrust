package com.example.app_mobile.ui.ticket;



import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.example.app_mobile.Apis.TypeSupermarcheApi;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketViewModel extends AndroidViewModel {
    private MutableLiveData<List<Ticket>> tickets;
    private TypeSupermarcheApi typeSupermarcheApi;

    public TicketViewModel(Application application) {
        super(application);
        tickets = new MutableLiveData<>();
        // Utilise ton RetrofitService pour créer une instance de ton API
        RetrofitService retrofitService = new RetrofitService();
        typeSupermarcheApi = retrofitService.getRetrofit().create(TypeSupermarcheApi.class);
    }

    public LiveData<List<Ticket>> getTickets() {
        return tickets;
    }

    public void fetchTicketsBySupermarcheId(Long supermarcheId) {
        typeSupermarcheApi.getTicketsBySupermarcheId(supermarcheId).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tickets.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Log.e("TypeViewModel", "Network error: " + t.getMessage());
            }
        });
    }

    // Factory pour créer une instance du ViewModel avec des arguments personnalisés
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Application application;

        public Factory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(TicketViewModel.class)) {
                return (T) new TicketViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}