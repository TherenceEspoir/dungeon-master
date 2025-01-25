package com.example.dungeon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;
    private Donjon donjon;
    private final String GAGNER="gagné";
    private final String PERDU="perdu";
    private TextView tvResultatCombat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvResultatCombat = findViewById(R.id.tv_resultat_message);

        // Récupérer le GridLayout
        GridLayout gridLayout = findViewById(R.id.grid);

        //Obtenir l'instance de GameManager
        gameManager = GameManager.getInstance();
        donjon = gameManager.getDonjon();

        TextView tvPiecesNonExplorees = findViewById(R.id.tv_pieces_non_explorees_label);
        tvPiecesNonExplorees.setText("Pièces non explorées");


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
        gameManager = GameManager.getInstance(); // L'instance peut être recréée ici si besoin
        Toast.makeText(this, "Recommencer la partie (ajouter la logique) !", Toast.LENGTH_SHORT).show();
        // Réinitialiser la grille
        GridLayout gridLayout = findViewById(R.id.grid);
        rendreGrille(gridLayout);
    }


    private void lancerCombat(int numeroPiece) {
        // Vérifier si la pièce est déjà explorée
        if (!gameManager.peutJouerPiece(numeroPiece)) {
            Toast.makeText(this, "Cette pièce est déjà explorée.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lancer l'activité de combat
        Intent intent = new Intent(MainActivity.this, CombatActivity.class);
        intent.putExtra("piece_id", numeroPiece);
        intent.putExtra("adversaire_puissance", gameManager.getDonjon().getAdversaire(numeroPiece).getPuissance());
        intent.putExtra("joueur_puissance", gameManager.getJoueur().getPuissance());
        intent.putExtra("joueur_pdv", gameManager.getJoueur().getPointsDeVie());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String combatResult = data.getStringExtra("combat_result");
            int pieceId = data.getIntExtra("piece_id", -1);

            if (pieceId != -1) {

                if ("victoire".equals(combatResult))
                {
                    donjon.setPieceExploree(pieceId);
                    tvResultatCombat.setText("VICTOIRE !!!");
                } else if ("fuite".equals(combatResult)) {
                    tvResultatCombat.setText("Vous avez fuis ...");
                } else if ("defaite".equals(combatResult)) {
                    tvResultatCombat.setText("DEFAITE...");
                }

                Toast.makeText(this, "Résultat : " + combatResult, Toast.LENGTH_SHORT).show();

                //Verifier si toutes les pièces ont été explorées
                if(donjon.getNbPiecesNonExplorees()==0)
                {
                    afficherMessage(GAGNER);
                    tvResultatCombat.setText("Bravo, vous avez gagné ! Recommencez la partie ");
                }
                else if(gameManager.getJoueur().getPointsDeVie()<=0)
                {
                    afficherMessage(PERDU);
                    tvResultatCombat.setText("Vous avez perdu ! Recommencez la partie ");
                }else {
                    // Mettre à jour l'état du jeu
                    gameManager.updateEtatJeu();
                    // Rafraîchir la grille
                    GridLayout gridLayout = findViewById(R.id.grid);
                    rendreGrille(gridLayout);
                }
            }
        }

        TextView tvPiecesNonExplorees = findViewById(R.id.tv_pieces_non_explorees_label);
        tvPiecesNonExplorees.setText( String.valueOf(gameManager.getDonjon().getNbPiecesNonExplorees()) + ": ");
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
                button.setEnabled(true);
                button.setAlpha(0.5f); // Réduire l'opacité pour indiquer qu'elle est explorée
                button.setText("X");
            }

            // Ajouter une action pour les clics sur le bouton
            int finalI = i; // Utiliser un index final pour le OnClickListener
            button.setOnClickListener(v -> {
                if (donjon.isPieceExploree(finalI)) {
                    Toast.makeText(this, "Pièce vide, rien à explorer ici ! ", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "Bouton " + (finalI + 1) + " cliqué !", Toast.LENGTH_SHORT).show();
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

            mettreAJourInfos();
        }
    }

    private void mettreAJourInfos() {
        // Mettre à jour la puissance du joueur
        TextView tvPuissance = findViewById(R.id.tv_puissance_value);
        tvPuissance.setText(String.valueOf(gameManager.getJoueur().getPuissance()));

        // Mettre à jour les points de vie du joueur
        TextView tvPointsDeVie = findViewById(R.id.tv_points_de_vie_value);
        tvPointsDeVie.setText(String.valueOf(gameManager.getJoueur().getPointsDeVie()));
    }


    private void afficherMessage(String msg) {
        Toast.makeText(this, "Vous avez " +msg+ " ! Relancez une nouvelle partie via le menu.", Toast.LENGTH_LONG).show();

        // Désactiver tous les boutons de la grille
        GridLayout gridLayout = findViewById(R.id.grid);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setEnabled(false); // Désactiver les boutons
            button.setAlpha(0.5f); // Réduire l'opacité
        }
    }

}
