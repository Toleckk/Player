package com.example.tolek.player;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tolek.player.Entities.Album;
import com.example.tolek.player.Entities.Artist;
import com.example.tolek.player.Entities.Song;

import java.util.ArrayList;


public final class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.ArtistViewHolder> {

    ArrayList<Artist> artistsList;

    public ArtistRecyclerViewAdapter(ArrayList<Artist> artistsList){
        this.artistsList = artistsList;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_card_view, parent, false);
        ArtistViewHolder songViewHolder = new ArtistViewHolder(v);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        holder.artist.setText(artistsList.get(position).getName());
        holder.tracks.setText(String.valueOf(artistsList.get(position).getTracksQuantity()));
    }

    @Override
    public int getItemCount() {
        return artistsList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder{

        CardView mainCardView;
        TextView artist;
        TextView tracks;

        public ArtistViewHolder(View itemView){
            super(itemView);

            mainCardView = itemView.findViewById(R.id.artistCardView);
            artist = itemView.findViewById(R.id.artist);
            tracks = itemView.findViewById(R.id.tracks_quantity);
        }
    }
}
