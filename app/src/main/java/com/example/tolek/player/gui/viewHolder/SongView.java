package com.example.tolek.player.gui.viewHolder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.R;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.RVAdapter;

import java.util.ArrayList;


public class SongView extends CheckableViewHolder {
    public static Drawable defaultArt;
    private static int layoutID;

    public ImageView albumArt;
    public TextView title;
    public TextView album;
    public TextView artist;
    public ArrayList<Song> playlist;
    private Song song;

    public SongView(View itemView, RVAdapter adapter) {
        super(itemView, (CardView) itemView.findViewById(R.id.songCardView), adapter);

        albumArt = itemView.findViewById(R.id.song_art);
        title = itemView.findViewById(R.id.song_title);
        album = itemView.findViewById(R.id.album);
        artist = itemView.findViewById(R.id.artist);
    }


    @Override
    public int getLayoutID() {
        return layoutID;
    }

    @Override
    public Drawable getDefaultArt() {
        return defaultArt;
    }

    @Override
    public void fillFromEntity(Entity entity, ArrayList list, boolean checkable,
                               boolean isChecked, View.OnClickListener checkBoxListener) {
        final ArrayList<Song> songs = list;
        song = (Song) entity;

        if (!checkable)
            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player player = Player.getInstance();
                    if (player.getCurrentSong() != null && player.getCurrentSong().equals(song) && player.isPlaying())
                        player.pause();
                    else {
                        if (!player.getCurrentPlaylist().equals(songs))
                            player.setCurrentPlaylist(new Playlist(songs));

                        player.play(song);
                    }
                }
            });
        else
            mainCardView.setOnClickListener(checkBoxListener);

        title.setText(song.getTitle());
        album.setText(song.getAlbum());
        artist.setText(song.getArtist());

        Glide.with(mainCardView.getContext())
                .load(song.getAlbumArt())
                .apply(new RequestOptions().placeholder(defaultArt))
                .into(albumArt);

        playlist = songs;

        checkBox.setVisibility(checkable ? View.VISIBLE : View.GONE);
        checkBox.setImageResource(isChecked
                ? R.drawable.ic_check_circle_black_40dp
                : R.drawable.ic_radio_button_unchecked_black_24dp);
        checkBox.setOnClickListener(checkBoxListener);
    }
}