package com.example.app_mobile.Entities;

import java.util.List;

public class TypeSupermarche {
    private Long id;
    private String nom;
    private String description;
    private List<Ticket> tickets;

    public Long getId() {
        return id;
    }

    public TypeSupermarche(Long id, String nom) {
        this.id = id;
        this.nom = nom;

    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
