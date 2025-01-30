package com.example.dungeon.core;

import com.example.dungeon.model.Donjon;
import com.example.dungeon.model.EtatJeu;
import com.example.dungeon.model.EtatPiece;
import com.example.dungeon.model.Joueur;

public class GameManager {
    private static GameManager instance;

    private EtatJeu etatActuel;
    private final Donjon donjon;
    private final Joueur joueur;

    // Constructeur privé pour éviter l'instanciation directe
    private GameManager() {
        this.donjon = new Donjon();
        this.joueur = new Joueur();
        this.etatActuel = EtatJeu.EN_COURS;
    }

    // Méthode pour obtenir l'instance unique
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // Méthode pour réinitialiser l'instance
    public static void resetInstance() {
        instance = new GameManager(); // Crée une nouvelle instance
    }

    public boolean peutJouerPiece(int numeroPiece) {
        EtatPiece etat = donjon.getEtatPiece(numeroPiece);
        // Une pièce peut être jouée si elle est NON_EXPLOREE ou EXPLOREE_NON_TERMINEE
        return (etat == EtatPiece.NON_EXPLOREE || etat == EtatPiece.EXPLOREE_NON_TERMINEE) && etatActuel == EtatJeu.EN_COURS;
    }

    /*
    public void updateEtatJeu() {
        if (joueur.getPointsDeVie() <= 0) {
            etatActuel = EtatJeu.DEFAITE;
        } else if (donjon.getNbPiecesNonExplorees() == 0) {
            etatActuel = EtatJeu.VICTOIRE;
        }
    }
    */

    public void updateEtatJeu() {
        if (joueur.getPointsDeVie() <= 0) {
            etatActuel = EtatJeu.DEFAITE;
        } else if (donjon.tousLesAdversairesVaincus()) {
            etatActuel = EtatJeu.VICTOIRE;
        }
    }

    public EtatJeu getEtatJeu() {
        return etatActuel;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public Donjon getDonjon() {
        return donjon;
    }
}
