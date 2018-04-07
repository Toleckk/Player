package com.example.tolek.player.Repository;

import com.example.tolek.player.Entities.Artist;

import java.util.ArrayList;

public class ArtistsRepository {
    private ArrayList<Artist> artists;

    public ArtistsRepository(ArrayList<Artist> artists){
        this.artists = artists;
    }

    public ArrayList<Artist> getList(){
        return artists;
    }
}
