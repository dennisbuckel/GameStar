package com.example.stargame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);
        timer.run();


        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            changeBackground();
            mHandler.postDelayed(timer, 10000);
            zaehler += 1;
        }
    };

    LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
    
    public void changeBackground(){
        if(zaehler %2==0  ){
          linear.setBackgroundResource(R.drawable.hintergrund1);  
        }else{
            linear.setBackgroundResource(R.drawable.hintergrund2); 
        }
    }


}
