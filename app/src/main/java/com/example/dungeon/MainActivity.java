/*package com.example.dungeon;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}*/

package com.example.dungeon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;
    private Donjon donjon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Récupérer le GridLayout
        GridLayout gridLayout = findViewById(R.id.grid);

        //Initialisation des objets principaux
        Joueur joueur = new Joueur();
        donjon = new Donjon();
        gameManager = new GameManager(donjon,joueur);

        // Vérifier que le GridLayout n'est pas null
        if (gridLayout == null) {
            Toast.makeText(this, "Erreur : GridLayout introuvable", Toast.LENGTH_SHORT).show();
            return;
        }
        rendreGrille(gridLayout);

    }

    // Charger le menu d'options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id==R.id.action_restart)
        {
            Toast.makeText(this, "Partie redémarrée !", Toast.LENGTH_SHORT).show();
            restartGame();
            return true;
        } else if (id==R.id.action_quit) {
            Toast.makeText(this, "Application quittée !", Toast.LENGTH_SHORT).show();
            finish(); // Ferme l'application
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void restartGame() {
        Toast.makeText(this, "Recommencer la partie (ajouter la logique) !", Toast.LENGTH_SHORT).show();
    }


    private void lancerCombat(int numeroPiece)
    {
        //verifier si la piece est déja explorée
        if(donjon.isPieceExploree(numeroPiece))
        {
            Toast.makeText(this, "Cette pièce est déjà explorée.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Lancer l'activité de combat
        Intent intent = new Intent(MainActivity.this,CombatActivity.class);
        intent.putExtra("piece_id", numeroPiece);
        intent.putExtra("adversaire_puissance", donjon.getAdversaire(numeroPiece).getPuissance());
        intent.putExtra("joueur_puissance", gameManager.getJoueur().getPuissance());
        intent.putExtra("joueur_pdv", gameManager.getJoueur().getPointsDeVie());
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String combatResult = data.getStringExtra("combat_result");
            int pieceId = data.getIntExtra("piece_id", -1);

            if (pieceId != -1) {
                Toast.makeText(this, "Résultat : " + combatResult, Toast.LENGTH_SHORT).show();

                // Mettre à jour l'état du jeu
                gameManager.updateEtatJeu();

                // Rafraîchir la grille
                GridLayout gridLayout = findViewById(R.id.grid);
                //initialiserGrille(gridLayout);
                rendreGrille(gridLayout);
            }
        }
    }


    private void rendreGrille(GridLayout gridLayout) {
        int totalButtons = 16;
        gridLayout.removeAllViews(); // Nettoyer les anciennes vues

        for (int i = 0; i < totalButtons; i++) { // Commencer à 0 pour correspondre à l'index du tableau
            // Créer un nouveau bouton
            Button button = new Button(this);
            button.setBackgroundResource(R.drawable.button_background);
            button.setText(String.format("%02d", i + 1)); // Texte du bouton (01, 02, ...)

            // Vérifier si la pièce est explorée
            if (donjon.isPieceExploree(i)) {
                button.setEnabled(false); // Désactiver le bouton si la pièce est explorée
                button.setAlpha(0.5f); // Réduire l'opacité pour indiquer qu'elle est explorée
            }

            // Ajouter une action pour les clics sur le bouton
            int finalI = i; // Utiliser un index final pour le OnClickListener
            button.setOnClickListener(v -> {
                if (donjon.isPieceExploree(finalI)) {
                    Toast.makeText(this, "Cette pièce est déjà explorée.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bouton " + (finalI + 1) + " cliqué !", Toast.LENGTH_SHORT).show();
                    lancerCombat(finalI); // Lancer le combat pour cette pièce
                }
            });

            // Paramètres de mise en page pour le bouton
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 150;
            params.height = 75;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);

            button.setLayoutParams(params);

            // Ajouter le bouton au GridLayout
            gridLayout.addView(button);
        }
    }

}
