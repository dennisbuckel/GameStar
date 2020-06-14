package com.example.stargame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    Button fuettern;
    Button schlafen;
    Button saeubern;

    Charakter testCharakter = new Charakter();

    int counter = 0;

    String zustand ="Schlafen";

    int backgroundIntervall = 0;
    int showesBackgroundView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);

        herstellungDerSternes();

        fuettern = (Button) findViewById(R.id.fuettern);
        schlafen = (Button) findViewById(R.id.schlafen);
        saeubern = (Button) findViewById(R.id.saeubern);

        fuettern.setOnClickListener(this);
        schlafen.setOnClickListener(this);
        saeubern.setOnClickListener(this);
        Button muedeButton = (Button) findViewById(R.id.schlafen);
        timer.run();

        namensAnzeige();

        final RelativeLayout startLin = findViewById(R.id.linear);
        startLin.setBackgroundResource(R.drawable.morgens);

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

            changeBackground();

            hp();
            hunger();
            sauber();
            muede();

            abnutzung();
            //Toast.makeText(GameActivity.this, "Zähler " + zaehler, Toast.LENGTH_SHORT).show();
            handler.postDelayed(timer, 1000);

            int hp = testCharakter.getHp();
            if(hp==0) {
                handler.removeCallbacks(timer);
                sternIstGestorben();
            }

        }
    };

    public void changeBackground(){

        //Hintergrund Tag/Nacht ändern
        final RelativeLayout lin = findViewById(R.id.linear);

        if(backgroundIntervall == 9){

            switch(showesBackgroundView){
                case 0:
                    lin.setBackgroundResource(R.drawable.mittags);
                    showesBackgroundView++;
                    break;
                case 1:
                    lin.setBackgroundResource(R.drawable.nachmittags);
                    showesBackgroundView++;
                    break;
                 case 2:
                    lin.setBackgroundResource(R.drawable.nachts);
                     showesBackgroundView++;
                     break;
                case 3:
                    lin.setBackgroundResource(R.drawable.morgens);
                    showesBackgroundView = 0;
                    break;
            }
            backgroundIntervall = 0;
        }else{

            backgroundIntervall++;

        }

    }

    public void sternIstGestorben(){
        // navigiert zurück zur MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Deitei öffenen in der die Daten liegen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();
        editor.putInt("hungerHp", 0);
        editor.putInt("sauberHp", 0);
        editor.putInt("energieHp", 0);
        editor.commit();

    }
    /**
     * Zeigt den Namen das Sternes an
     *
     */
    public void namensAnzeige(){

        //Share Preferences Datei öffnen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();

        String nameString = speicherung.getString("name", "Jonny");

        TextView name = (TextView) findViewById(R.id.viewName);

        name.setText(nameString);

    }

    /**
     * Hier bekommt der Stern seine Hp
     */
    public void herstellungDerSternes(){

        // Deitei öffenen in der die Daten liegen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();

        int hungerHP = speicherung.getInt("hungerHp", 100);
        int sauberkeitsHP = speicherung.getInt("sauberHp", 100);
        int energieHP = speicherung.getInt("energieHp",100);
        if(hungerHP+sauberkeitsHP+energieHP == 0){

            hungerHP = 100;
            sauberkeitsHP = 100;
            energieHP = 100;

        }
        testCharakter.updateHungerHp(hungerHP);
        testCharakter.updateSauberkeitHp(sauberkeitsHP);
        testCharakter.updateEnergieHp(energieHP);

    }
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

    @Override
    protected void onStop() {
        super.onStop();

        //Aktueller Tag
        Calendar calendar = Calendar.getInstance();
        String aktuellerTag = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        aktuellerTag = aktuellerTag.substring(0, 2);
        if(aktuellerTag.contains(".")){
            aktuellerTag = aktuellerTag.substring(0, 1);
        }
        //Uhrzeit
        SimpleDateFormat zeitformat = new SimpleDateFormat("HH:mm:ss");
        String zeitformatS = zeitformat.format(calendar.getTime());


        int hungerHp = testCharakter.getHunger();
        int sauberHp = testCharakter.getSauberkeit();
        int energieHp = testCharakter.getEnergie();


        //Share Preferences Datei öffnen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();

        //Schreiben der wichtigen Daten
        editor.putString("tag", aktuellerTag);
        editor.putString("uhrzeit",  zeitformatS);

        editor.putInt("hungerHp", hungerHp);
        editor.putInt("sauberHp", sauberHp);
        editor.putInt("energieHp", energieHp);

        //Speicherung der Daten

        editor.commit();
    }



}
