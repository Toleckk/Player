package com.example.tolek.player.domain.Entities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.Repository.EntityKeeper;
import com.example.tolek.player.gui.PlaylistActivity.PlaylistActivity;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.viewHolder.ArtistView;

import java.util.ArrayList;



public class Artist extends Entity implements EntityKeeper<Song>{
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

    @Override
    public ArrayList<Song> getList() {
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

    @Override
    public boolean checkQuery(String query) {
        return getName().toLowerCase().contains(query.toLowerCase());
    }
}
