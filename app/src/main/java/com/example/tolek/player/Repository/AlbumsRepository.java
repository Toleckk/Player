package com.example.tolek.player.Repository;

import com.example.tolek.player.Entities.Album;

import java.util.ArrayList;

public class AlbumsRepository {
    private ArrayList<Album> albums;

    public AlbumsRepository(ArrayList<Album> albums){
        this.albums = albums;
    }

    public ArrayList<Album> getList(){
        return albums;
    }
}
