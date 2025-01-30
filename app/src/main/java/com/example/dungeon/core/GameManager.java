package com.example.dungeon.core;

import com.example.dungeon.model.Donjon;
import com.example.dungeon.model.EtatJeu;
import com.example.dungeon.model.EtatPiece;
import com.example.dungeon.model.Joueur;


/**
 * Classe GameManager gérant l'état global du jeu.
 * Implémente le **Pattern Singleton** pour assurer une instance unique.
 */
public class GameManager {
    private static GameManager instance;

    private EtatJeu etatActuel; // État actuel du jeu (EN_COURS, VICTOIRE, DEFAITE)
    private final Donjon donjon; // Instance du donjon contenant les pièces et adversaires
    private final Joueur joueur; // Instance du joueur avec ses stats (PV, puissance)

    /**
     * Constructeur privé empêchant l'instanciation directe.
     * Initialise le donjon, le joueur et met l'état du jeu à EN_COURS.
     */
    private GameManager() {
        this.donjon = new Donjon();
        this.joueur = new Joueur();
        this.etatActuel = EtatJeu.EN_COURS;
    }

    /**
     * Retourne l'instance unique du GameManager (Pattern Singleton).
     * Si aucune instance n'existe, elle est créée.
     *
     * @return Instance unique du GameManager.
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Réinitialise l'instance du GameManager.
     */
    public static void resetInstance() {
        instance = new GameManager(); // Crée une nouvelle instance
    }

    /**
     * Vérifie si une pièce peut être jouée (explorée).
     * Une pièce est jouable si :
     *  - Elle est NON_EXPLOREE (jamais visitée)
     *  - Elle est EXPLOREE_NON_TERMINEE (le joueur a déjà tenté mais n'a pas gagné)
     *
     * @param numeroPiece Numéro de la pièce à vérifier.
     * @return true si la pièce peut être explorée, false sinon.
     */
    public boolean peutJouerPiece(int numeroPiece) {
        EtatPiece etat = donjon.getEtatPiece(numeroPiece);
        // Une pièce peut être jouée si elle est NON_EXPLOREE ou EXPLOREE_NON_TERMINEE
        return (etat == EtatPiece.NON_EXPLOREE || etat == EtatPiece.EXPLOREE_NON_TERMINEE) && etatActuel == EtatJeu.EN_COURS;
    }

    /**
     * Met à jour l'état global du jeu en fonction de la progression du joueur.
     * - Si les points de vie du joueur tombent à 0, il perd la partie.
     * - Si tous les adversaires sont vaincus, le joueur gagne la partie.
     */
    public void updateEtatJeu() {
        if (joueur.getPointsDeVie() <= 0) {
            etatActuel = EtatJeu.DEFAITE;
        } else if (donjon.tousLesAdversairesVaincus()) {
            etatActuel = EtatJeu.VICTOIRE;
        }
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public Donjon getDonjon() {
        return donjon;
    }
}
