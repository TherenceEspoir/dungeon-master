package com.example.dungeon.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dungeon.R;
import com.example.dungeon.core.Configuration;


public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText editPuissanceJoueur = findViewById(R.id.edit_puissance_joueur);
        EditText editPdvJoueur = findViewById(R.id.edit_pdv_joueur);
        EditText editPuissanceAdversaire = findViewById(R.id.edit_puissance_adversaire);
        Spinner spinnerDifficulte = findViewById(R.id.spinner_difficulte);
        Button btnSave = findViewById(R.id.btn_save_settings);
        
        // Charger les valeurs actuelles depuis Configuration
        editPuissanceJoueur.setText(String.valueOf(Configuration.PUISSANCE_INITIALE_JOUEUR));
        editPdvJoueur.setText(String.valueOf(Configuration.POINTS_DE_VIE_INITIAUX));
        editPuissanceAdversaire.setText(String.valueOf(Configuration.PUISSANCE_MAX_ADVERSAIRE));

        btnSave.setOnClickListener(v -> {
            try {
                // Récupérer les nouvelles valeurs saisies par l'utilisateur
                int nouvellePuissance = Integer.parseInt(editPuissanceJoueur.getText().toString());
                int nouveauxPointsDeVie = Integer.parseInt(editPdvJoueur.getText().toString());
                int nouvellePuissanceMaxAdversaire = Integer.parseInt(editPuissanceAdversaire.getText().toString());

                // Mettre à jour la configuration
                Configuration.mettreAJour(nouvellePuissance, nouveauxPointsDeVie, nouvellePuissanceMaxAdversaire);

                // Informer l'utilisateur
                Toast.makeText(this, "Configuration mise à jour !", Toast.LENGTH_SHORT).show();

                // Fermer l'activité
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Veuillez entrer des valeurs valides.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}