package com.example.dungeon;

public class Joueur {
    public static int PUISSANCE_INITIALE = 100;
    public static int POINTS_DE_VIE_INITIAUX = 10;

    private int puissance;
    private int pointsDeVie;

    public Joueur() {
        this.puissance = PUISSANCE_INITIALE;
        this.pointsDeVie = POINTS_DE_VIE_INITIAUX;
    }

    public int getPuissance() {
        return puissance;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
    }

    public void perdrePdv(int points) {
        this.pointsDeVie -= points;
    }

    public void gagnerPuissance(int points) {
        this.puissance += points;
    }
}