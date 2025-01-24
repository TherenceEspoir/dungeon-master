package com.example.dungeon;

public class GameManager {
    private EtatJeu etatActuel;
    private final Donjon donjon;
    private final Joueur joueur;

    public GameManager(Donjon donjon, Joueur joueur) {
        this.donjon = donjon;
        this.joueur = joueur;
        this.etatActuel = EtatJeu.EN_COURS;
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

    public Joueur getJoueur()
    {
        return joueur;
    }
}