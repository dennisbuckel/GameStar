package com.example.stargame;

import android.widget.ImageView;

public class Charakter {
    //IV
    float posX;
    float posY;


    private int hunger;
    private int energie;
    private int sauberkeit;
    private int hp;

    boolean istAusgeschlafen(){
        if (this.energie > 90)
            return true;
        else
            return false;
    }
    boolean istSauber(){
        if (this.sauberkeit > 90)
            return true;
        else
            return false;
    }
    boolean istSatt(){
        if (this.hunger > 90)
            return true;
        else
            return false;
    }

    boolean hatVielHunger(){
        boolean hatHunger=false;
        if(this.hunger<=60) return true;
        else return false;
    }
    boolean istSehrDreckig(){
        if(this.sauberkeit<=60) return true;
        else return false;
    }
    boolean istSehrMuede(){
        if(this.energie<=60)return true;
        else return false;
    }

    boolean hatHunger(){
        boolean hatHunger=false;
        if(this.hunger<=90 && this.hunger>60) return true;
        else return false;
    }
    boolean istDreckig(){
        if(this.sauberkeit<=90 && this.sauberkeit> 60) return true;
        else return false;
    }
    boolean istMuede(){
        if(this.energie<=90&& this.energie> 60)return true;
        else return false;
    }
    //Constructor
    Charakter(){
        this.energie=100;
        this.hunger=100;
        this.sauberkeit=100;
        this.hp= energie+hunger+sauberkeit;
    }
    //Methoden für Eigenschaften
    //Charakter bekommt Hunger
    public void wirdHungrig(){
        if(this.hunger > 0) {
            this.hunger -= 5;
        }
    }
    //Charakter wird dreckig
    public void wirdDreckig(){
        if(this.sauberkeit > 0) {
            this.sauberkeit -= 5;
        }
    }
    //Charakter wird Müde
    public void wirdMuede(){
        if(this.energie > 0) {
            this.energie -= 5;
        }
    }
    //Charakter schläft
    public void schlaeft(){
        if(this.energie < 100) {
            this.energie += 5;
        }

    }
    //Charakter isst
    public void isst(){
        if(this.hunger < 100){
            this.hunger+=10;
        }
        if(this.hunger > 100) {
            this.hunger = 100;
        }
    }
    //Charakter wird gewaschen
    public void wirdSauber(){
        if(this.sauberkeit < 100) {
            this.sauberkeit += 10;
        }
        if(this.sauberkeit > 100) {
            this.sauberkeit = 100;
        }
    }
    /**
     * Gibt die HungerHp zurück
     */
    public int getHunger() {
        return hunger;
    }
    /**
     * Gibt die energieHp zurück
     */
    public int getEnergie() {
        return energie;
    }

    /**
     * Gibt die sauberkeitHp zurück
     */
    public int getSauberkeit() {
        return sauberkeit;
    }
    /**
     * gib die GesamtHp zurück
     */
    public int getHp() {
        return hp = energie+hunger+sauberkeit;
    }
    /**
     * Update die HungerHp
     * @param _hp ist der neue Wert
     */
    public void updateHungerHp(int _hp) {
         this.hunger = _hp;
    }
    /**
     * Update die SauberkeitsHp
     * @param _hp ist der neue Wert
     */
    public void updateSauberkeitHp(int _hp) {
        this.sauberkeit = _hp;
    }
    /**
     * Update die EnergieHp
     * @param _hp ist der neue Wert
     */
    public void updateEnergieHp(int _hp) {
        this.energie = _hp;
    }

    public void setPosX(float _posX){
        this.posX=_posX;
    }
    public void setPosY(float _posY){
        this.posY=_posY;
    }

    public float getPosX(){
        return this.posX;
    }
    public float getPosY(){
        return this.posY;
    }

}
