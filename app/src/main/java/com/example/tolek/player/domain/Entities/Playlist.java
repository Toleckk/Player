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
import com.example.tolek.player.R;
import com.example.tolek.player.Repository.EntityKeeper;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.PlaylistActivity.PlaylistActivity;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.gui.viewHolder.PlaylistView;

import java.util.ArrayList;

public class Playlist extends Entity implements EntityKeeper<Song>{
    private String name;
    private ArrayList<Song> list;

    public Playlist(String name){
        this.name = name;
        list = new ArrayList<>();
    }

    public Playlist(String name, Song song){
        this(name);
        list.add(song);
    }

    public Playlist(String name, ArrayList<Song> list){
        this.name = name;
        this.list = list;
    }

    public Playlist(ArrayList<Song> list){
        this("", list);
    }

    public void setName(String name){
        this.name = name;
    }

    public void addSong(Song song){
        list.add(song);
    }

    public String getName(){
        return name;
    }

    @Override
    public ArrayList<Song> getList(){
        return list;
    }

    @Override
    public boolean checkQuery(String query) {
        return name.toLowerCase().contains(query.toLowerCase());
    }
}
