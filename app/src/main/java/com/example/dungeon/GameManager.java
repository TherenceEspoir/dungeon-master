package com.example.dungeon;

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

    public boolean peutJouerPiece(int numeroPiece) {
        return !donjon.isPieceExploree(numeroPiece) && etatActuel == EtatJeu.EN_COURS;
    }

    public void updateEtatJeu() {
        if (joueur.getPointsDeVie() <= 0) {
            etatActuel = EtatJeu.DEFAITE;
        } else if (donjon.getNbPiecesNonExplorees() == 0) {
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
