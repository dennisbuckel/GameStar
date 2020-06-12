package com.example.stargame;

public class Charakter {
    //IV
    private String name;
    private int hunger;
    private int energie;
    private int sauberkeit;
    private int hp;
    boolean hatHunger(){
        boolean hatHunger=false;
        if(this.energie<=50) return true;
        else return false;
    }
    boolean istDreckig(){
        if(this.sauberkeit<=50) return true;
        else return false;
    }
    boolean istMuede(){
        if(this.energie<=50)return true;
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
            this.hunger -= 10;
        }    }
    //Charakter wird dreckig
    public void wirdDreckig(){
        if(this.sauberkeit > 0) {
            this.sauberkeit -= 10;
        }
    }
    //Charakter wird Müde
    public void wirdMuede(){
        if(this.energie > 0) {
            this.energie -= 10;
        }
    }
    //Charakter schläft
    public void schlaeft(){
        if(this.energie < 100) {
            this.energie += 10;
        }
    }
    //Charakter isst
    public void isst(){
        if(this.hunger < 100){
            this.hunger+=10;
        }
    }
    //Charakter wird gewaschen
    public void wirdSauber(){
        if(this.sauberkeit < 100) {
            this.sauberkeit += 10;
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
    /**
     * Gibt den Stern einen Namen
     * @param _name wird der Name des Sternens
     */
    public void namensVergabe(String _name){

        this.name = _name;

    }

}
