package com.example.tolek.player.Util;

import android.os.Environment;

import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class FileWorker {
    static private final String APP_DIRECTORY = Environment.getExternalStorageDirectory()
            + "/Player/";

    static public boolean isFileExist(String name) {
        return new File(APP_DIRECTORY + name).exists();
    }

    static public void writePlaylist(ArrayList<Song> playlist, String name) {
        try (FileWriter fileWriter = new FileWriter(APP_DIRECTORY + "/" + name)) {
            if (playlist != null && playlist.size() != 0)
                fileWriter.write(new GsonBuilder().create().toJson(playlist));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    static private String readFile(File file) throws IOException {
        String stringFile = "";
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((line = bufferedReader.readLine()) != null)
            stringFile += (line);

        return stringFile;
    }

    static private void makeCoverDirectory() {
        try {
            File file = new File(APP_DIRECTORY + "/Cover/");
            if (!file.exists()) {
                file.mkdirs();
                new File(file.getAbsolutePath() + "/.nomedia").createNewFile();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    static public String saveCover(byte[] cover, String name) {
        String path = APP_DIRECTORY + "Cover/" + name + ".jpg";

        if (!new File(path).exists())
            try (FileOutputStream fileWriter = new FileOutputStream(path)) {
                fileWriter.write(cover);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        return path;
    }

    static public void launch() {
        File mainDirectory = new File(APP_DIRECTORY);
        if (!mainDirectory.exists())
            mainDirectory.mkdirs();

        makeCoverDirectory();
    }

    public static ArrayList<Song> getListFromJson(String fileName) {
        ArrayList<Song> songsList = new ArrayList<>();
        try {
            String stringFile = readFile(new File(APP_DIRECTORY + fileName));
            if (!stringFile.equals(""))
                songsList = new Gson().fromJson(stringFile,
                        new TypeToken<ArrayList<Song>>() {
                        }.getType());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return songsList;
    }

    public static ArrayList<Playlist> getCustomPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();

        File directory = new File(APP_DIRECTORY + "Playlists/");

        if (directory.exists()) {
            for (File file : directory.listFiles())
                playlists.add(new Playlist(file.getName(),
                        getListFromJson("Playlists/" + file.getName())));
        } else
            directory.mkdirs();

        return playlists;
    }

    public static void delete(Entity entity) {
        if (entity instanceof Playlist)
            new File(APP_DIRECTORY + "Playlists/" + ((Playlist) entity).getName()).delete();
        else if (entity instanceof Song)
            new File(((Song) entity).getPath()).delete();
    }
}



















