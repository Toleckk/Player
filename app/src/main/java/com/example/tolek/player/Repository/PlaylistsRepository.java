package com.example.tolek.player.Repository;

import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;

import java.util.ArrayList;

public class PlaylistsRepository extends Repository<Playlist>{

    public PlaylistsRepository(ArrayList<Playlist> playlists) {
        super(playlists);
    }

    public void addPlaylist(Playlist playlist){
        list.add(playlist);
        FileWorker.writePlaylist(playlist.getList(), "Playlists/" + playlist.getName());
    }

    public ArrayList<Playlist> getList() {
        return list;
    }
}
