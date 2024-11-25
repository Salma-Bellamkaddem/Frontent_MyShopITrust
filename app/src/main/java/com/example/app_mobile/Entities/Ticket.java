package com.example.app_mobile.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.List;

public class Ticket implements Parcelable {
    private Long id;
    private String startDate; // Use String to match the API response format
    private String nomTicket;
    private List<Produit> produits; // Ensure this matches the API response

    public Ticket(Long id, String startDate, String nomTicket, List<Produit> produits) {
        this.id = id;
        this.startDate = startDate;
        this.nomTicket = nomTicket;
        this.produits = produits;
    }

    protected Ticket(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        startDate = in.readString();
        nomTicket = in.readString();
        produits = in.createTypedArrayList(Produit.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(startDate);
        dest.writeString(nomTicket);
        dest.writeTypedList(produits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getNomTicket() {
        return nomTicket;
    }

    public void setNomTicket(String nomTicket) {
        this.nomTicket = nomTicket;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
}
