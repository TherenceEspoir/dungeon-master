package com.example.dungeon;

public class Donjon {
    private static final int NB_PIECES = 16;
    private static final int PUISSANCE_MAX = 150;
    private Adversaire[] pieces;
    private boolean[] piecesExplorees;
    private int nbPiecesNonExplorees;
    private EtatPiece[] etatsPieces;

    public Donjon() {
        pieces = new Adversaire[NB_PIECES];
        etatsPieces = new EtatPiece[NB_PIECES];
        nbPiecesNonExplorees = NB_PIECES;

        // Initialisation des adversaires avec puissance aléatoire entre 1 et 150
        for (int i = 0; i < NB_PIECES; i++) {
            int puissance = (int) (Math.random() * PUISSANCE_MAX) + 1;
            pieces[i] = new Adversaire(puissance);
            etatsPieces[i] =EtatPiece.NON_EXPLOREE;
        }
    }

    public EtatPiece getEtatPiece(int piece)
    {
        return etatsPieces[piece];
    }

    public void setEtatPiece(int piece,EtatPiece etat)
    {
        if(etatsPieces[piece] == EtatPiece.NON_EXPLOREE)
        {
            nbPiecesNonExplorees--; //Réduire le compteur uniquepent si la pièce passe de Non exploré à un autre état
        }
        etatsPieces[piece] = etat; // Mettre à jour l'état de la pièce
    }

    public Adversaire getAdversaire(int piece) {
        return pieces[piece];
    }

    public boolean isPieceExploree(int piece) {
        return piecesExplorees[piece];
    }

    public int getNbPiecesNonExplorees() {
        return nbPiecesNonExplorees;
    }
}
