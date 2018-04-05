package com.example.tolek.player.Entities;

import java.util.ArrayList;


public class Album {


    private String albumName;
    private String albumArt;
    private String artist;
    private ArrayList<Song> songs;

    public Album(String albumName, String albumArt, String artist){
        this.albumName = albumName;
        this.albumArt = albumArt;
        this.artist = artist;
        songs = new ArrayList<>();
    }

    public void addSong(Song song){
        songs.add(song);
    }

    public ArrayList<Song> getSongs() {
        return songs;
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
}
