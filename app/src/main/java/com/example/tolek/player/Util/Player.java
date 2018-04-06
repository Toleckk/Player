package com.example.tolek.player.Util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.PlayerActivity.PlayerActivity;
import com.example.tolek.player.R;
import com.example.tolek.player.debug.PlayerViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Player {
    private static final Player ourInstance = new Player();
    private static final String MYTAG = "MYTAG";
    private final int MAX_PLAYLIST_SIZE = 1000;
    private MediaPlayer mediaPlayer;
    private Song currentSong;
    private ArrayList<Song> lastSongs;
    private ArrayList<Song> currentPlaylist;
    private BottomViewHolder bottomViewHolder;
    private PlayerViewHolder playerViewHolder;

    private Activity context;
    private volatile boolean finished = true;
    private volatile boolean playFinished = true;
    private volatile int mode = 0;
    private int currentPosition = -1;

    static public Player getInstance() {
        return ourInstance;
    }

    private Player() {
        mediaPlayer = new MediaPlayer();
        lastSongs = new ArrayList<>();
        currentPlaylist = new ArrayList<>();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(currentSong != null
                        && currentSong.lastPosition != null
                        && currentSong.lastPosition != (long)mediaPlayer.getCurrentPosition()) {
                    currentSong.lastPosition = (long) mediaPlayer.getCurrentPosition();
                    FileWorker.writePlaylist(lastSongs, "lastSongs");
                }
            }
        }, 5000, 5000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(playerViewHolder != null && playerViewHolder.getSeekBar() != null
                        && !playerViewHolder.getSeekBar().isPressed()
                        && mediaPlayer != null)
                    playerViewHolder.getSeekBar().setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 500, 500);
    }

    @Nullable
    public Song getCurrentSong(){
        return currentSong;
    }

    public int getMode() {
        return mode;
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public ArrayList<Song> getCurrentPlaylist() {
        return currentPlaylist;
    }


    public void setPlayerViewHolder(View view){
        playerViewHolder = new PlayerViewHolder(view);
    }

    public void setCurrentPosition(int progress) {
        if(mediaPlayer != null)
            mediaPlayer.seekTo(progress);
    }

    public void setContext(Activity context){
        this.context = context;
    }

    public void setBottomViewHolder(){
        bottomViewHolder = new BottomViewHolder(context);
    }

    public void setPlaylist(ArrayList<Song> playlist){
        currentPlaylist = playlist;
        FileWorker.writePlaylist(playlist, "currentPlaylist");
    }

    public void setLastSongs(ArrayList<Song> lastSongs){
        currentPosition = lastSongs.size() - 1;
        this.lastSongs = lastSongs;
        currentSong = lastSongs.get(lastSongs.size()-1);
        if(currentSong != null)
            try {
                mediaPlayer.setDataSource(currentSong.getPath());
                mediaPlayer.prepare();
                mediaPlayer.seekTo(currentSong.lastPosition == null
                        ? 0
                        : currentSong.lastPosition.intValue()
                );
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bottomViewHolder.setSong(currentSong);
                    }
                });
            } catch (IOException exception) {
                exception.printStackTrace();
            }
    }

    public void changeMode() {
        mode = mode == 2 ? 0 : mode+1;
    }


    public boolean isPlaying(){
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    private void prepareMediaPlayer(Song song) throws IOException{
        mediaPlayer.reset();

        if (currentPlaylist.size() == MAX_PLAYLIST_SIZE)
            currentPlaylist.remove(0);

        if(currentPosition >= lastSongs.size() && mode != 1)
            lastSongs.add(song);

        mediaPlayer.setDataSource(song.getPath());
        mediaPlayer.prepare();
    }

    public void play() {
        if (currentSong != null) {
            if (playerViewHolder != null && playerViewHolder.getSeekBar() != null)
                playerViewHolder.getSeekBar().setMax(mediaPlayer.getDuration());

            mediaPlayer.start();
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bottomViewHolder.getPlay().setImageResource(R.drawable.ic_pause_black_40dp);
                    ImageButton button = context.findViewById(R.id.player_play);
                    if(button != null)
                        button.setImageResource(R.drawable.ic_pause_black_70dp);
                }
            });
        }
    }

    public void play(final Song song) {
        if (playFinished)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    playFinished = false;
                    try {
                        if(currentSong != null && currentSong.equals(song))
                            play();
                        else {
                            prepareMediaPlayer(song);
                            currentSong = song;

                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bottomViewHolder.setSong(currentSong);
                                    if(playerViewHolder != null)
                                        playerViewHolder.setSong(currentSong);
                                }
                            });

                            FileWorker.writePlaylist(lastSongs, "lastSongs");
                            play();

                            if (mode == 2) {
                                if (currentPosition < lastSongs.size() - 2 && !currentSong.equals(lastSongs.get(currentPosition + 1)))
                                    for (int i = currentPosition; i < lastSongs.size(); i++)
                                        lastSongs.remove(i);
                                currentPosition++;
                            }
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    playFinished = true;
                }
            }).start();
    }

    public void pause(){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bottomViewHolder.getPlay().setImageResource(R.drawable.ic_play_arrow_black_40dp);
                ImageButton button = context.findViewById(R.id.player_play);
                if(button != null)
                    button.setImageResource(R.drawable.ic_play_arrow_black_70dp);
            }
        });

        mediaPlayer.pause();
    }

    public void next() {
        if (finished && playFinished)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    finished = false;
                    currentSong.lastPosition = null;
                    if (mode == 0) {
                        int index = currentPlaylist.indexOf(currentSong);
                        play(currentPlaylist.get(index == currentPlaylist.size() - 1 ? 0 : index + 1));
                        currentPosition++;
                    } else if (mode == 1)
                        try {
                            prepareMediaPlayer(currentSong);
                            play(currentSong);
                        }catch(IOException exception){
                            exception.printStackTrace();
                        }
                    else if (mode == 2) {
                        if (currentPosition < lastSongs.size()-1)
                            play(lastSongs.get(currentPosition +1));
                        else {
                            play(currentPlaylist.get(Math.abs(
                                    new Random(System.currentTimeMillis()).nextInt() % (currentPlaylist.size()))
                                    )
                            );
                            lastSongs.add(currentSong);
                            currentPosition++;
                        }
                    }
                    finished = true;
                }
            }).start();

    }

    public void previous() {
        if (finished && playFinished)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    finished = false;
                    switch (mode){
                        case 0:
                            int index = currentPlaylist.indexOf(currentSong);
                            play(currentPlaylist.get(index == 0 ? currentPlaylist.size() - 1 : index - 1));
                            currentPosition--;
                            break;
                        case 1:
                            try {
                                prepareMediaPlayer(currentSong);
                                play(currentSong);
                            }catch(IOException exception){
                                exception.printStackTrace();
                            }
                            break;
                        case 2:
                            play(lastSongs.get(currentPosition--));
                            break;
                        default:
                            return;
                    }
                    finished = true;
                }
            }).start();
    }


    private class BottomViewHolder{
        private CardView cardView;
        private TextView artist;
        private TextView title;
        private ImageButton previous;
        private ImageButton next;
        private ImageButton play;
        private ImageView art;

        BottomViewHolder(Activity activity) {
            cardView = activity.findViewById(R.id.bottom_card_view);
            artist = activity.findViewById(R.id.bottom_artist);
            title = activity.findViewById(R.id.bottom_title);
            previous = activity.findViewById(R.id.bottom_to_left);
            next = activity.findViewById(R.id.bottom_to_right);
            play = activity.findViewById(R.id.bottom_play);
            art = activity.findViewById(R.id.bottom_art);

            if(currentSong != null)
                setSong(currentSong);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayerActivity.class);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.animation_player_show, R.anim.animation_main_hide);
                }
            });

            play.setImageResource(isPlaying()
                    ? R.drawable.ic_pause_black_40dp
                    : R.drawable.ic_play_arrow_black_40dp);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finished)
                        next();
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finished)
                        previous();
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finished) {
                        if (isPlaying())
                            pause();
                        else
                            play();
                    }
                }
            });

        }

        void setSong(Song song){
            art.setImageDrawable(Drawable.createFromPath(song.getAlbumArt()));
            artist.setText(song.getArtist());
            title.setText(song.getTitle());
        }

        CardView getCardView() {
            return cardView;
        }

        TextView getArtist() {
            return artist;
        }

        TextView getTitle() {
            return title;
        }

        ImageButton getPrevious() {
            return previous;
        }

        ImageButton getNext() {
            return next;
        }

        ImageButton getPlay() {
            return play;
        }

        ImageView getArt() {
            return art;
        }

    }
}
