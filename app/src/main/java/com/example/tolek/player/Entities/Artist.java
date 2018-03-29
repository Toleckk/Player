package com.example.tolek.player.Entities;

/**
 * Created by Tolek on 08.03.2018.
 */

public class Artist {
    String name;
    int tracksQuantity = 1;

    public Artist(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void incrementQuantity(){
        tracksQuantity++;
    }
    public int getTracksQuantity() {
        return tracksQuantity;
    }
}
