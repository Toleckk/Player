package com.example.tolek.player.Entities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.AlbumActivity.AlbumActivity;
import com.example.tolek.player.R;
import com.example.tolek.player.debug.MediaStore;

import java.util.ArrayList;

public class Album extends Entity{

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

    @Override
    public boolean checkQuery(String query) {
        return (getAlbumName().toLowerCase().contains(query)
                || getArtist().toLowerCase().contains(query));
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(RecyclerView.ViewHolder v, Drawable art, ArrayList<Entity> list) {
        ViewHolder holder = (ViewHolder) v;

        holder.albumText.setText(getAlbumName());
        holder.artist.setText(getArtist());

        Glide.with(holder.mainCardView.getContext())
                .load(getCover())
                .apply(new RequestOptions().placeholder(art))
                .into(holder.albumArt);

        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(
                        new Intent(view.getContext(), AlbumActivity.class)
                                .putExtra("Album",
                                        MediaStore.getInstance().getAlbums().indexOf(Album.this))
                );
            }
        });
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mainCardView;
        public ImageView albumArt;
        public TextView albumText;
        public TextView artist;
        public Album album;

        public ViewHolder(View itemView) {
            super(itemView);

            mainCardView = itemView.findViewById(R.id.albumCardView);
            albumArt = itemView.findViewById(R.id.album_art);
            albumText = itemView.findViewById(R.id.album);
            artist = itemView.findViewById(R.id.album_artist);
        }
    }
}
