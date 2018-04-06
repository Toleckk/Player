package com.example.tolek.player.debug;

import android.os.Environment;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.jaudiotagger.audio.mp3.MP3File;
import com.example.tolek.player.jaudiotagger.tag.FieldKey;
import com.example.tolek.player.jaudiotagger.tag.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.example.tolek.player.Util.FileWorker.writePlaylist;

public class MediaSearcher {

    private class SongBuilder{
        private String id;
        private String artist;
        private String title;
        private String album;
        private String path;
        private String duration;

        public SongBuilder(){}

        public SongBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public SongBuilder setArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public SongBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SongBuilder setAlbum(String album) {
            this.album = album;
            return this;
        }

        public SongBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public SongBuilder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public Song build(){
            return new Song(id, artist, title, album, path, duration);
        }
    }

    private ArrayList<Song> songsList;

    public MediaSearcher() {
        songsList = new ArrayList<>();
    }

    public ArrayList<Song> findSongs() {
        if(FileWorker.isFileExist("songs")){
            songsList = FileWorker.getListFromJson("songs");
        } else {
            findSongs(Environment.getExternalStorageDirectory());
            findSongs(new File("/storage/sdcard1/Music"));
            Collections.sort(songsList, new Comparator<Song>() {
                @Override
                public int compare(Song song, Song song1) {
                    return song.getTitle().compareTo(song1.getTitle());
                }
            });
            writePlaylist(songsList, "songs");
        }

        return songsList;
    }
/*
    private String makeCover(Tag tag) throws IOException{
        String artPath = "";

        File cover = new File(APP_DIRECTORY + "/Cover/");

        if (!cover.exists()) {
            if (cover.mkdirs()) {
                File nomedia = new File(cover.getAbsolutePath() + "/.nomedia");
                nomedia.createNewFile();
            }
        }

        cover = new File(APP_DIRECTORY + "/Cover/"
                + tag.getFirst(FieldKey.ARTIST) + "-" + tag.getFirst(FieldKey.ALBUM) + ".jpg");

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
        return artPath;
    }
*/
    private SongBuilder setTags(SongBuilder songBuilder, Tag tag){
        return songBuilder
                .setId(tag.getFirst(FieldKey.TRACK))
                .setArtist(tag.getFirst(FieldKey.ARTIST))
                .setTitle(tag.getFirst(FieldKey.TITLE))
                .setAlbum(tag.getFirst(FieldKey.ALBUM));
    }

    private Song makeSong(MP3File file) throws IOException{
        SongBuilder songBuilder = new SongBuilder()
                .setDuration(String.valueOf(file.getMP3AudioHeader().getTrackLength() * 1000))
                .setPath(file.getFile().getAbsolutePath());

        if (file.hasID3v1Tag() || file.hasID3v2Tag())
            setTags(songBuilder, file.getTag());

        return songBuilder.build();
    }

    private void findSongs(File file) {
        if (file.isDirectory()) {
            if (file.length() == 0)
                return;

            else if (Arrays.asList(file.list()).contains(".nomedia"))
                return;

            for (File file1 : file.listFiles())
                if (!file1.isHidden())
                    findSongs(file1);
        } else if (file.isFile() && file.getAbsolutePath().endsWith(".mp3")) {
            try {
                songsList.add(makeSong(new MP3File(file)));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
