package com.example.dungeon.core;

public class Configuration {
    public static int PUISSANCE_INITIALE_JOUEUR = 100;
    public static int POINTS_DE_VIE_INITIAUX = 10;
    public static int PUISSANCE_MAX_ADVERSAIRE = 150;

    public static void mettreAJour(int puissanceJoueur, int pointsDeVie, int puissanceAdversaire) {
        PUISSANCE_INITIALE_JOUEUR = puissanceJoueur;
        POINTS_DE_VIE_INITIAUX = pointsDeVie;
        PUISSANCE_MAX_ADVERSAIRE = puissanceAdversaire;
    }
}
