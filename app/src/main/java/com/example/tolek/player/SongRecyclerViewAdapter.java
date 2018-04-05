package com.example.tolek.player;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.Util.Player;

import java.util.ArrayList;


public final class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.SongViewHolder> {

    ArrayList<Song> songsList;
    Drawable musicArt;

    public SongRecyclerViewAdapter(ArrayList<Song> songsList, Drawable musicArt){
        this.songsList = songsList;
        this.musicArt = musicArt;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card_view, parent, false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.title.setText(songsList.get(position).getTitle());
        holder.artistAndAlbum.setText(songsList.get(position).getArtist() +
                " — " + songsList.get(position).getAlbum());

        Glide.with(holder.mainCardView.getContext())
                .load(songsList.get(position).getAlbumArt())
                .apply(new RequestOptions().placeholder(musicArt))
                .into(holder.albumArt);
        holder.song = songsList.get(position);
        holder.playlist = songsList;
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder{

        CardView mainCardView;
        ImageView albumArt;
        TextView title;
        TextView artistAndAlbum;
        Song song;
        ArrayList<Song> playlist;

        public SongViewHolder(final View itemView){
            super(itemView);

            mainCardView = itemView.findViewById(R.id.songCardView);
            albumArt = itemView.findViewById(R.id.song_art);
            title = itemView.findViewById(R.id.song_title);
            artistAndAlbum = itemView.findViewById(R.id.song_artist_and_album);

            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Player player = Player.getInstance();
                    if(player.getCurrentSong() != null && player.getCurrentSong().equals(song) && player.isPlaying())
                        player.pause();
                    else {
                        if(!player.getCurrentPlaylist().equals(playlist))
                            player.setPlaylist(playlist);

                        player.play(song);
                    }
                }
            });
        }
    }
}
