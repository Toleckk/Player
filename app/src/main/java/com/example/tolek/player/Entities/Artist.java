package com.example.tolek.player.Entities;

import java.util.ArrayList;



public class Artist {
    private String name;
    private ArrayList<Song> songs;

    private String art;

    public Artist(Song song) {
        songs = new ArrayList<>();
        songs.add(song);

        if(song.getAlbumArt() != null)
            art = song.getAlbumArt();
        this.name = song.getArtist();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song){
        songs.add(song);

        if(art != null && song.getAlbumArt() != null)
            art = song.getAlbumArt();
    }

    public String getName() {
        return name;
    }

    public String getArt() {
        return art;
    }
}
