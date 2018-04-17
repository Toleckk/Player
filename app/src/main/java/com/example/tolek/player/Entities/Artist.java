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



public class Artist extends Entity{
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

    public ArrayList<Song> getSongs() {
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

    @Override
    public RecyclerView.ViewHolder createViewHolder(RecyclerView.ViewHolder v, Drawable art, ArrayList<Entity> list) {
        ViewHolder view = (ViewHolder) v;

        view.artistText.setText(getName());
        view.tracks.setText(String.valueOf(getSongs().size()));
        view.artist = Artist.this;

        Glide.with(view.mainCardView.getContext())
                .load(getArt())
                .apply(new RequestOptions().placeholder(art))
                .into(view.art);

        view.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(
                        new Intent(view.getContext(), AlbumActivity.class)
                                .putExtra("Artist",
                                        MediaStore.getInstance().getArtists().indexOf(Artist.this))
                );
            }
        });
        return view;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mainCardView;
        public TextView artistText;
        public TextView tracks;
        public ImageView art;
        public Artist artist;

        public ViewHolder(View itemView) {
            super(itemView);
            mainCardView = itemView.findViewById(R.id.artistCardView);
            artistText = itemView.findViewById(R.id.artist);
            tracks = itemView.findViewById(R.id.tracks_quantity);
            art = itemView.findViewById(R.id.artist_art);
        }
    }
}
