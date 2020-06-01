package com.example.stargame;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);
    }


    private Handler mHandler = new Handler();
    private int zaehler = 0;

    /**
     * In timer.run() werden alle Methoden in einem 2 Sekunden Intervall aufgerufen
     */
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(GameActivity.this, "Zähler " + zaehler, Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(timer, 2000);
            zaehler += 1;
        }
    };


}
