package com.example.tolek.player.Repository;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.debug.MediaSearcher;

import java.util.ArrayList;

public class SongsRepository {
    private static SongsRepository instance = new SongsRepository();

    private ArrayList<Song> songs;

    public static SongsRepository getInstance() {
        return instance;
    }

    private SongsRepository() {}

    public void initialize() {
        songs = new MediaSearcher().findSongs();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
}
