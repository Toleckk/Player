package com.example.tolek.player.Entities;

import java.util.ArrayList;

public class Album {

    private String albumName;
    private String cover;
    private String artist;
    private ArrayList<Song> songs;

    public Album(String albumName, String artist){
        this.albumName = albumName;
        this.artist = artist;
        songs = new ArrayList<>();
    }

    public void addSong(Song song){
        songs.add(song);
        if(cover == null && song.getAlbumArt() != null)
            cover = song.getAlbumArt();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getCover() {
        return cover;
    }

    public String getArtist() {
        return artist;
    }

    public void setCovers() {
        for(Song song : songs)
            song.setCover(cover);
    }
}
