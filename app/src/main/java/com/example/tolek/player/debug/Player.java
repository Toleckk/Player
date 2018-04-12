package com.example.tolek.player.debug;


import android.media.MediaPlayer;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.MainActivity.BottomViewHolder;
import com.example.tolek.player.PlayerActivity.PlayerViewHolder;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Player {
    static private Player instance = new Player();

    private MediaPlayer mediaPlayer;
    private ArrayList<Song> lastSongs;
    private ArrayList<Song> currentPlaylist;
    private Song currentSong;
    private Random random;

    private PlayerViewHolder playerViewHolder;
    private BottomViewHolder bottomViewHolder;

    private PlayThread playThread;

    private int mode;
    private int currentPosition;


    static public Player getInstance() {
        return instance;
    }

    private Player() {
        mediaPlayer = new MediaPlayer();
        random = new Random(System.currentTimeMillis());
        //TODO
        mode = 0;

        setCurrentPlaylist(FileWorker.getListFromJson("currentPlaylist"));
        setLastSongs(FileWorker.getListFromJson("lastSongs"));


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
                    int index = currentPlaylist.indexOf(currentSong);
                    play(currentPlaylist.get(index == currentPlaylist.size() - 1 ? 0 : index + 1));
                    break;
                case 1:
                    prepareMediaPlayer(currentSong);
                    play(currentSong);
                    break;
                case 2:
                    if (currentPosition < lastSongs.size() - 1)
                        play(lastSongs.get(++currentPosition));
                    else {
                        currentPosition++;
                        play(currentPlaylist.get(Math.abs(random.nextInt() % currentPlaylist.size())));
                    }
                    break;
            }
    }

    public void previous() {
        if ((playThread == null || !playThread.isAlive())
                && (playerViewHolder == null || playerViewHolder.isUiThreadDone()))
            switch (mode) {
                case 0:
                    int index = currentPlaylist.indexOf(currentSong);
                    play(currentPlaylist.get(index == 0 ? currentPlaylist.size() - 1 : index - 1));
                    currentPosition = currentPosition - 2;
                    break;
                case 1:
                    prepareMediaPlayer(currentSong);
                    play(currentSong);
                    break;
                case 2:
                    play(lastSongs.get(--currentPosition));
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

    public ArrayList<Song> getCurrentPlaylist() {
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


    public void changeMode() {
        mode = mode == 2 ? 0 : mode + 1;
    }

    public void setCurrentPosition(int currentPosition) {
        if (mediaPlayer != null)
            mediaPlayer.seekTo(currentPosition);
    }

    public void setCurrentPlaylist(ArrayList<Song> currentPlaylist) {
        if (currentPlaylist != null && currentPlaylist.size() > 0)
            this.currentPlaylist = currentPlaylist;
        else
            this.currentPlaylist = MediaStore.getInstance().getSongs();

        if(this.currentPlaylist != null)
            FileWorker.writePlaylist(currentPlaylist, "currentPlaylist");
        else
            this.currentPlaylist = new ArrayList<>();
    }

    public void setLastSongs(ArrayList<Song> lastSongs) {
        if (lastSongs != null && lastSongs.size() > 0) {
            currentPosition = lastSongs.size();
            this.lastSongs = lastSongs;
            currentSong = lastSongs.get(lastSongs.size() - 1);
        } else if (currentPlaylist != null && currentPlaylist.size() > 0 && currentSong == null) {
            this.lastSongs = new ArrayList<>();
            currentSong = currentPlaylist.get(0);
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

                        if (currentPosition >= lastSongs.size() && mode != 1)
                            lastSongs.add(song);

                        prepareMediaPlayer(song);
                        currentSong = song;

                        if(playerViewHolder != null)
                            playerViewHolder.setSong(song);

                        if(bottomViewHolder != null)
                            bottomViewHolder.setSong(song);


                        FileWorker.writePlaylist(lastSongs, "lastSongs");
                        play();
                    }
                }
            });
        }

    }
}
