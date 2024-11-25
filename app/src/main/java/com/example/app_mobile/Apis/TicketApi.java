package com.example.app_mobile.Apis;

import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.Entities.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TicketApi {

    // Récupérer tous les tickets
    @GET("/tickets")
    Call<List<Ticket>> getAllTickets();

    // Récupérer les détails d'un ticket par ID
    @GET("/tickets/{id}")
    Call<Ticket> getTicketById(@Path("id") Long id);

    // Ajouter un nouveau ticket
    @POST("/tickets")
    Call<Ticket> createTicket(@Body Ticket ticket);

    // Mettre à jour un ticket existant
    @PUT("/tickets/{id}")
    Call<Ticket> updateTicket(@Path("id") Long id, @Body Ticket ticket);

    // Supprimer un ticket par ID
    @DELETE("/tickets/{id}")
    Call<Void> deleteTicket(@Path("id") Long id);

    // Ajouter un produit à un ticket
    @POST("/tickets/{ticketId}/produits")
    Call<Ticket> addProductToTicket(@Path("ticketId") Long ticketId, @Body Produit produit);

        @GET("/tickets/{ticketId}/produits")
        Call<List<Produit>> getProductsByTicketId(@Path("ticketId") Long ticketId);


}

