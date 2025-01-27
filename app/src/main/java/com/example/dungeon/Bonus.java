package com.example.dungeon;

public class Bonus {
    private BonusType type;
    private int valeur;

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
