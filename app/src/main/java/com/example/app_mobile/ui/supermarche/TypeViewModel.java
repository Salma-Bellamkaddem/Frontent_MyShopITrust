package com.example.app_mobile.ui.supermarche;

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
import com.example.app_mobile.Entities.TypeSupermarche;

import com.example.app_mobile.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeViewModel extends AndroidViewModel {
    private final TypeSupermarcheApi typeSupermarcheApi;
    private final MutableLiveData<List<TypeSupermarche>> typeSupermarcheLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Ticket>> ticketsLiveData = new MutableLiveData<>();

    public TypeViewModel(@NonNull Application application) {
        super(application);

        // Initialiser le service Retrofit pour l'API
        RetrofitService retrofitService = new RetrofitService();
        typeSupermarcheApi = retrofitService.getRetrofit().create(TypeSupermarcheApi.class);

        // Charger les types de supermarché lors de la création du ViewModel
        loadAllTypesSupermarches();
    }

    // Méthode pour obtenir tous les types de supermarché
    public LiveData<List<TypeSupermarche>> getAllTypesSupermarches() {
        return typeSupermarcheLiveData;
    }

    // Méthode pour obtenir les tickets par ID de supermarché
    public LiveData<List<Ticket>> getTicketsByTypeSupermarcheId(Long typeSupermarcheId) {
        fetchTicketsByTypeSupermarcheId(typeSupermarcheId);
        return ticketsLiveData;
    }

    // Charger tous les types de supermarché depuis l'API
    private void loadAllTypesSupermarches() {
        Call<List<TypeSupermarche>> call = typeSupermarcheApi.getAllSupermarches();
        call.enqueue(new Callback<List<TypeSupermarche>>() {
            @Override
            public void onResponse(Call<List<TypeSupermarche>> call, Response<List<TypeSupermarche>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    typeSupermarcheLiveData.setValue(response.body());
                } else {
                    Log.e("TypeViewModel", "Failed to fetch types: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TypeSupermarche>> call, Throwable t) {
                Log.e("TypeViewModel", "Network error: " + t.getMessage());
            }
        });
    }

    // Charger les tickets pour un type de supermarché spécifique
    private void fetchTicketsByTypeSupermarcheId(Long typeSupermarcheId) {
        Call<List<Ticket>> call = typeSupermarcheApi.getTicketsBySupermarcheId(typeSupermarcheId);
        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ticketsLiveData.setValue(response.body());
                } else {
                    Log.e("TypeViewModel", "Failed to fetch tickets: " + response.message());
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
            if (modelClass.isAssignableFrom(TypeViewModel.class)) {
                return (T) new TypeViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}