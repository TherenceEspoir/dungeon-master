package com.example.dungeon.combat;

/**
 * Représente un bonus que le joueur peut obtenir dans le jeu.
 * Un bonus peut être de deux types :
 * - POTION_MAGIQUE : restaure des points de vie (entre 1 et 3).
 * - CHARME_PUISSANCE : augmente la puissance du joueur (entre 5 et 10).
 */
public class Bonus {
    private BonusType type;  // Type du bonus (Potion magique ou Charme de puissance)
    private int valeur;  // Valeur du bonus (nombre de points de vie ou puissance gagnée)

    public Bonus(BonusType type) {
        this.type = type;
        if (type == BonusType.POTION_MAGIQUE) {
            this.valeur = (int) (Math.random() * 3) + 1; // Entre 1 et 3 points
        } else if (type == BonusType.CHARME_PUISSANCE) {
            this.valeur = (int) (Math.random() * 6) + 5; // Entre 5 et 10 points
        }
    }

    public BonusType getType() {
        return type;
    }

    public int getValeur() {
        return valeur;
    }
}
