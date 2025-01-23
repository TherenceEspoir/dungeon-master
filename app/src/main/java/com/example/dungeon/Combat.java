package com.example.dungeon;

public class Combat {
    private static final int PERTE_PDV_FUITE = 1;
    private static final int PERTE_PDV_DEFAITE = 3;
    private static final int GAIN_PUISSANCE_VICTOIRE = 10;

    public static int calculerResultatCombat(int puissanceJoueur, int puissanceAdversaire) {
        double rand1 = Math.random();
        double rand2 = Math.random();
        return (int)(puissanceJoueur * rand1 - puissanceAdversaire * rand2);
    }

    public static void gererCombat(Joueur joueur, Donjon donjon, int numeroPiece) {
        Adversaire adversaire = donjon.getAdversaire(numeroPiece);
        int resultat = calculerResultatCombat(joueur.getPuissance(), adversaire.getPuissance());

        if (resultat > 0) {  // Victoire
            joueur.gagnerPuissance(GAIN_PUISSANCE_VICTOIRE);
            donjon.setPieceExploree(numeroPiece);
        } else {  // Défaite
            joueur.perdrePdv(PERTE_PDV_DEFAITE);
        }
    }

    public static void gererFuite(Joueur joueur) {
        joueur.perdrePdv(PERTE_PDV_FUITE);
    }



}
