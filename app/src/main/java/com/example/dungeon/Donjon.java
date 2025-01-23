package com.example.dungeon;

public class Donjon {
    private static final int NB_PIECES = 16;
    private static final int PUISSANCE_MAX = 150;
    private Adversaire[] pieces;
    private boolean[] piecesExplorees;
    private int nbPiecesNonExplorees;

    public Donjon() {
        pieces = new Adversaire[NB_PIECES];
        piecesExplorees = new boolean[NB_PIECES];
        nbPiecesNonExplorees = NB_PIECES;

        // Initialisation des adversaires avec puissance aléatoire entre 1 et 150
        for (int i = 0; i < NB_PIECES; i++) {
            int puissance = (int) (Math.random() * PUISSANCE_MAX) + 1;
            pieces[i] = new Adversaire(puissance);
            piecesExplorees[i] = false;
        }
    }

    public Adversaire getAdversaire(int piece) {
        return pieces[piece];
    }

    public boolean isPieceExploree(int piece) {
        return piecesExplorees[piece];
    }

    public void setPieceExploree(int piece) {
        if (!piecesExplorees[piece]) {
            piecesExplorees[piece] = true;
            nbPiecesNonExplorees--;
        }
    }

    public int getNbPiecesNonExplorees() {
        return nbPiecesNonExplorees;
    }
}
