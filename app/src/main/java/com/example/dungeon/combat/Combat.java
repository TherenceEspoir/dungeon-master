package com.example.dungeon.combat;

import com.example.dungeon.model.Adversaire;
import com.example.dungeon.model.Donjon;
import com.example.dungeon.model.EtatPiece;
import com.example.dungeon.model.Joueur;

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

        if (resultat >= 0) {  // Victoire
            joueur.gagnerPuissance(GAIN_PUISSANCE_VICTOIRE);
            donjon.setEtatPiece(numeroPiece, EtatPiece.EXPLOREE_TERMINEE);
        } else {  // Défaite
            joueur.perdrePdv(PERTE_PDV_DEFAITE);
            donjon.setEtatPiece(numeroPiece,EtatPiece.EXPLOREE_NON_TERMINEE);
        }
    }

    public static void gererFuite(Joueur joueur,Donjon donjon, int numeroPiece) {
        joueur.perdrePdv(PERTE_PDV_FUITE);
        donjon.setEtatPiece(numeroPiece,EtatPiece.EXPLOREE_NON_TERMINEE);
    }
}
