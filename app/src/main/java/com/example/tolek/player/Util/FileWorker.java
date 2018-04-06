package com.example.tolek.player.Util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.tolek.player.Entities.Album;
import com.example.tolek.player.Entities.Artist;
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.Repository.SongsRepository;
import com.example.tolek.player.jaudiotagger.audio.mp3.MP3File;
import com.example.tolek.player.jaudiotagger.tag.FieldKey;
import com.example.tolek.player.jaudiotagger.tag.Tag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public final class FileWorker {
    static private ArrayList<Song> songsList = new ArrayList<>();
    static private ArrayList<Album> albumsList = new ArrayList<>();
    static private ArrayList<Artist> artistsList = new ArrayList<>();
    static private ContentResolver contentResolver;
    static private Cursor audioCursor;
    static private final String MYTAG = "MYTAG";
    static private final String APP_DIRECTORY = Environment.getExternalStorageDirectory()
            + "/Player/";

    static public void setContentResolver(ContentResolver cResolver) {
        contentResolver = cResolver;

        audioCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"*"},
                MediaStore.Audio.Media.IS_MUSIC + " != 0", null, null);
    }

    static public ArrayList<Song> getSongs() {
        return songsList;
    }

    static public ArrayList<Album> getAlbums() {
        return albumsList;
    }

    static public ArrayList<Artist> getArtists() {
        return artistsList;
    }

    static public boolean isFileExist(String name) {
        return new File(APP_DIRECTORY + name + ".txt").exists();
    }

    static public void writePlaylist(ArrayList<Song> playlist, String name) {
        try (FileWriter fileWriter = new FileWriter(APP_DIRECTORY + "/" + name + ".txt")) {
            if (playlist != null && playlist.size() != 0)
                fileWriter.write(new GsonBuilder().create().toJson(playlist));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    private static String readFile(File file) throws IOException {
        String stringFile = "";
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((line = bufferedReader.readLine()) != null)
            stringFile += (line);

        return stringFile;
    }


    static private void loadSettings() {
        //TODO
    }

    static private void loadSongs() {
        try {
            String stringFile = readFile(new File(APP_DIRECTORY + "/songs.txt"));
            if (!stringFile.equals(""))
                songsList = new Gson().fromJson(stringFile,
                        new TypeToken<ArrayList<Song>>() {
                        }.getType());
        } catch (FileNotFoundException exception) {
            //findSongs();
            findSongs(new File("/storage/sdcard1/Music"));
            findSongs(Environment.getExternalStorageDirectory());
            Collections.sort(songsList, new Comparator<Song>() {
                @Override
                public int compare(Song song, Song song1) {
                    return song.getTitle().compareTo(song1.getTitle());
                }
            });
            writePlaylist(songsList, "songs");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    static private void loadLastSongs() {
        try {
            String stringFile = readFile(new File(APP_DIRECTORY + "/lastSongs.txt"));
            if (!stringFile.equals(""))
                Player.getInstance().setLastSongs((ArrayList<Song>) new Gson().fromJson(stringFile,
                        new TypeToken<ArrayList<Song>>() {
                        }.getType()));
        } catch (FileNotFoundException exception) {
            //TODO
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        /*
        File file = new File(APP_DIRECTORY + "/lastSongs.txt");
        if(file.exists()){
            try {

            } catch(Exception exception){
                exception.printStackTrace();
            }

        }*/
    }

    static private void loadCurrentPlaylist() {
        try {
            String stringFile = readFile(new File(APP_DIRECTORY + "/currentPlaylist.txt"));
            if (!stringFile.equals(""))
                Player.getInstance().setPlaylist((ArrayList<Song>) new Gson().fromJson(stringFile,
                        new TypeToken<ArrayList<Song>>() {
                        }.getType()));
        } catch (IOException exception) {
//            Player.getInstance().setPlaylist(songsList);
        }
    }

    static public void launch() {
        File mainDirectory = new File(APP_DIRECTORY);

        if (!mainDirectory.exists())
            mainDirectory.mkdirs();

        loadSettings();
//        loadSongs();
        SongsRepository.getInstance().initialize();
        songsList = SongsRepository.getInstance().getSongs();
        loadLastSongs();
        loadCurrentPlaylist();

        initializeAlbums();
        initializeArtists();

        Collections.sort(albumsList, new Comparator<Album>() {
            @Override
            public int compare(Album album, Album album1) {
                return album.getAlbumName().compareTo(album1.getAlbumName());
            }
        });

        Collections.sort(artistsList, new Comparator<Artist>() {
            @Override
            public int compare(Artist artist, Artist artist1) {
                return artist.getName().compareTo(artist.getName());
            }
        });
    }

    static private void findSongs() {
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    String path = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA)
                    );

                    Cursor albumCursor = contentResolver.query(
                            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Audio.Albums._ID,
                                    MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID + "=?",
                            new String[]{String.valueOf(
                                    audioCursor.getString(
                                            audioCursor.getColumnIndex(MediaStore.Audio
                                                    .Media.ALBUM_ID)
                                    )
                            )},
                            null);

                    String albumArtPath = "";
                    if (albumCursor.moveToFirst())
                        albumArtPath = albumCursor.getString(
                                albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
                        );

                    songsList.add(new Song(
                            audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.TRACK)),
                            audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                            path,
                            audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                            albumArtPath
                            )
                    );
                } while (audioCursor.moveToNext());
            }

        }
    }

    static private void findSongs(File file) {
        if (file.isDirectory()) {
            if (file.length() == 0)
                return;
           /* for(File a : file.listFiles())
                if(a.length() == 0 && a.getName().equals(".nomedia"))
                    return;*/

            else if (Arrays.asList(file.list()).contains(".nomedia"))// || file.getName().equals("Music"))
                return;

            for (File file1 : file.listFiles())
                if (!file1.isHidden())
                    findSongs(file1);
        } else if (file.isFile() && file.getAbsolutePath().endsWith(".mp3")) {
            try {
                MP3File mp3File = new MP3File(file);
                if (mp3File.hasID3v1Tag() || mp3File.hasID3v2Tag()) {
                    Tag tag = mp3File.getTag();

                    String artist = tag.getFirst(FieldKey.ARTIST);
                    String album = tag.getFirst(FieldKey.ALBUM);
                    String artPath = "";

                    File cover = new File(APP_DIRECTORY + "/Cover/");

                    if (!cover.exists()) {
                        if (cover.mkdirs()) {
                            File nomedia = new File(cover.getAbsolutePath() + "/.nomedia");
                            nomedia.createNewFile();
                        }
                    }

                    cover = new File(APP_DIRECTORY + "/Cover/"
                            + artist + "-" + album + ".jpg");

                    if (cover.exists())
                        artPath = cover.getAbsolutePath();
                    else if (tag.getArtworkList().size() > 0) {
                        artPath = cover.getAbsolutePath();
                        try (FileOutputStream fileWriter = new FileOutputStream(cover)) {
                            fileWriter.write(tag.getArtworkList().get(0).getBinaryData());
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }

                    songsList.add(new Song(
                            tag.getFirst(FieldKey.TRACK),
                            artist,
                            tag.getFirst(FieldKey.TITLE),
                            album,
                            file.getAbsolutePath(),
                            String.valueOf(mp3File.getMP3AudioHeader().getTrackLength() * 1000),
                            artPath
                    ));

                } else {
                    songsList.add(new Song(
                            null, null, null, null,
                            file.getAbsolutePath(),
                            String.valueOf(mp3File.getMP3AudioHeader().getTrackLength() * 1000),
                            null
                    ));
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
     /*
            MediaMetadataRetriever metadata = new MediaMetadataRetriever();
            metadata.setDataSource(file.getAbsolutePath());
            String albumPath = "";
            byte[] art = metadata.getEmbeddedPicture();

            if(art != null){
                albumPath = APP_DIRECTORY + "Cover/";

                if(!new File(albumPath).exists())
                    new File(albumPath).mkdirs();

                albumPath += metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                        + ".jpg";

                try(FileOutputStream fileWriter = new FileOutputStream(albumPath)) {
                    fileWriter.write(art);
                }catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                File cover = new File(APP_DIRECTORY + "Cover/"
                        + metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                        + ".jpg"
                );
                if(cover.exists())
                    albumPath = cover.getAbsolutePath();
            }

            try{
                MP3File file1 = new MP3File(file);
                if((file1.hasID3v2Tag() || file1.hasID3v1Tag())
                        && file1.getTag().getArtworkList().size() > 0)
                    a++;
                else
                    Log.d(MYTAG, file.getAbsolutePath());
            } catch(Exception exception){
                Log.d("MYTAG", "EXCEPTION");
                exception.printStackTrace();
            }
            songsList.add(new Song(
                    "",
                    metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                    metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                    metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                    file.getAbsolutePath(),
                    metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION),
                    albumPath
                    )
            );*/
        }
    }

    static private void initializeArtists() {
        for (Song song : songsList) {
            boolean isContains = false;
            for (Artist artist : artistsList)
                if (song.getArtist().equals("Unknown artist")) {
                    if (artist.getName().equals("Unknown artist")) {
                        artist.addSong(song);
                        isContains = true;
                        break;
                    }
                } else if (artist.getName().equals(song.getArtist())) {
                    artist.addSong(song);
                    isContains = true;
                    break;
                }

            if (!isContains)
                artistsList.add(new Artist(song));
        }
    }

    static private void initializeAlbums() {
        for (Song song : songsList) {
            boolean isContains = false;
            for (Album album : albumsList)
                if (song.getAlbum().equals("Unknown album")) {
                    if (album.getAlbumName().equals("Unknown album")) {
                        album.addSong(song);
                        isContains = true;
                        break;
                    }
                } else if (album.getArtist().equals(song.getArtist()) && album.getAlbumName().equals(song.getAlbum())) {
                    album.addSong(song);
                    isContains = true;
                    break;
                }

            if (!isContains) {
                Album album = new Album(song.getAlbum(),
                        song.getAlbumArt(),
                        song.getAlbum().equals("Unknown album") ? "" : song.getArtist());
                album.addSong(song);
                albumsList.add(album);
            }
        }
    }

    public static ArrayList<Song> getListFromJson(String fileName) {
        ArrayList<Song> songsList = new ArrayList<>();
        try {
            String stringFile = readFile(new File(APP_DIRECTORY + fileName + "txt"));
            if (!stringFile.equals(""))
                songsList = new Gson().fromJson(stringFile,
                        new TypeToken<ArrayList<Song>>(){}.getType());
        } catch (IOException exception){
            exception.printStackTrace();
        }
        return songsList;
    }
}



















