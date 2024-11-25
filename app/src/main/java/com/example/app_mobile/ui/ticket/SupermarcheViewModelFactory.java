package com.example.app_mobile.ui.ticket;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_mobile.ui.supermarche.TypeViewModel;

public class SupermarcheViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application application;



    // Constructeur qui prend l'application et le repository
    public SupermarcheViewModelFactory(Application application) {
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
