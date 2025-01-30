package com.example.dungeon.model;

import com.example.dungeon.core.Configuration;

public class Joueur {

    private int puissance = Configuration.PUISSANCE_INITIALE_JOUEUR;
    private int pointsDeVie = Configuration.POINTS_DE_VIE_INITIAUX;

    public Joueur() {
    }

    public int getPuissance() {
        return puissance;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public void perdrePdv(int points) {
        this.pointsDeVie -= points;
        if(this.pointsDeVie < 0 )
        {
            this.pointsDeVie = 0; //Pour empecher les points de vie négatifs
        }
    }

    public void gagnerPuissance(int points) {
        this.puissance += points;
    }

    public void gagnerPointsDeVie(int points) {
        this.pointsDeVie += points;
    }
}