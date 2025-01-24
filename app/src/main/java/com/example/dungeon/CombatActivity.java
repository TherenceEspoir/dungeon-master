package com.example.dungeon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class  CombatActivity extends AppCompatActivity {
    private  int pieceId;
    private int puissanceAdversaire;
    private int joueurPuissance;
    private int joueurPDV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_combat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Récupérer les données transmises par MainActivity
        Intent intent = getIntent();
        pieceId = intent.getIntExtra("piece_id", -1);
        puissanceAdversaire = intent.getIntExtra("adversaire_puissance", 0);
        joueurPuissance = intent.getIntExtra("joueur_puissance", 0);
        joueurPDV = intent.getIntExtra("joueur_pdv", 0);

        if (pieceId == -1) {
            Toast.makeText(this, "Erreur : ID de la pièce invalide", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Utiliser les données pour initialiser l'interface
        TextView tvPuissanceAdversaire = findViewById(R.id.tv_adversaire_value);
        TextView tvPuissanceJoueur = findViewById(R.id.tv_puissance_value);
        TextView tvPointsDeVie = findViewById(R.id.tv_points_de_vie_value);

        tvPuissanceAdversaire.setText(String.valueOf(puissanceAdversaire));
        tvPuissanceJoueur.setText(String.valueOf(joueurPuissance));
        tvPointsDeVie.setText(String.valueOf(joueurPDV));

        // Gestion des actions
        Button btnAttaque = findViewById(R.id.btn_attaque);
        Button btnFuite = findViewById(R.id.btn_fuite);

        btnAttaque.setOnClickListener(v->gererAttaque(tvPuissanceJoueur,tvPointsDeVie));
        btnFuite.setOnClickListener(v-> gererFuite(tvPointsDeVie));
    }

    private void gererAttaque(TextView tvPuissanceJoueur, TextView tvPointsDeVie) {
        // Calculer le résultat du combat
        int resultatCombat = Combat.calculerResultatCombat(joueurPuissance, puissanceAdversaire);

        if (resultatCombat > 0) { // Victoire
            joueurPuissance += 10; // Gagner 10 de puissance
            tvPuissanceJoueur.setText(String.valueOf(joueurPuissance));
            Toast.makeText(this, "Victoire !", Toast.LENGTH_SHORT).show();
            retournerResultat("victoire");
        } else { // Défaite
            joueurPDV -= 3; // Perte de 3 points de vie
            tvPointsDeVie.setText(String.valueOf(joueurPDV));

            if (joueurPDV <= 0) {
                retournerResultat("game_over");
            } else {
                Toast.makeText(this, "Défaite ! Points de vie restants : " + joueurPDV, Toast.LENGTH_SHORT).show();
                retournerResultat("défaite");
            }
        }
    }

    private void gererFuite(TextView tvPointsDeVie) {
        joueurPDV -= 1; // Perte d'un point de vie
        tvPointsDeVie.setText(String.valueOf(joueurPDV));

        if (joueurPDV <= 0) {
            retournerResultat("game_over");
        } else {
            Toast.makeText(this, "Fuite ! Points de vie restants : " + joueurPDV, Toast.LENGTH_SHORT).show();
            retournerResultat("fuite");
        }
    }

    private void retournerResultat(String resultatCombat) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("combat_result", resultatCombat);
        resultIntent.putExtra("piece_id", pieceId);
        resultIntent.putExtra("joueur_puissance", joueurPuissance);
        resultIntent.putExtra("joueur_pdv", joueurPDV);
        setResult(RESULT_OK, resultIntent);
        finish(); // Fermer l'activité et retourner à MainActivity
    }

}