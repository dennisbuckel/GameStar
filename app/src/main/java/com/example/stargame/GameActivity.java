package com.example.stargame;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.util.Calendar;

public class GameActivity extends AppCompatActivity implements View.OnDragListener, View.OnTouchListener{

    ImageView fuettern;
    ImageView schlafen;
    ImageView saeubern;
    ImageView destination;
    ImageView stern;




    ProgressBar hpbar;

    Charakter testCharakter = new Charakter();

    int intervall = 1;
    int counter = 0;
    int schlafCounter = 0;
    int tick=0;

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

        fuettern = (ImageView) findViewById(R.id.fuettern);
        schlafen = (ImageView) findViewById(R.id.schlafen);
        saeubern = (ImageView) findViewById(R.id.saeubern);
        destination = findViewById(R.id.stern);
        stern= findViewById(R.id.stern);

        fuettern.setOnTouchListener(this);
        schlafen.setOnTouchListener(this);
        saeubern.setOnTouchListener(this);
        destination.setOnDragListener(this);


        ImageView muedeButton = (ImageView) findViewById(R.id.schlafen);
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
            ImageView muedeButton = (ImageView) findViewById(R.id.schlafen);
            //muedeButton.setText(zustand);

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
            moving(stern, tick);


            abnutzung();
            int hp = testCharakter.getHp();
            hpbar.setProgress(hp);

            updateImage(stern);
            updateEating(stern);
            //Toast.makeText(GameActivity.this, "Zähler " + zaehler, Toast.LENGTH_SHORT).show();
            handler.postDelayed(timer, 500);





            if(hp==0) {
                handler.removeCallbacks(timer);
                sternIstGestorben();
            }
            if(testCharakter.getEnergie() >= 100){
                zustand = "Schlafen";
                updateImage(stern);
            }

            SternSchlafAnimation();

            if (tick==9)
                tick=0;
            else
                tick++;

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

        // Aktueller Tag und Zeit
        Calendar calendar = Calendar.getInstance();
        String todesTag = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        todesTag = todesTag.substring(0, 2);
        if(todesTag.contains(".")){
            todesTag = todesTag.substring(0, 1);
        }
        //Uhrzeit
        SimpleDateFormat zeitformat = new SimpleDateFormat("HH:mm:ss");
        String todesUhrzeit = zeitformat.format(calendar.getTime());

        //Schneidet HH aus
        String zeitformatStunde = todesUhrzeit.substring(0, 2);
        //Schneidet mm aus
        String zeitformatMinute = todesUhrzeit.substring(3, 5);
        //Schneidet ss aus
        String zeitformatSekunden = todesUhrzeit.substring(6, 8);

        int aktuelleZeitInSekunden = ((Integer.parseInt(todesTag) * 24 * 60) + (Integer.parseInt(zeitformatStunde) * 60) + Integer.parseInt(zeitformatMinute)) * 60 + Integer.parseInt(zeitformatSekunden);

        int ersterLoginInSekunden = speicherung.getInt("geburtsZeit",0);

        int ueberlebteZeit = aktuelleZeitInSekunden - ersterLoginInSekunden;

        editor.putInt("ueberlebteZeit", ueberlebteZeit);

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
        changeBackground();
        showesBackgroundProzess = showesBackgroundView;


    }

    /**
     * Regelt die Abnutzung des Sternes
     */
    public void abnutzung(){

            if (zustand.equals("Aufwäcken")) {
                testCharakter.schlaeft();

            }
            else if (zustand.equals("Schlafen") && counter<intervall) {

                    testCharakter.wirdDreckig();
                    testCharakter.wirdHungrig();
                    testCharakter.wirdMuede();
                    counter++;

            }else if (counter==5) {
                counter = 0;
            }
            else
                counter++;

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
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:

                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:

                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                String clipData = event.getClipDescription().getLabel().toString();
                switch (clipData) {
                    case "shower":
                        if(zustand == "Aufwäcken") {

                            Toast.makeText(GameActivity.this, "Sie müssen den Stern erst aufwäcken...", Toast.LENGTH_SHORT).show();

                        }else{

                            testCharakter.wirdSauber();
                            playWashingSound();
                        }

                        break;
                    case "sleep":
                        if(zustand == "Schlafen") {

                            zustand = "Aufwäcken";
                            updateImage(stern);

                        }else{

                            zustand = "Schlafen";
                            updateImage(stern);

                        }

                        break;
                    case "food":
                        if(zustand == "Aufwäcken") {

                            Toast.makeText(GameActivity.this, "Sie müssen den Stern erst aufwäcken...", Toast.LENGTH_SHORT).show();

                        }else{

                            testCharakter.isst();
                            playEatingSound();

                        }
                }


                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_ENDED:

                if (event.getResult()==false) {
                    Toast.makeText(GameActivity.this, "Daneben, probiere es nochmal!!", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

        switch (v.getId()) {
            case R.id.schlafen:


                v.startDragAndDrop(data, mShadow, null, 0);

                break;
            case R.id.fuettern:

                v.startDragAndDrop(data, mShadow, null, 0);
                break;

            case R.id.saeubern  :
                v.startDragAndDrop(data, mShadow, null, 0);
                break;
        }

        return false;
    }
    public void moving(View v,int tick) {
        Float posX = v.getX();
        Float posY = v.getY();

        if (zustand.equals("Schlafen")) {
            if (tick < 5) {
                posX += 1;

            } else if (tick >= 5) {
                posX -= 1;

            }
        }

        v.setX(posX);

    }
    public void updateImage(ImageView v){
        int hp= testCharakter.getHp();

        if(testCharakter.istDreckig() && !testCharakter.istMuede() && !testCharakter.istSehrMuede()){
            if (zustand=="Schlafen")
                v.setImageResource(R.drawable.stern1_1);
            else
                v.setImageResource(R.drawable.stern1_1_schlaeft);

        }
        else if (testCharakter.istDreckig() && testCharakter.istMuede()){
            if (zustand=="Schlafen")
                v.setImageResource(R.drawable.stern2_1);
            else
                v.setImageResource(R.drawable.stern2_1_schlaeft);

        }
        else if (testCharakter.istMuede() && !testCharakter.istDreckig()){
           if (zustand=="Schlafen")
               v.setImageResource(R.drawable.stern_2);
           else
               v.setImageResource(R.drawable.stern2_schlaeft);
        }
        else if (testCharakter.istSauber() && testCharakter.istAusgeschlafen()){
            if (zustand=="Schlafen")
                v.setImageResource(R.drawable.stern_1);
            else
                v.setImageResource(R.drawable.stern_1_schlaeft);
        }

        else if (!testCharakter.istSehrDreckig() && testCharakter.istSehrMuede()){
            if (zustand=="Schlafen")
                v.setImageResource(R.drawable.stern_3);
            else
                v.setImageResource(R.drawable.stern3s);
        }
        else if (testCharakter.istSehrDreckig() && testCharakter.istSehrMuede()){
            if (zustand=="Schlafen")
                v.setImageResource(R.drawable.stern3_1);
            else
                v.setImageResource(R.drawable.stern3_1_schlaeft);
        }
        else if (testCharakter.istSehrMuede() && testCharakter.istDreckig()){
            if (zustand=="Schlafen")
                v.setImageResource(R.drawable.stern3_1);
            else
                v.setImageResource(R.drawable.stern3_1_schlaeft);

        }






    }
    public void updateEating (ImageView v){
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) v.getLayoutParams();

        if (testCharakter.hatHunger()) {
            params.width= 400;
            params.height= 400;
            v.setLayoutParams(params);
        }
        else if (testCharakter.hatVielHunger()){
            params.width=350;
            params.height=350;
            v.setLayoutParams(params);

        }
        else{
            params.width=450;
            params.height=450;
            v.setLayoutParams(params);
        }

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
