package com.example.tolek.player.debug;


import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.tolek.player.Entities.Album;
import com.example.tolek.player.Entities.Artist;
import com.example.tolek.player.Entities.Entity;
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.R;
import com.example.tolek.player.Repository.AlbumsRepository;
import com.example.tolek.player.Repository.ArtistsRepository;
import com.example.tolek.player.Repository.SongsRepository;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.jaudiotagger.audio.mp3.MP3File;
import com.example.tolek.player.jaudiotagger.tag.FieldKey;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.tolek.player.Util.FileWorker.writePlaylist;

public final class MediaStore {
    static private MediaStore instance = new MediaStore();

    private SongsRepository songs;
    private AlbumsRepository albums;
    private ArtistsRepository artists;

    static public MediaStore getInstance() {
        return instance;
    }

    private MediaStore() {
        if (!FileWorker.isFileExist("songs"))
            songs = new SongsRepository(new MediaSearcher().findSongs());
        else
            songs = new SongsRepository(FileWorker.getListFromJson("songs"));

        this.albums = new AlbumsRepository(makeAlbums());
        this.artists = new ArtistsRepository(makeArtists());
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


    public String getText(Song song){
        if(song != null) {
            try {
                MP3File file = new MP3File(song.getPath());
                return file.hasID3v1Tag() || file.hasID3v2Tag()
                        ? file.getTag().getFirst(FieldKey.LYRICS)
                        : null;
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Song> getSongs() {
        return songs.getList();
    }

    public ArrayList<Album> getAlbums() {
        return albums.getList();
    }

    public ArrayList<Artist> getArtists() {
        return artists.getList();
    }

    public ArrayList<Entity> getSongsAsEntities() {
        ArrayList<Entity> list = new ArrayList<>();
        for(Song song : songs.getList())
            list.add(song);

        return list;
    }
}
