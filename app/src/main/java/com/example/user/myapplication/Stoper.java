package com.example.user.myapplication;

/**
 * Created by user on 04.01.2017.
 */

class Stoper {


    private long start;


    private long stop;

    private String nazwa;

    public Stoper() {

        this("");

    }

    public Stoper(String nazwa) {

        this.nazwa = nazwa;

    }

    public void start(){

        start = System.currentTimeMillis();

    }



    public void stop(){

// pobieramy aktualny czas - stop stopera

        stop = System.currentTimeMillis();

    }

    public double pobierzWynik(){

// zamiana milisekund na sekundy

        return (stop - start) / 1000.0;

    }


// przesłonięta metoda toString()

    public String toString(){

// zwracamy w formie tekstowej informacje o naszym stoperze

        return nazwa + ": " + this.pobierzWynik() + " s.";

    }

}