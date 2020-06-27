package com.example.stargame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    ProgressBar hpbar;

    Charakter testCharakter = new Charakter();

    int intervall = 1;
    int counter = 0;
    int schlafCounter = 0;

    String zustand ="Schlafen";

    int backgroundIntervall = 0;
    int showesBackgroundView;
    int showesBackgroundProzess = 0;
    int progress;

    MediaPlayer eatingPlayer;
    MediaPlayer sleepingPlayer;
    MediaPlayer washingPlayer;
    MediaPlayer backgroundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);

        herstellungDerSternes();

        hpbar = (ProgressBar)findViewById(R.id.hpbar);

        fuettern = (Button) findViewById(R.id.fuettern);
        schlafen = (Button) findViewById(R.id.schlafen);
        saeubern = (Button) findViewById(R.id.saeubern);

        fuettern.setOnClickListener(this);
        schlafen.setOnClickListener(this);
        saeubern.setOnClickListener(this);
        Button muedeButton = (Button) findViewById(R.id.schlafen);
        timer.run();

        namensAnzeige();

        playBackgroundSound();

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

            if(backgroundIntervall == (5*2)){
                showesBackgroundProzess += 3;
                changeBackground();
            }else{
                backgroundIntervall++;
                if(showesBackgroundProzess <= 99) {
                    showesBackgroundProzess += 3;
                    ProgressBar tagnacht = (ProgressBar) findViewById(R.id.leiste);
                    tagnacht.setProgress(showesBackgroundProzess);
                }
            }

            hp();

            abnutzung();
            //Toast.makeText(GameActivity.this, "Zähler " + zaehler, Toast.LENGTH_SHORT).show();
            handler.postDelayed(timer, 500);

            int hp = testCharakter.getHp();
            hpbar.setProgress(hp);


            if(hp==0) {
                handler.removeCallbacks(timer);
                sternIstGestorben();
            }
            if(testCharakter.getEnergie() == 100){
                zustand = "Schlafen";
            }

            SternSchlafAnimation();

        }
    };
    /**
     * Schlafanimation des Sternes
     */
    public void SternSchlafAnimation(){

        TextView z1 = (TextView) findViewById(R.id.schlafZ1);
        TextView z2 = (TextView) findViewById(R.id.schlafZ2);

        if(zustand == "Aufwäcken"){
            playSleepingSound();
            stopEatingPlayer();
            stopWashingPlayer();
            z1.setText("Z");
            if(schlafCounter < 3){
                z2.setText("Z");
                schlafCounter++;
            }
            if(schlafCounter >= 3){
                z2.setText("");
                schlafCounter++;
                if(schlafCounter == 6){
                    schlafCounter = 0;
                }
            }

        }else{
            stopSleepingPlayer();
            z1.setText("");
            z2.setText("");
        }


    }
    /**
     * Ändert den Hintergrund und zeigt den Fortschritt dessen an
     */
    public void changeBackground(){

        //Hintergrund Tag/Nacht ändern
        final RelativeLayout lin = findViewById(R.id.linear);
        ProgressBar tagnacht = (ProgressBar) findViewById(R.id.leiste);

            switch(showesBackgroundView){
                case 0:
                    lin.setBackgroundResource(R.drawable.mittags);
                    showesBackgroundView = 33;
                    tagnacht.setProgress(showesBackgroundProzess);
                    break;
                case 33:
                    lin.setBackgroundResource(R.drawable.nachmittags);
                    showesBackgroundView = 66;
                    tagnacht.setProgress(showesBackgroundProzess);
                    break;
                 case 66:
                    lin.setBackgroundResource(R.drawable.nachts);
                     showesBackgroundView = 99;
                     tagnacht.setProgress(showesBackgroundProzess);
                     break;
                case 99:
                    lin.setBackgroundResource(R.drawable.morgens);
                    showesBackgroundView = 0;
                    showesBackgroundProzess=0;
                    tagnacht.setProgress(showesBackgroundProzess);
                    break;
            }
            backgroundIntervall = 0;
    }

    /**
     * Vorkehrungen beim Tod des Sternes
     */
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
        int hintergrund = speicherung.getInt("hintergrund",99);

        if(hungerHP+sauberkeitsHP+energieHP == 0){

            hungerHP = 100;
            sauberkeitsHP = 100;
            energieHP = 100;
            hintergrund = 99;

        }
        testCharakter.updateHungerHp(hungerHP);
        testCharakter.updateSauberkeitHp(sauberkeitsHP);
        testCharakter.updateEnergieHp(energieHP);


        showesBackgroundView = hintergrund;
        //Toast.makeText(GameActivity.this, "" +showesBackgroundView, Toast.LENGTH_SHORT).show();
        changeBackground();
        showesBackgroundProzess = showesBackgroundView;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fuettern:
                if(zustand == "Aufwäcken") {

                    Toast.makeText(GameActivity.this, "Sie müssen den Stern erst aufwäcken...", Toast.LENGTH_SHORT).show();

                }else{

                    testCharakter.isst();
                    playEatingSound();

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
                    playWashingSound();
                }
                break;
        }
    }
    /**
     * Regelt die Abnutzung des Sternes
     */
    public void abnutzung(){
        if(counter<intervall){
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
    /**
     *   Regelt die Hintergrungmusik der App
     */
    public void playBackgroundSound(){

        if(backgroundPlayer == null){

            backgroundPlayer = MediaPlayer.create(this, R.raw.background);
            backgroundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    backgroundPlayer.release();
                    backgroundPlayer = null;
                    playBackgroundSound();
                }
            });

        }
        backgroundPlayer.setVolume(0.1f, 0.1f);
        backgroundPlayer.start();

    }

    /**
     *   Regelt das Stoppen der Hintergrungmusik der App
     */
    private void stopBackgroundPlayer(){

        if(backgroundPlayer != null){

            //Sound stoppen und den Mediaplayer beenden
            backgroundPlayer.release();
            backgroundPlayer = null;

        }

    }

    /**
     *   Regelt die Essenssoundeffekte der App
     */
    public void playEatingSound(){

        if(eatingPlayer == null){

            eatingPlayer = MediaPlayer.create(this, R.raw.eating);
            eatingPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopEatingPlayer();
                }
            });

        }
        eatingPlayer.setVolume(1, 1);
        eatingPlayer.start();
        stopWashingPlayer();
    }

    /**
     *   Regelt das Stoppen der Essenssoundeffekte der App
     */
    private void stopEatingPlayer(){

        if(eatingPlayer != null){

            //Sound stoppen und den Mediaplayer beenden
            eatingPlayer.release();
            eatingPlayer = null;

        }

    }

    /**
     *   Regelt die Säuberungssoundeffekte der App
     */
    public void playWashingSound(){

        if(washingPlayer == null){

            washingPlayer = MediaPlayer.create(this, R.raw.washing);
            washingPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopWashingPlayer();
                }
            });

        }
        washingPlayer.setVolume(1, 1);
        washingPlayer.start();
        stopEatingPlayer();
    }

    /**
     *   Regelt das Stoppen der Säuberungssoundeffekte der App
     */
    private void stopWashingPlayer(){

        if(washingPlayer != null){

            //Sound stoppen und den Mediaplayer beenden
            washingPlayer.release();
            washingPlayer = null;

        }

    }

    /**
     *   Regelt die Schlafsoundeffekte der App
     */
    public void playSleepingSound(){

        if(sleepingPlayer == null){

            sleepingPlayer = MediaPlayer.create(this, R.raw.sleeping);
            sleepingPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopSleepingPlayer();
                }
            });

        }
        sleepingPlayer.setVolume(1, 1);
        sleepingPlayer.start();
    }

    /**
     *   Regelt das Stoppen der Schlafsoundeffekte der App
     */
    private void stopSleepingPlayer(){

        if(sleepingPlayer != null){

            //Sound stoppen und den Mediaplayer beenden
            sleepingPlayer.release();
            sleepingPlayer = null;

        }

    }

    /**
     * Zeigt die Hp inm der App an
     */
    public void hp(){
        TextView hpNumber = (TextView) findViewById(R.id.textViewHp);
        int hp = testCharakter.getHp();
        hpNumber.setText("HP "+hp);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Stoppt Sounds
        stopEatingPlayer();
        stopWashingPlayer();
        stopSleepingPlayer();
        stopBackgroundPlayer();


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

        editor.putInt("hintergrund", showesBackgroundView);

        //Speicherung der Daten

        editor.commit();
    }



}
