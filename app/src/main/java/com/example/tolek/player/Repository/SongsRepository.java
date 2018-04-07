package com.example.tolek.player.Repository;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.debug.MediaSearcher;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SongsRepository {
    private ArrayList<Song> songs;

    public interface Filter{
        boolean filter(Song song);
    }

    public SongsRepository(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public Song getSong(int index){
        return songs.get(index);
    }

    public ArrayList<Song> getList(Filter filter){
        ArrayList<Song> list = new ArrayList<>();
        for(Song song : songs)
            if(filter.filter(song))
                list.add(song);
        return list;
    }

    public ArrayList<Song> getList() {
        return songs;
    }
}
