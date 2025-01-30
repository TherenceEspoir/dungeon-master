package com.example.dungeon.model;

public class Adversaire {

    private String nom;
    private int puissance;

    public Adversaire(int puissance,String nom) {
        this.puissance = puissance;
        this.nom = nom;
    }

    public int getPuissance() {
        return puissance;
    }

    public String getNom()
    {
        return nom;
    }
}

