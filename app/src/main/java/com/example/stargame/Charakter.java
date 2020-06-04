package com.example.stargame;

public class Charakter {
    //IV
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
        }    }
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
    public void updateHp(){
        this.hp= (this.energie+this.sauberkeit+this.hunger);
    }

    public int getHunger() {
        return hunger;
    }

    public int getEnergie() {
        return energie;
    }

    public int getHp() {
        return hp = energie+hunger+sauberkeit;
    }

    public int getSauberkeit() {
        return sauberkeit;
    }
}
