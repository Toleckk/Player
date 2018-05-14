package com.example.tolek.player.domain.Entities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.Repository.EntityKeeper;
import com.example.tolek.player.gui.PlaylistActivity.PlaylistActivity;
import com.example.tolek.player.R;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.viewHolder.AlbumView;

import java.util.ArrayList;

public class Album extends Entity implements EntityKeeper<Song>{

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

    public ArrayList<Song> getList() {
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

    @Override
    public boolean checkQuery(String query) {
        return (getAlbumName().toLowerCase().contains(query)
                || getArtist().toLowerCase().contains(query));
    }
}
