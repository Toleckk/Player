package com.example.tolek.player.Entities;

/**
 * Created by Tolek on 26.02.2018.
 */

final public class Song {
    private String id;
    private String artist;
    private String title;
    private String album;
    private String path;
    private String duration;
    private String albumArt;
    public Long lastPosition = null;


    public Song(String id, String artist, String title,
                String album, String path, String duration, String albumArt){
        this.id = id;
        this.artist = artist == null || artist.equals("<unknown>") || artist.equals("")
                ? "Unknown artist"
                : artist;
        this.title = title == null || title.equals("<unknown>") || title.equals("")
                ? path.split("/")[path.split("/").length-1]
                : title;
        this.album = album == null || album.equals("<unknown>") || album.equals("")
                ? "Unknown album"
                : album;
        this.path = path;
        this.duration = duration;
        this.albumArt = albumArt == null || albumArt.equals("") ? null : albumArt;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getPath(){
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
}
