package com.example.tolek.player.domain;


import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.gui.MainActivity.BottomViewHolder;
import com.example.tolek.player.gui.PlayerActivity.PlayerViewHolder;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Player {
    static private Player instance;

    private MediaPlayer mediaPlayer;
    private Playlist lastSongs;
    private Playlist currentPlaylist;
    private Song currentSong;
    private Random random;
    private SharedPreferences preferences;

    private PlayerViewHolder playerViewHolder;
    private BottomViewHolder bottomViewHolder;

    private PlayThread playThread;

    private int mode;
    private int currentPosition;


    static public Player getInstance() {
        if(instance == null)
            instance = new Player();

        return instance;
    }

    private Player() {
        mediaPlayer = new MediaPlayer();
        random = new Random(System.currentTimeMillis());
        mode = -1;

        setCurrentPlaylist(new Playlist(FileWorker.getListFromJson("currentPlaylist")));
        setLastSongs(new Playlist(FileWorker.getListFromJson("lastSongs")));


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (playerViewHolder != null && playerViewHolder.getSeekBar() != null
                        && !playerViewHolder.getSeekBar().isPressed()
                        && mediaPlayer != null)
                    playerViewHolder.getSeekBar().setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 500, 500);
    }

    public void play(Song song) {
        if ((playThread == null || !playThread.isAlive())
                && (playerViewHolder == null || playerViewHolder.isUiThreadDone())) {
            playThread = new PlayThread(song);
            playThread.start();
        }
    }

    public void play() {
        if (currentSong != null) {
            if (playerViewHolder != null && playerViewHolder.getSeekBar() != null)
                playerViewHolder.getSeekBar().setMax(mediaPlayer.getDuration());

            mediaPlayer.start();

            if(bottomViewHolder != null)
                bottomViewHolder.setPlayImage(R.drawable.ic_pause_black_40dp);

            if(playerViewHolder != null)
                playerViewHolder.setPlayImage(R.drawable.ic_pause_black_70dp);
        }
    }

    public void pause() {
        try {
            bottomViewHolder.setPlayImage(R.drawable.ic_play_arrow_black_40dp);
            playerViewHolder.setPlayImage(R.drawable.ic_play_arrow_black_70dp);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }

        mediaPlayer.pause();
    }

    public void next() {
        if ((playThread == null || !playThread.isAlive())
                && (playerViewHolder == null || playerViewHolder.isUiThreadDone()))
            switch (mode) {
                case 0:
                    int index = currentPlaylist.getList().indexOf(currentSong);
                    play(currentPlaylist.getList()
                            .get(index == currentPlaylist.getList().size() - 1 ? 0 : index + 1));
                    break;
                case 1:
                    prepareMediaPlayer(currentSong);
                    play(currentSong);
                    break;
                case 2:
                    if (currentPosition < lastSongs.getList().size() - 1)
                        play(lastSongs.getList().get(++currentPosition));
                    else {
                        currentPosition++;
                        play(currentPlaylist.getList()
                                .get(Math.abs(random.nextInt() % currentPlaylist.getList().size())));
                    }
                    break;
            }
    }

    public void previous() {
        if ((playThread == null || !playThread.isAlive())
                && (playerViewHolder == null || playerViewHolder.isUiThreadDone()))
            switch (mode) {
                case 0:
                    int index = currentPlaylist.getList().indexOf(currentSong);
                    play(currentPlaylist.getList().get(index == 0 ? currentPlaylist.getList().size() - 1 : index - 1));
                    currentPosition = currentPosition - 2;
                    break;
                case 1:
                    prepareMediaPlayer(currentSong);
                    play(currentSong);
                    break;
                case 2:
                    play(lastSongs.getList().get(--currentPosition));
                    break;
            }
    }

    private void prepareMediaPlayer(Song song) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public int getMode() {
        return mode;
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }


    public int changeMode() {
        mode = (mode == 2) ? 0 : mode + 1;

        if(preferences != null)
            preferences.edit().putInt("mode", mode).apply();

        return mode;
    }

    public void setCurrentPosition(int currentPosition) {
        if (mediaPlayer != null)
            mediaPlayer.seekTo(currentPosition);
    }

    public void setCurrentPlaylist(Playlist currentPlaylist) {
        if (currentPlaylist != null && currentPlaylist.getList().size() > 0)
            this.currentPlaylist = currentPlaylist;
        else
            this.currentPlaylist = new Playlist(MediaStore.getSongs().getList());

        if(this.currentPlaylist != null)
            FileWorker.writePlaylist(currentPlaylist.getList(), "currentPlaylist");
        else
            this.currentPlaylist = new Playlist("");
    }

    public void setLastSongs(Playlist lastSongs) {
        if (lastSongs != null && lastSongs.getList().size() > 0) {
            currentPosition = lastSongs.getList().size();
            this.lastSongs = lastSongs;
            currentSong = lastSongs.getList().get(lastSongs.getList().size() - 1);
        } else if (currentPlaylist != null && currentPlaylist.getList().size() > 0 && currentSong == null) {
            this.lastSongs = new Playlist("");
            currentSong = currentPlaylist.getList().get(0);
            currentPosition = 0;
        }

        prepareMediaPlayer(currentSong);
    }

    public void setPlayerViewHolder(PlayerViewHolder playerPlayerViewHolder) {
        this.playerViewHolder = playerPlayerViewHolder;
    }

    public void setBottomViewHolder(BottomViewHolder bottomViewHolder) {
        this.bottomViewHolder = bottomViewHolder;
    }

    public void play(Playlist playlist) {
        if(playlist != null && playlist.getList().size() > 0) {
            currentPlaylist = playlist;
            play(playlist.getList().get(0));
        }
    }


    public void setPreferences(SharedPreferences preferences){
        if(mode == -1)
            mode = preferences.getInt("mode", -1);
        if (mode == -1)
            preferences.edit().putInt("mode", (mode = 0)).apply();

        this.preferences = preferences;
    }


    private class PlayThread extends Thread {
        public PlayThread(final Song song) {
            super(new Runnable() {
                @Override
                public void run() {
                    if (currentSong.equals(song))
                        play();
                    else {
                        if (mode == 0)
                            currentPosition++;

                        if (currentPosition >= lastSongs.getList().size() && mode != 1)
                            lastSongs.addSong(song);

                        prepareMediaPlayer(song);
                        currentSong = song;

                        if(playerViewHolder != null)
                            playerViewHolder.setSong(song);

                        if(bottomViewHolder != null)
                            bottomViewHolder.setSong(song);


                        FileWorker.writePlaylist(lastSongs.getList(), "lastSongs");
                        play();
                    }
                }
            });
        }

    }
}
