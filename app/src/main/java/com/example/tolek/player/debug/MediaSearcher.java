package com.example.tolek.player.debug;

import android.os.Environment;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.jaudiotagger.audio.mp3.MP3File;
import com.example.tolek.player.jaudiotagger.tag.FieldKey;
import com.example.tolek.player.jaudiotagger.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.example.tolek.player.Util.FileWorker.saveCover;

public class MediaSearcher {

    private ArrayList<Song> songsList;

    public MediaSearcher() {
        songsList = new ArrayList<>();
    }

    public ArrayList<Song> findSongs() {
        findSongs(Environment.getExternalStorageDirectory());
        findSongs(new File("/storage/sdcard1/Music"));
        Collections.sort(songsList, new Comparator<Song>() {
            @Override
            public int compare(Song song, Song song1) {
                return song.getTitle().compareTo(song1.getTitle());
            }
        });

        return songsList;
    }

    private void setTags(Song.Builder builder, Tag tag) {
        builder
                .setId(tag.getFirst(FieldKey.TRACK))
                .setArtist(tag.getFirst(FieldKey.ARTIST))
                .setTitle(tag.getFirst(FieldKey.TITLE))
                .setAlbum(tag.getFirst(FieldKey.ALBUM));
        if(tag.getArtworkList().size() > 0)
            builder.setCover(saveCover(tag.getArtworkList().get(0).getBinaryData(),
                    tag.getFirst(FieldKey.ARTIST) + " — " + tag.getFirst(FieldKey.ALBUM)));
    }

    private Song makeSong(MP3File file) throws IOException{
        Song.Builder builder = new Song.Builder(
                file.getFile().getAbsolutePath(),
                String.valueOf(file.getMP3AudioHeader().getTrackLength() * 1000)
        );

        if (file.hasID3v1Tag() || file.hasID3v2Tag())
            setTags(builder, file.getTag());

        return builder.build();
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