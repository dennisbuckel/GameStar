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
        this.hunger-=10;
    }
    //Charakter wird dreckig
    public void wirdDreckig(){
        this.sauberkeit-=10;
    }
    //Charakter wird Müde
    public void wirdMuede(){
        this.energie-=10;
    }
    //Charakter schläft
    public void schlaeft(){
        this.energie+=10;
    }
    //Charakter isst
    public void isst(){
        this.hunger+=10;
    }
    //Charakter wird gewaschen
    public void wirdSauber(){
        this.sauberkeit+=10;
    }
    public void updateHp(){
        this.hp= (this.energie+this.sauberkeit+this.hunger);
    }

}
