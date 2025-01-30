package com.example.dungeon.model;

/**
 * Classe représentant un adversaire dans le jeu.
 * Chaque adversaire possède un **nom** et une **puissance** qui influencent les combats.
 */
public class Adversaire {

    private String nom;
    private int puissance;

    /**
     * Constructeur de la classe Adversaire.
     * Initialise l'adversaire avec une puissance et un nom donnés.
     *
     * @param puissance Niveau de puissance de l'adversaire.
     * @param nom Nom de l'adversaire.
     */
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

