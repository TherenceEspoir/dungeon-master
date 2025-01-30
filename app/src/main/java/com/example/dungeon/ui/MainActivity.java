package com.example.dungeon.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dungeon.combat.Bonus;
import com.example.dungeon.combat.BonusType;
import com.example.dungeon.R;
import com.example.dungeon.core.GameManager;
import com.example.dungeon.model.Donjon;
import com.example.dungeon.model.EtatPiece;
import com.example.dungeon.model.Joueur;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;
    private Donjon donjon;
    private final String GAGNER="gagné";
    private final String PERDU="perdu";
    private TextView tvResultatCombat;
    private TextView tvPiecesNonExplorees;

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

        tvPiecesNonExplorees = findViewById(R.id.tv_pieces_non_explorees_label);
        tvPiecesNonExplorees.setText("Pièces non explorées:");


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
            restartGame();
            return true;
        } else if (id==R.id.action_quit) {
            Toast.makeText(this, "Application quittée !", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void restartGame() {

        GameManager.resetInstance();
        gameManager = GameManager.getInstance(); // Obtenir Une nouvelle instance du gameManager
        donjon = gameManager.getDonjon();
        // Réinitialiser la grille
        GridLayout gridLayout = findViewById(R.id.grid);
        rendreGrille(gridLayout);

        tvPiecesNonExplorees.setText("Pièces non explorées : ");
        tvResultatCombat.setText("En attente");

        Toast.makeText(this, "Nouvelle partie démarrée !", Toast.LENGTH_SHORT).show();
    }


    private void lancerCombat(int numeroPiece) {
        // Vérifier si la pièce est déjà explorée
        if (!gameManager.peutJouerPiece(numeroPiece)) {
            Toast.makeText(this, "Cette pièce est déjà explorée.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifier et appliquer le bonus avant le combat
        if (donjon.contientBonus(numeroPiece)) {
            Bonus bonus = donjon.getBonus(numeroPiece);
            appliquerBonus(gameManager.getJoueur(), bonus); // Applique le bonus
            donjon.retirerBonus(numeroPiece); // Retire le bonus de la pièce
        }

        // Lancer l'activité de combat
        Intent intent = new Intent(MainActivity.this, CombatActivity.class);
        intent.putExtra("piece_id", numeroPiece);
        intent.putExtra("adversaire_puissance", gameManager.getDonjon().getAdversaire(numeroPiece).getPuissance());
        intent.putExtra("joueur_puissance", gameManager.getJoueur().getPuissance());
        intent.putExtra("joueur_pdv", gameManager.getJoueur().getPointsDeVie());
        intent.putExtra("adversaire_nom", donjon.getAdversaire(numeroPiece).getNom());

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
                    donjon.setEtatPiece(pieceId, EtatPiece.EXPLOREE_TERMINEE);
                    tvResultatCombat.setText("VICTOIRE !!!");
                } else if ("fuite".equals(combatResult)) {
                    donjon.setEtatPiece(pieceId,EtatPiece.EXPLOREE_NON_TERMINEE);
                    tvResultatCombat.setText("Vous avez fuis ...");
                } else if ("defaite".equals(combatResult)) {
                    donjon.setEtatPiece(pieceId,EtatPiece.EXPLOREE_NON_TERMINEE);
                    tvResultatCombat.setText("DEFAITE...");
                }

                // Mettre à jour l'état du jeu
                gameManager.updateEtatJeu();


                //Verifier si toutes les pièces ont été explorées terminés
                if(donjon.tousLesAdversairesVaincus())
                {
                    // Rafraîchir la grille avant de désactiver
                    GridLayout gridLayout = findViewById(R.id.grid);
                    rendreGrille(gridLayout);

                    gridOffwithMessage(GAGNER);
                }
                else if(gameManager.getJoueur().getPointsDeVie()<=0)
                {
                    // Rafraîchir la grille avant de désactiver
                    GridLayout gridLayout = findViewById(R.id.grid);
                    rendreGrille(gridLayout);

                    gridOffwithMessage(PERDU);
                }else {
                    // Rafraîchir la grille
                    GridLayout gridLayout = findViewById(R.id.grid);
                    rendreGrille(gridLayout);
                }
                mettreAJourInfos();
            }
        }

        TextView tvPiecesNonExplorees = findViewById(R.id.tv_pieces_non_explorees_label);
        tvPiecesNonExplorees.setText( String.valueOf(gameManager.getDonjon().getNbPiecesNonExplorees()) + ": ");
    }


    private void rendreGrille(GridLayout gridLayout) {
        int totalButtons = 16;
        gridLayout.removeAllViews(); // Nettoyer les anciennes vues

        for (int i = 0; i < totalButtons; i++) { // Commencer à 0 pour correspondre à l'index du tableau

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundColor(Color.parseColor("#283593"));


            //Choisir une image en fonction de l'état de la pièce
            
            EtatPiece etatPiece = donjon.getEtatPiece(i);
            if(etatPiece == EtatPiece.NON_EXPLOREE)
            {
                imageView.setImageResource(R.drawable.non_explore);
            } else if (etatPiece == EtatPiece.EXPLOREE_TERMINEE) {
                imageView.setImageResource(R.drawable.exploree_terminee);
            } else if (etatPiece == EtatPiece.EXPLOREE_NON_TERMINEE) {
                imageView.setImageResource(R.drawable.exploree_non_terminee);
            }

            // Ajouter une action pour les clics sur le bouton
            int finalI = i; // Utiliser un index final pour le OnClickListener
            imageView.setOnClickListener(v -> {
                if (etatPiece == EtatPiece.NON_EXPLOREE || etatPiece == EtatPiece.EXPLOREE_NON_TERMINEE) {
                    lancerCombat(finalI); // Lancer le combat pour cette pièce
                } else {
                    Toast.makeText(this, "Pièce vide, rien à explorer ici ! ", Toast.LENGTH_SHORT).show();
                }
            });

            // Paramètres de mise en page pour le bouton
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 150;
            params.height = 75;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);

            imageView.setLayoutParams(params);

            // Ajouter le bouton au GridLayout
            gridLayout.addView(imageView);

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


    private void gridOffwithMessage(String msg) {
        // Afficher un message Toast informant l'utilisateur
        Toast.makeText(this, "Vous avez " + msg + " ! Relancez une nouvelle partie via le menu.", Toast.LENGTH_LONG).show();

        // Désactiver toutes les images de la grille
        GridLayout gridLayout = findViewById(R.id.grid);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setEnabled(false); // Désactiver l'ImageView
            imageView.setAlpha(0.5f); // Réduire l'opacité pour indiquer qu'elle est inactive
        }

        // Mettre à jour le message de résultat du combat tvResultatCombat

        if ("gagné".equalsIgnoreCase(msg)) {
            tvResultatCombat.setText("Bravo, vous avez gagné ! Relancez une nouvelle partie.");
        } else if ("perdu".equalsIgnoreCase(msg)) {
            tvResultatCombat.setText("Vous avez perdu ! Relancez une nouvelle partie.");
        }
    }


    private void appliquerBonus(Joueur joueur, Bonus bonus) {
        if (bonus.getType() == BonusType.POTION_MAGIQUE) {
            int pointsDeVieRestitues = bonus.getValeur();
            joueur.gagnerPointsDeVie(pointsDeVieRestitues);
            Toast.makeText(this, "Vous avez trouvé une potion magique ! +" + pointsDeVieRestitues + " points de vie !", Toast.LENGTH_LONG).show();
        } else if (bonus.getType() == BonusType.CHARME_PUISSANCE) {
            int puissanceAjoutee = bonus.getValeur();
            joueur.gagnerPuissance(puissanceAjoutee);
            Toast.makeText(this, "Vous avez trouvé un charme de puissance ! +" + puissanceAjoutee + " de puissance !", Toast.LENGTH_LONG).show();
        }
    }

}
