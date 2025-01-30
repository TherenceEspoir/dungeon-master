package com.example.dungeon.combat;

import com.example.dungeon.model.Adversaire;
import com.example.dungeon.model.Donjon;
import com.example.dungeon.model.EtatPiece;
import com.example.dungeon.model.Joueur;

public class Combat {
    private static final int PERTE_PDV_FUITE = 1;
    private static final int PERTE_PDV_DEFAITE = 3;
    private static final int GAIN_PUISSANCE_VICTOIRE = 10;


    /**
     * Calcule le résultat du combat entre le joueur et l'adversaire.
     * Le résultat est basé sur une formule aléatoire utilisant la puissance des deux combattants.
     *
     * @param puissanceJoueur Puissance actuelle du joueur
     * @param puissanceAdversaire Puissance de l'adversaire
     * @return Un nombre indiquant l'issue du combat (positif = victoire, négatif = défaite)
     */
    public static int calculerResultatCombat(int puissanceJoueur, int puissanceAdversaire) {
        double rand1 = Math.random();
        double rand2 = Math.random();
        return (int)(puissanceJoueur * rand1 - puissanceAdversaire * rand2);
    }


    /**
     * Gère le combat entre le joueur et l'adversaire présent dans la pièce donnée.
     * Met à jour l'état du joueur et de la pièce selon l'issue du combat.
     *
     * @param joueur Le joueur qui combat
     * @param donjon Le donjon contenant la pièce du combat
     * @param numeroPiece L'index de la pièce où a lieu le combat
     */
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


    /**
     * Gère la fuite du joueur face à l'adversaire dans une pièce.
     * Réduit les points de vie du joueur et laisse la pièce en état non terminé.
     *
     * @param joueur Le joueur qui fuit
     * @param donjon Le donjon contenant la pièce
     * @param numeroPiece L'index de la pièce où le joueur fuit
     */
    public static void gererFuite(Joueur joueur,Donjon donjon, int numeroPiece) {
        joueur.perdrePdv(PERTE_PDV_FUITE);
        donjon.setEtatPiece(numeroPiece,EtatPiece.EXPLOREE_NON_TERMINEE);
    }
}
