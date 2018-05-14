package com.example.tolek.player.domain;


import com.example.tolek.player.Repository.Repository;
import com.example.tolek.player.domain.Entities.Album;
import com.example.tolek.player.domain.Entities.Artist;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.Repository.PlaylistsRepository;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.Util.jaudiotagger.audio.mp3.MP3File;
import com.example.tolek.player.Util.jaudiotagger.tag.FieldKey;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.tolek.player.Util.FileWorker.writePlaylist;

public final class MediaStore {
    static private MediaStore instance = new MediaStore();

    private Repository<Song> songs;
    private Repository<Album> albums;
    private Repository<Artist> artists;
    private Repository<Playlist> playlists;

    static private MediaStore getInstance() {
        return instance;
    }

    private MediaStore() {
        songs = new Repository<>((!FileWorker.isFileExist("songs"))
                ? new MediaSearcher().findSongs()
                : FileWorker.getListFromJson("songs"));
        this.albums = new Repository<>(makeAlbums());
        this.artists = new Repository<>(makeArtists());
        this.playlists = new PlaylistsRepository(FileWorker.getCustomPlaylists());
        writePlaylist(songs.getList(), "songs");
    }


    private ArrayList<Artist> makeArtists() {
        ArrayList<Artist> artists = new ArrayList<>();
        for (Song song : songs.getList()) {
            boolean isContains = false;
            for (Artist artist : artists)
                if (song.getArtist().equals("Unknown artist")) {
                    if (artist.getName().equals("Unknown artist")) {
                        artist.addSong(song);
                        isContains = true;
                        break;
                    }
                } else if (artist.getName().toLowerCase().equals(song.getArtist().toLowerCase())) {
                    artist.addSong(song);
                    isContains = true;
                    break;
                }

            if (!isContains)
                artists.add(new Artist(song));
        }

        Collections.sort(artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist artist, Artist artist1) {
                return artist.getName().compareTo(artist.getName());
            }
        });

        return artists;
    }

    private ArrayList<Album> makeAlbums() {
        ArrayList<Album> albums = new ArrayList<>();

        for (Song song : songs.getList()) {
            boolean isContains = false;
            for (Album album : albums)
                if (song.getAlbum().equals("Unknown album")) {
                    if (album.getAlbumName().equals("Unknown album")) {
                        album.addSong(song);
                        isContains = true;
                        break;
                    }
                } else if (album.getArtist().toLowerCase().equals(song.getArtist().toLowerCase())
                        && album.getAlbumName().toLowerCase().equals(song.getAlbum().toLowerCase())) {
                    album.addSong(song);
                    isContains = true;
                    break;
                }

            if (!isContains) {
                Album album = new Album(song.getAlbum(),
                        song.getAlbum().equals("Unknown album") ? "" : song.getArtist());
                album.addSong(song);
                albums.add(album);
            }
        }

        for(Album album : albums)
            album.setCovers();

        Collections.sort(albums, new Comparator<Album>() {
            @Override
            public int compare(Album album, Album album1) {
                return album.getAlbumName().compareTo(album1.getAlbumName());
            }
        });

        return albums;
    }


    public static String getText(Song song){
        if(song != null) {
            try {
                MP3File file = new MP3File(song.getPath());
                return (file.hasID3v1Tag() || file.hasID3v2Tag())
                        ? file.getTag().getFirst(FieldKey.LYRICS)
                        : null;
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static Repository<Song> getSongs() {
        return getInstance().songs;
    }

    public static Repository<Album> getAlbums() {
        return getInstance().albums;
    }

    public static Repository<Artist> getArtists() {
        return getInstance().artists;
    }

    public static PlaylistsRepository getPlaylists(){
        return (PlaylistsRepository) getInstance().playlists;
    }

}
