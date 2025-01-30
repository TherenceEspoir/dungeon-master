package com.example.dungeon.model;

import com.example.dungeon.combat.Bonus;
import com.example.dungeon.combat.BonusType;
import com.example.dungeon.core.Configuration;

/**
 * Classe représentant le Donjon dans le jeu.
 * Un donjon est composé de 16 pièces, chacune contenant un adversaire et pouvant contenir un bonus.
 */
public class Donjon {
    private static final String[] NOM_ADVERSAIRES = {
            "Gobelin", "Troll", "Dragon", "Squelette", "Sorcière",
            "Garde maudit", "Banshee", "Minotaure", "Vampire", "Loup-garou",
            "Golem de pierre", "Esprit hanté", "Hydre", "Cyclope", "Fantôme", "Démon"
    };
    private static final int NB_PIECES = 16;
    private Adversaire[] pieces; // Tableau contenant les adversaires dans chaque pièce
    private int nbPiecesNonExplorees;
    private EtatPiece[] etatsPieces; // Tableau stockant l'état de chaque pièce
    private int indexPotionMagique;  // Indice de la pièce contenant la potion magique
    private int indexCharmePuissance;  // Indice de la pièce contenant le charme de puissance


    public Donjon() {
        pieces = new Adversaire[NB_PIECES];
        etatsPieces = new EtatPiece[NB_PIECES];
        nbPiecesNonExplorees = NB_PIECES;

        // Initialisation des adversaires avec puissance aléatoire entre 1 et 150
        for (int i = 0; i < NB_PIECES; i++) {
            int puissance = (int) (Math.random() * Configuration.PUISSANCE_MAX_ADVERSAIRE) + 1;
            String nom = NOM_ADVERSAIRES[i];
            pieces[i] = new Adversaire(puissance,nom);
            etatsPieces[i] =EtatPiece.NON_EXPLOREE;
        }

        placerBonus();
    }

    public EtatPiece getEtatPiece(int piece)
    {
        return etatsPieces[piece];
    }

    public void setEtatPiece(int piece, EtatPiece nouvelEtat) {
        // Décrémenter si l'état courant est NON_EXPLOREE ou EXPLOREE_NON_TERMINEE,
        // et que le nouvel état est EXPLOREE_TERMINEE
        if ((etatsPieces[piece] == EtatPiece.NON_EXPLOREE || etatsPieces[piece] == EtatPiece.EXPLOREE_NON_TERMINEE)
                && nouvelEtat == EtatPiece.EXPLOREE_TERMINEE) {
            nbPiecesNonExplorees--; // Réduire le compteur
        }

        // Mettre à jour l'état de la pièce
        etatsPieces[piece] = nouvelEtat;

    }

    public Adversaire getAdversaire(int piece) {
        return pieces[piece];
    }

    /**
     * Retourne le nombre de pièces encore non explorées.
     *
     * @return Nombre de pièces non explorées.
     */
    public int getNbPiecesNonExplorees() {
        return nbPiecesNonExplorees;
    }

    /**
     * Place aléatoirement les bonus (potion magique et charme de puissance) dans le donjon.
     */
    private void placerBonus() {
        indexPotionMagique = (int) (Math.random() * NB_PIECES);

        do {
            indexCharmePuissance = (int) (Math.random() * NB_PIECES);
        } while (indexCharmePuissance == indexPotionMagique);  // Pour eviter de placer les deux bonus dans la même pièce

        System.out.println("Potion magique placée dans la pièce : " + (indexPotionMagique + 1));
        System.out.println("Charme de puissance placé dans la pièce : " + (indexCharmePuissance + 1));
    }

    /**
     * Vérifie si une pièce contient un bonus.
     *
     * @param numeroPiece Numéro de la pièce.
     * @return `true` si un bonus est présent, sinon `false`.
     */
    public boolean contientBonus(int numeroPiece) {
        return numeroPiece == indexPotionMagique || numeroPiece == indexCharmePuissance;
    }

    public Bonus getBonus(int numeroPiece) {
        if (numeroPiece == indexPotionMagique) {
            return new Bonus(BonusType.POTION_MAGIQUE);
        } else if (numeroPiece == indexCharmePuissance) {
            return new Bonus(BonusType.CHARME_PUISSANCE);
        }
        return null;
    }

    /**
     * Supprime un bonus de la pièce une fois utilisé.
     *
     * @param numeroPiece Numéro de la pièce.
     */
    public void retirerBonus(int numeroPiece) {
        if (numeroPiece == indexPotionMagique) {
            indexPotionMagique = -1;  // Bonus retiré
        } else if (numeroPiece == indexCharmePuissance) {
            indexCharmePuissance = -1;  // Bonus retiré
        }
    }


    /**
     * Vérifie si tous les adversaires du donjon ont été vaincus.
     *
     * @return `true` si tous les adversaires ont été vaincus, sinon `false`.
     */
    public boolean tousLesAdversairesVaincus() {
        for (int i = 0; i < NB_PIECES; i++) {
            if (pieces[i] != null) {
                if (etatsPieces[i] != EtatPiece.EXPLOREE_TERMINEE) {
                    return false; // L'adversaire de cette pièce n'a pas été vaincu
                }
            }
        }
        return true; // Tous les adversaires ont été vaincus
    }

}
