package com.example.tolek.player;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tolek.player.Entities.Album;

import java.util.ArrayList;


public final class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumViewHolder> {

    ArrayList<Album> albumsList;
    Drawable musicArt;

    public AlbumRecyclerViewAdapter(ArrayList<Album> albumsList, Drawable musicArt){
        this.musicArt = musicArt;
        this.albumsList = albumsList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_view, parent, false);
        AlbumViewHolder songViewHolder = new AlbumViewHolder(v);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.album.setText(albumsList.get(position).getAlbumName());
        holder.artist.setText(albumsList.get(position).getArtist());

        holder.albumArt.setImageDrawable(albumsList.get(position).getAlbumArt() == null
                ? musicArt
                : Drawable.createFromPath(albumsList.get(position).getAlbumArt())
        );
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder{

        CardView mainCardView;
        ImageView albumArt;
        TextView album;
        TextView artist;

        public AlbumViewHolder(View itemView){
            super(itemView);

            mainCardView = itemView.findViewById(R.id.albumCardView);
            albumArt = itemView.findViewById(R.id.album_art);
            album = itemView.findViewById(R.id.album);
            artist = itemView.findViewById(R.id.album_artist);
        }
    }
}
