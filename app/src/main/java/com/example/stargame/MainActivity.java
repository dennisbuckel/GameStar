package com.example.stargame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        Button start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity();
                speicherDesNamens();
            }
        });

        // Deitei öffenen in der die Daten liegen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();

        String schonExistierend = speicherung.getString("tag", "FALSE");

        if(schonExistierend != "FALSE"){

            hpReduzierenHintergrundsWechsel();
            int hungerHP = speicherung.getInt("hungerHp", 0);
            int sauberkeitsHP = speicherung.getInt("sauberHp", 0);
            int energieHP = speicherung.getInt("energieHp",0);

            if((hungerHP + sauberkeitsHP +energieHP) > 0){

                launchActivity();

            }else{
                Toast.makeText(MainActivity.this, " Wo waren Sie?... Hier Stern ist gestorben!", Toast.LENGTH_SHORT).show();
                editor.putInt("hungerHp", 100);
                editor.putInt("sauberHp", 100);
                editor.putInt("energieHp", 100);
                editor.putInt("hintergrund", 99);
                editor.commit();

            }

        }

        namensEingabe();

    }

    public void launchActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    /**
     *  Zeigt eine einen Platzhalter oder den Namen des vorigen Sternes an
     */
    public void namensEingabe(){

        // Deitei öffenen in der die Daten liegen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();
        String name = speicherung.getString("name", "FALSE");

        EditText nameText = (EditText) findViewById(R.id.name);
        String nameTextString = nameText.getText().toString();
        if(name == "FALSE"){

            nameText.setText("Geben Sie Ihren Stern einen Namen...");

        }else{

            nameText.setText(name);

        }
    }

    /**
     * Speichert den Namen aud der Texteingabe oder einen Defaultwert
     */
    public void speicherDesNamens(){

        // Deitei öffenen in der die Daten liegen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();

        //Wert aus der Texteingabe
        EditText nameText = (EditText) findViewById(R.id.name);
        String nameTextString = nameText.getText().toString();

        if(nameTextString.equals("Geben Sie Ihren Stern einen Namen...")){

            //Default Name
            nameTextString = "Jonny";

        }
        editor.putString("name", nameTextString);
        //Speicherung der Daten
        editor.commit();

    }

    /**
     *  Berechnet die Zeit in Sekunden der aktuellen Zeit
     */
    public int aktuelleZeitInSekunden(){

        // Aktuelles Datum
        Calendar calendar = Calendar.getInstance();
        //Legt das Datenformat fest
        String aktuellerTag = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        //Trennt die ersten zwei Zeichen von der Zeichenkette(der Tag)
        aktuellerTag = aktuellerTag.substring(0, 2);
        //falls statt 04, 4. abgeschnitten wird -> 4
        if(aktuellerTag.contains(".")){
            aktuellerTag = aktuellerTag.substring(0, 1);
        }

        //Aktuelle Uhrzeit im Format HH:mm:ss
        SimpleDateFormat zeitformat = new SimpleDateFormat("HH:mm:ss");
        String zeitformatS = zeitformat.format(calendar.getTime());

        //Schneidet HH aus
        String zeitformatStunde = zeitformatS.substring(0, 2);
        //Schneidet mm aus
        String zeitformatMinute = zeitformatS.substring(3, 5);
        //Schneidet ss aus
        String zeitformatSekunden = zeitformatS.substring(6, 8);

        int aktuelleZeitInSekunden = ((Integer.parseInt(aktuellerTag) * 24 * 60) + (Integer.parseInt(zeitformatStunde) * 60) + Integer.parseInt(zeitformatMinute)) * 60 + Integer.parseInt(zeitformatSekunden);

        return aktuelleZeitInSekunden;

    }
    /**
     *  Berechnet die Zeit in Sekunden des letzten Logins
     */
    public int letzteLoginZeitInSekunden(){
        // Deitei öffenen in der die Daten liegen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();
        String letzterLoginTag = speicherung.getString("tag", "00");
        String letzterLoginUhrzeit = speicherung.getString("uhrzeit", "00:00:00");

        String letzterLoginStunde = letzterLoginUhrzeit.substring(0, 2);
        String letzterLoginMinute = letzterLoginUhrzeit.substring(3, 5);
        String letzterLoginSekunde = letzterLoginUhrzeit.substring(6, 8);


        int letzteLoginZeitInSekunden = ((Integer.parseInt(letzterLoginTag) * 24 * 60) + (Integer.parseInt(letzterLoginStunde) * 60) + Integer.parseInt(letzterLoginMinute)) * 60 + Integer.parseInt(letzterLoginSekunde);

        return letzteLoginZeitInSekunden;

    }
    /**
     *   Reduziert die HPs entsprechend der vergangenen Zeit und schreibt die neuen HPs in die Datei
     */
    public void hpReduzierenHintergrundsWechsel(){
        // Deitei öffenen in der die Daten liegen
        SharedPreferences speicherung = getSharedPreferences("SpeicherDatei", 0);

        //Editor Klasse initialiesieren
        SharedPreferences.Editor editor = speicherung.edit();
        int hungerHP = speicherung.getInt("hungerHp", 0);
        int sauberkeitsHP = speicherung.getInt("sauberHp", 0);
        int energieHP = speicherung.getInt("energieHp",0);
        int hintergrund = speicherung.getInt("hintergrund",0);


        int aktuelleZeitInSekunden = aktuelleZeitInSekunden();
        int letzteLoginZeitInSekunden = letzteLoginZeitInSekunden();
        int vergangeneSekunden = aktuelleZeitInSekunden - letzteLoginZeitInSekunden;

        //Lässt alle verpassten Intervalle aufholen
        int intervallEinheiten = vergangeneSekunden / 5;
        int intervallEinheitenHintergrund = vergangeneSekunden / 10;

        for(int i=0; i<=intervallEinheiten; i++){

            hungerHP += -10;
            sauberkeitsHP += -10;
            energieHP += -10;

        }
        if(hungerHP < 0){
            hungerHP = 0;
        }
        if(sauberkeitsHP < 0){
            sauberkeitsHP = 0;
        }
        if(energieHP < 0){
            energieHP = 0;
        }

        for(int i=0; i<=intervallEinheitenHintergrund; i++){

           if(hintergrund == 99){

               hintergrund = 0;

           }else {

               hintergrund += 33;

           }

        }
        editor.putInt("hungerHp", hungerHP);
        editor.putInt("sauberHp", sauberkeitsHP);
        editor.putInt("energieHp", energieHP);
        editor.putInt("hintergrund", hintergrund);

        //Speicherung der Daten
        editor.commit();

    }



}




