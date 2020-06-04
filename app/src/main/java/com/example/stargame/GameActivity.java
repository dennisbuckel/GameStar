package com.example.stargame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    Charakter testCharakter = new Charakter();

    Button fuettern;
    Button schlafen;
    Button saeubern;

    int counter = 0;

    String zustand ="Schlafen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);
        fuettern = (Button) findViewById(R.id.fuettern);
        schlafen = (Button) findViewById(R.id.schlafen);
        saeubern = (Button) findViewById(R.id.saeubern);

        fuettern.setOnClickListener(this);
        schlafen.setOnClickListener(this);
        saeubern.setOnClickListener(this);
        Button muedeButton = (Button) findViewById(R.id.schlafen);
        timer.run();
    }

    /**
     * In timer.run() werden alle Methoden in einem 2 Sekunden Intervall aufgerufen
     */
    private Handler handler = new Handler();
    int zaehler = 0;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            Button muedeButton = (Button) findViewById(R.id.schlafen);
            muedeButton.setText(zustand);

            hp();
            hunger();
            sauber();
            muede();

            abnutzung();
            //Toast.makeText(GameActivity.this, "Zähler " + zaehler, Toast.LENGTH_SHORT).show();
            handler.postDelayed(timer, 1000);
            //zaehler += 1;
      }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fuettern:
                if(zustand == "Aufwäcken") {

                    Toast.makeText(GameActivity.this, "Sie müssen den Stern erst aufwäcken...", Toast.LENGTH_SHORT).show();

                }else{

                    testCharakter.isst();
                    hunger();

                }
                break;
            case R.id.schlafen:
                if(zustand == "Schlafen") {

                    zustand = "Aufwäcken";

                }else{

                    zustand = "Schlafen";

                }
                break;
            case R.id.saeubern:
                if(zustand == "Aufwäcken") {

                    Toast.makeText(GameActivity.this, "Sie müssen den Stern erst aufwäcken...", Toast.LENGTH_SHORT).show();

                }else{

                    testCharakter.wirdSauber();
                    sauber();

                }

                break;
        }
    }
    public void abnutzung(){
        if(counter<5){
            counter++;
        }else{
            if(zustand == "Aufwäcken") {
                testCharakter.schlaeft();
            }else {
                testCharakter.wirdMuede();
            }
            testCharakter.wirdDreckig();
            testCharakter.wirdHungrig();
            counter = 0;
        }
    }
    public void hunger(){
        TextView essenNumber = (TextView) findViewById(R.id.textViewEssen);
        int hungerHp = testCharakter.getHunger();
        essenNumber.setText("Hunger "+hungerHp);
    }
    public void sauber(){
        TextView sauberNumber = (TextView) findViewById(R.id.textViewSauber);
        int sauberHp = testCharakter.getSauberkeit();
        sauberNumber.setText("Sauber "+sauberHp);
    }
    public void muede(){
        TextView energieNumber = (TextView) findViewById(R.id.textViewSchalfen);
        int energieHp = testCharakter.getEnergie();
        energieNumber.setText("Schafen "+energieHp);
    }
    public void hp(){
        TextView hpNumber = (TextView) findViewById(R.id.textViewHp);
        int hp = testCharakter.getHp();
        hpNumber.setText("HP "+hp);
    }
}
