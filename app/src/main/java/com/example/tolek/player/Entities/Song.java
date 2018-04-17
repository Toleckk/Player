package com.example.tolek.player.Entities;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.R;
import com.example.tolek.player.debug.Player;

import java.util.ArrayList;

final public class Song extends Entity{
    private String id;
    private String artist;
    private String title;
    private String album;
    private String path;
    private String duration;
    private String albumArt;

    @Override
    public ViewHolder createViewHolder(RecyclerView.ViewHolder v, Drawable art, ArrayList list) {
        ViewHolder holder = (ViewHolder) v;

        final ArrayList<Song> songs = list;

        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = Player.getInstance();
                if (player.getCurrentSong() != null && player.getCurrentSong().equals(Song.this) && player.isPlaying())
                    player.pause();
                else {
                    if (!player.getCurrentPlaylist().equals(songs))
                        player.setCurrentPlaylist(new ArrayList<>(songs));

                    player.play(Song.this);
                }
            }
        });

        holder.title.setText(getTitle());
        holder.artistAndAlbum.setText(getArtist() +
                " — " + getAlbum());

        Glide.with(holder.mainCardView.getContext())
                .load(getAlbumArt())
                .apply(new RequestOptions().placeholder(art))
                .into(holder.albumArt);

        holder.playlist = songs;
        return holder;
    }

    @Override
    public boolean checkQuery(String query) {
        query = query.toLowerCase();
        return title.toLowerCase().contains(query)
                || album.toLowerCase().contains(query)
                || artist.toLowerCase().contains(query);
    }

    public static class Builder {
        private String id;
        private String artist;
        private String title;
        private String album;
        private String path;
        private String duration;
        private String cover;

        public Builder(String path, String duration) {
            this.path = path;
            this.duration = duration;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setAlbum(String album) {
            this.album = album;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public Builder setCover(String cover) {
            this.cover = cover;
            return this;
        }

        public Song build() {
            Song song = new Song(id, artist, title, album, path, duration);
            if (cover != null)
                song.setCover(cover);
            return song;
        }
    }

    public Song(String id, String artist, String title, String album, String path, String duration) {
        this.id = id;
        this.artist = artist == null || artist.equals("<unknown>") || artist.equals("")
                ? "Unknown artist"
                : artist;
        this.title = title == null || title.equals("<unknown>") || title.equals("")
                ? path.split("/")[path.split("/").length - 1]
                : title;
        this.album = album == null || album.equals("<unknown>") || album.equals("")
                ? "Unknown album"
                : album;
        this.path = path;
        this.duration = duration;
    }

    public void setCover(String albumArt) {
        this.albumArt = albumArt == null || albumArt.equals("") ? null : albumArt;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getPath() {
        return path;
    }

    public String getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (id != null ? !id.equals(song.id) : song.id != null) return false;
        if (artist != null ? !artist.equals(song.artist) : song.artist != null) return false;
        if (title != null ? !title.equals(song.title) : song.title != null) return false;
        if (album != null ? !album.equals(song.album) : song.album != null) return false;
        if (path != null ? !path.equals(song.path) : song.path != null) return false;
        if (duration != null ? !duration.equals(song.duration) : song.duration != null)
            return false;
        return albumArt != null ? albumArt.equals(song.albumArt) : song.albumArt == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (albumArt != null ? albumArt.hashCode() : 0);
        return result;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mainCardView;
        public ImageView albumArt;
        public TextView title;
        public TextView artistAndAlbum;
        public ArrayList<Song> playlist;


        public ViewHolder(View itemView) {
            super(itemView);
            mainCardView = itemView.findViewById(R.id.songCardView);
            albumArt = itemView.findViewById(R.id.song_art);
            title = itemView.findViewById(R.id.song_title);
            artistAndAlbum = itemView.findViewById(R.id.song_artist_and_album);
        }
    }
}
