package com.example.app_mobile.Apis;

import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.Entities.TypeSupermarche;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TypeSupermarcheApi {
    // GET request to fetch all supermarkets
    // GET all supermarkets
    @GET("/supermarches")
    Call<List<TypeSupermarche>> getAllSupermarches();

    // GET a specific supermarket by ID
    @GET("/supermarches/{id}")
    Call<TypeSupermarche> getSupermarcheById(@Path("id") Long id);

    // POST to create a new supermarket
    @POST("/supermarches")
    Call<TypeSupermarche> createSupermarche(@Body TypeSupermarche typeSupermarche);

    // PUT to update a supermarket by ID
    @PUT("/supermarches/{id}")
    Call<TypeSupermarche> updateSupermarche(@Path("id") Long id, @Body TypeSupermarche typeSupermarche);

    // DELETE a supermarket by ID
    @DELETE("/supermarches/{id}")
    Call<Void> deleteSupermarche(@Path("id") Long id);
    @GET("supermarches/{id}/total")
    Call<Double> getTotalBySupermarcheId(@Path("id") Long id);
    // GET tickets by supermarket ID
    @GET("/supermarches/{id}/tickets")
    Call<List<Ticket>> getTicketsBySupermarcheId(@Path("id") Long id); // GET all supermarkets
    @GET("/supermarches/{id}/produits")
    Call<List<Produit>> getProduitsBySupermarcheId(@Path("id") Long id);

}
