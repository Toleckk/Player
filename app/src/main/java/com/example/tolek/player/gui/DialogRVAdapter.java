package com.example.tolek.player.gui;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.R;
import com.example.tolek.player.Repository.EntityKeeper;
import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.dialogs.contextMenu.PlaylistAddingDialog;

import java.util.ArrayList;

import static com.example.tolek.player.gui.viewHolder.PlaylistView.defaultArt;

public class DialogRVAdapter extends RecyclerView.Adapter<DialogRVAdapter.ViewHolder>{
    private ArrayList<Playlist> playlists;
    private PlaylistAddingDialog fragment;

    public DialogRVAdapter(PlaylistAddingDialog fragment){
        playlists = MediaStore.getPlaylists().getList();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_playlist, parent, false),
                fragment,
                fragment.getAdapter()
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillFromPlaylist(playlists.get(position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mainCardView;
        public TextView playlistTitle;
        public TextView tracks;
        public ImageView art;
        public Playlist playlist;
        public DialogFragment fragment;
        public RVAdapter adapter;

        public ViewHolder(View itemView, DialogFragment fragment, RVAdapter adapter) {
            super(itemView);

            mainCardView = itemView.findViewById(R.id.playlistCardView);
            playlistTitle = itemView.findViewById(R.id.playlist_title);
            tracks = itemView.findViewById(R.id.tracks_quantity);
            art = itemView.findViewById(R.id.playlist_art);
            this.fragment = fragment;
            this.adapter = adapter;
        }

        public void fillFromPlaylist(Playlist playlist){
            this.playlist = playlist;

            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Song> songs = new ArrayList<>();
                    if(adapter.getSelected().size() > 0 && adapter.getSelected().get(0) instanceof EntityKeeper)
                        for(Entity entityKeeper : adapter.getSelected())
                            songs.addAll(((EntityKeeper)entityKeeper).getList());
                    else
                        songs.addAll((ArrayList<Song>)adapter.getSelected());

                    Logger.log(songs.size());

                    for(Song song : songs)
                        ViewHolder.this.playlist.addSong(song);

                    adapter.getContext().onBackPressed();
                    fragment.dismiss();
                }
            });
            playlistTitle.setText(playlist.getName());
            tracks.setText(String.valueOf(playlist.getList().size()));

            Glide.with(mainCardView.getContext())
                    .load(art)
                    .apply(new RequestOptions().placeholder(defaultArt))
                    .into(this.art);
        }
    }
}
