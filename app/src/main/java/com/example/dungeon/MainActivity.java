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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Récupérer le GridLayout
        GridLayout gridLayout = findViewById(R.id.grid);

        // Vérifier que le GridLayout n'est pas null
        if (gridLayout == null) {
            Toast.makeText(this, "Erreur : GridLayout introuvable", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nombre total de boutons à créer
        int totalButtons = 16;

        for (int i = 1; i <= totalButtons; i++) {
            // Créer un nouveau bouton
            Button button = new Button(this);
            button.setBackgroundResource(R.drawable.button_background);
            button.setText(String.format("%02d", i)); // Texte du bouton (01, 02, ...)
            button.setPadding(8, 8, 8, 8); // Ajouter un padding interne

            // Ajouter une action pour les clics sur le bouton
            int buttonNumber = i; // Utiliser une variable finale pour accéder dans le clic listener
            button.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "Bouton " + buttonNumber + " cliqué !", Toast.LENGTH_SHORT).show();
            });

            // Paramètres de mise en page pour le bouton

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width=150;
            params.height=75;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);

            button.setLayoutParams(params);

            // Ajouter le bouton au GridLayout
            gridLayout.addView(button);
        }
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
        // Ajoute ici ta logique pour réinitialiser la partie
        Toast.makeText(this, "Recommencer la partie (ajouter la logique) !", Toast.LENGTH_SHORT).show();
    }
}
