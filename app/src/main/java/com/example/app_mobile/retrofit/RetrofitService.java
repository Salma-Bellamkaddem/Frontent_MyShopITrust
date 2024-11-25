package com.example.app_mobile.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        this.initializeRetrofit();
    }

    private void initializeRetrofit() {
        this.retrofit = (new Retrofit.Builder()).baseUrl("http://192.168.1.182:8090").addConverterFactory(GsonConverterFactory.create(new Gson())).build();
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }

}
