package com.example.tolek.player.Entities;

import android.graphics.drawable.Drawable;

/**
 * Created by Tolek on 07.03.2018.
 */

public class Album {
    private String albumName;
    private String albumArt;
    private String artist;
    private int tracksQuantity = 1;

    public Album(String albumName, String albumArt, String artist){
        this.albumName = albumName;
        this.albumArt = albumArt;
        this.artist = artist;
    }

    public void incrementingQuantity(){
        tracksQuantity++;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getArtist() {
        return artist;
    }

    public int getTracksQuantity(){
        return tracksQuantity;
    }
}
