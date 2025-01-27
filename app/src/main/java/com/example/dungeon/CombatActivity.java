package com.example.dungeon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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

    private GameManager gameManager;


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

        // Obtenir l'instance unique de GameManager
        gameManager = GameManager.getInstance();

        // Récupérer les données transmises par MainActivity
        Intent intent = getIntent();
        pieceId = intent.getIntExtra("piece_id", -1);

        if (pieceId == -1) {
            Toast.makeText(this, "Erreur : ID de la pièce invalide", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialiser les données du joueur et de l'adversaire
        Adversaire adversaire = gameManager.getDonjon().getAdversaire(pieceId);
        Joueur joueur = gameManager.getJoueur();

        // Utiliser les données pour initialiser l'interface
        TextView tvPuissanceAdversaire = findViewById(R.id.tv_adversaire_value);
        TextView tvPuissanceJoueur = findViewById(R.id.tv_puissance_value);
        TextView tvPointsDeVie = findViewById(R.id.tv_points_de_vie_value);
        ImageView ivAdversaire = findViewById(R.id.iv_monstre);
        TextView tvAdversaireNom = findViewById(R.id.tv_adversaire);

        tvPuissanceAdversaire.setText(String.valueOf(adversaire.getPuissance()));
        tvPuissanceJoueur.setText(String.valueOf(joueur.getPuissance()));
        tvPointsDeVie.setText(String.valueOf(joueur.getPointsDeVie()));
        ivAdversaire.setImageResource(R.drawable.monster);
        tvAdversaireNom.setText(adversaire.getNom());

        // Gestion des actions
        Button btnAttaque = findViewById(R.id.btn_attaque);
        Button btnFuite = findViewById(R.id.btn_fuite);

        btnAttaque.setOnClickListener(v -> gererAttaque(joueur, adversaire, tvPuissanceJoueur, tvPointsDeVie));
        btnFuite.setOnClickListener(v -> gererFuite(joueur, tvPointsDeVie));
    }

    private void gererAttaque(Joueur joueur, Adversaire adversaire, TextView tvPuissanceJoueur, TextView tvPointsDeVie) {
        // Utiliser la méthode `gererCombat` de la classe Combat
        Combat.gererCombat(joueur, gameManager.getDonjon(), pieceId);

        // Mettre à jour l'interface en fonction des résultats
        tvPuissanceJoueur.setText(String.valueOf(joueur.getPuissance()));
        tvPointsDeVie.setText(String.valueOf(joueur.getPointsDeVie()));

        // Vérifier l'état de la pièce après le combat
        EtatPiece etatPiece = gameManager.getDonjon().getEtatPiece(pieceId);

        if (etatPiece== EtatPiece.EXPLOREE_TERMINEE) { // Victoire
            Toast.makeText(this, "Victoire !", Toast.LENGTH_SHORT).show();
            retournerResultat("victoire");
        } else if (joueur.getPointsDeVie() <= 0) { // Défaite avec Game Over
            Toast.makeText(this, "Game Over !", Toast.LENGTH_SHORT).show();
            retournerResultat("game_over");
        } else { // Défaite simple
            Toast.makeText(this, "Défaite ! Points de vie restants : " + joueur.getPointsDeVie(), Toast.LENGTH_SHORT).show();
            retournerResultat("defaite");
        }
    }

    private void gererFuite(Joueur joueur, TextView tvPointsDeVie) {
        // Utiliser la méthode `gererFuite` de la classe Combat
        Combat.gererFuite(joueur,gameManager.getDonjon(),pieceId);

        // Mettre à jour les points de vie
        int pointsDevie= Math.max(0,joueur.getPointsDeVie());
        tvPointsDeVie.setText(String.valueOf(pointsDevie));

        if (joueur.getPointsDeVie() <= 0) { // Fuite mais Game Over
            Toast.makeText(this, "Game Over après fuite !", Toast.LENGTH_SHORT).show();
            retournerResultat("game_over");
        } else { // Fuite réussie
            Toast.makeText(this, "Fuite réussie ! Points de vie restants : " + joueur.getPointsDeVie(), Toast.LENGTH_SHORT).show();
            retournerResultat("fuite");
        }
    }

    private void retournerResultat(String resultatCombat) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("combat_result", resultatCombat);
        resultIntent.putExtra("piece_id", pieceId);
        setResult(RESULT_OK, resultIntent);
        finish(); // Fermer l'activité et retourner à MainActivity
    }

    @Override
    public void onBackPressed() {
        GameManager gameManager = GameManager.getInstance();
        Joueur joueur = gameManager.getJoueur();

        Combat.gererFuite(joueur,gameManager.getDonjon(),pieceId);
        // Préparer les données à retourner à MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("combat_result", "fuite");
        resultIntent.putExtra("piece_id", getIntent().getIntExtra("piece_id", -1));
        resultIntent.putExtra("joueur_puissance", joueur.getPuissance());
        resultIntent.putExtra("joueur_pdv", joueur.getPointsDeVie());
        setResult(RESULT_OK, resultIntent);

        // Fermer l'activité
        finish();
    }

}