package com.example.app_mobile.Apis;

import com.example.app_mobile.Entities.Produit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public   interface ProduitApi {

    // Récupérer tous les produits
    @GET("/produits")
    Call<List<Produit>> getAllProduits();

    // Récupérer les détails d'un produit par ID
    @GET("/produits/{id}")
    Call<Produit> getProduitById(@Path("id") Long id);

    // Ajouter un nouveau produit
    @POST("/produits")
    Call<Produit> createProduit(@Body Produit produit);

    // Mettre à jour un produit existant
    @PUT("/produits/{id}")
    Call<Produit> updateProduit(@Path("id") Long id, @Body Produit produit);

    // Supprimer un produit par ID
    @DELETE("/produits/{id}")
    Call<Void> deleteProduit(@Path("id") Long id);
}