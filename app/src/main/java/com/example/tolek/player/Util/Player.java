package com.example.tolek.player.Util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Player {
    private static final Player ourInstance = new Player();
    private final int MAX_PLAYLIST_SIZE = 1000;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Song currentSong = null;
    private ArrayList<Song> lastSongs = new ArrayList<>();
    private ArrayList<Song> currentPlaylist = new ArrayList<>();
    private BottomViewHolder bottomViewHolder;
    private PlayerViewHolder playerViewHolder;

    private Activity context;
    private volatile boolean finished = true;
    private volatile boolean playFinished = true;

    public int getMode() {
        return mode;
    }

    private volatile int mode = 0;
    int temp = 0;

    static public Player getInstance() {
        return ourInstance;
    }

    private Player() {

    }

    public void setCurrentPosition(int progress) {
        if(mediaPlayer != null)
            mediaPlayer.seekTo(progress);
    }

    public void changeMode() {
        mode = mode == 2 ? 0 : mode+1;
    }

    private static class BottomViewHolder {
        private CardView cardView;
        private TextView artist;
        private TextView title;
        private ImageButton previous;
        private ImageButton next;
        private ImageButton play;
        private ImageView art;

        BottomViewHolder(Activity context) {
            cardView = context.findViewById(R.id.bottom_card_view);
            artist = context.findViewById(R.id.bottom_artist);
            title = context.findViewById(R.id.bottom_title);
            previous = context.findViewById(R.id.bottom_to_left);
            next = context.findViewById(R.id.bottom_to_right);
            play = context.findViewById(R.id.bottom_play);
            art = context.findViewById(R.id.bottom_art);
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

    private static class PlayerViewHolder{
        ImageView art;
        TextView title;
        TextView artist;
        TextView album;
        SeekBar seekBar;
        ImageButton previous;
        ImageButton play;
        ImageButton next;
        Button mode;
        Drawable defaultArt;

        public PlayerViewHolder(Activity activity, View context){
            art = context.findViewById(R.id.player_art);
            title = context.findViewById(R.id.player_title);
            artist = context.findViewById(R.id.player_artist);
            album = context.findViewById(R.id.player_album);
            seekBar = context.findViewById(R.id.seekBar);
            previous = context.findViewById(R.id.player_previous);
            play = context.findViewById(R.id.player_play);
            next = context.findViewById(R.id.player_next);
            mode = context.findViewById(R.id.modeChanger);
            defaultArt = activity.getDrawable(R.drawable.ic_music_note_black_70dp);

            play.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (Player.getInstance().isPlaying())
                                                Player.getInstance().pause();
                                            else
                                                Player.getInstance().play();
                                        }
                                    }
            );
            play.setImageResource(Player.getInstance().isPlaying()
                            ? R.drawable.ic_pause_black_70dp
                            : R.drawable.ic_play_arrow_black_70dp);

            previous.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Player.getInstance().previous();
                        }
                    }
            );

            next.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Player.getInstance().next();
                        }
                    }
            );

            Song song = Player.getInstance().getCurrentSong();
            if (song != null) {
                art.setImageDrawable(song.getAlbumArt() == null
                        ? activity.getDrawable(R.drawable.ic_music_note_black_70dp)
                        : Drawable.createFromPath(song.getAlbumArt()));

                title.setText(song.getTitle());
                artist.setText(song.getArtist());
                album.setText(song.getAlbum());
            }

            if(Player.getInstance().getCurrentSong() != null)
                seekBar.setMax(Integer.valueOf(Player.getInstance().getCurrentSong().getDuration()));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Player.getInstance().setCurrentPosition(seekBar.getProgress());
                }
            });
            seekBar.setProgress(Player.getInstance().getCurrentPosition());

            mode.setText(String.valueOf(Player.getInstance().getMode()));
            mode.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Player.getInstance().changeMode();
                            ((Button)view).setText(String.valueOf(Player.getInstance().getMode()));
                        }
                    });
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getArt() {
            return art;
        }

        public TextView getArtist() {
            return artist;
        }

        public TextView getAlbum() {
            return album;
        }

        public SeekBar getSeekBar() {
            return seekBar;
        }

        public ImageButton getPrevious() {
            return previous;
        }

        public ImageButton getPlay() {
            return play;
        }

        public ImageButton getNext() {
            return next;
        }

        public Button getMode() {
            return mode;
        }

        void setSong(Song song){
            art.setImageDrawable(song.getAlbumArt() == null
                    ? defaultArt
                    : Drawable.createFromPath(song.getAlbumArt())
            );
            art.setImageDrawable(Drawable.createFromPath(song.getAlbumArt()));
            artist.setText(song.getArtist());
            title.setText(song.getTitle());
            album.setText(song.getAlbum());
            seekBar.setMax(Integer.valueOf(song.getDuration()));
        }
    }

    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void setContext(Activity context, View view){
        this.context = context;
        playerViewHolder = new PlayerViewHolder(context, view);
    }

    @Nullable
    public Song getCurrentSong(){
        return currentSong;
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void setPlaylist(ArrayList<Song> playlist){
        currentPlaylist = playlist;
        FileWorker.writePlaylist(playlist, "currentPlaylist");
    }

    public void setLastSongs(ArrayList<Song> lSongs){
        lastSongs = lSongs;
        temp = lastSongs.size()-1;
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

    public void pause(){
        bottomViewHolder.getPlay().setImageResource(R.drawable.ic_play_arrow_black_40dp);
        ImageButton button = context.findViewById(R.id.player_play);
        if(button != null)
            button.setImageResource(R.drawable.ic_play_arrow_black_70dp);
        mediaPlayer.pause();
    }

    public void play(final Song song) {
        if (playFinished)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    playFinished = false;
                    try {
                        if(currentSong.equals(song))
                            play();
                        else {
                            mediaPlayer.reset();

                            if (currentPlaylist.size() == MAX_PLAYLIST_SIZE)
                                currentPlaylist.remove(0);

                            if (mode != 2 || temp == lastSongs.size() - 1)
                                lastSongs.add(song);
                            mediaPlayer.setDataSource(song.getPath());
                            mediaPlayer.prepare();
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
                                if (temp < lastSongs.size() - 2 && !currentSong.equals(lastSongs.get(temp + 1)))
                                    for (int i = temp; i < lastSongs.size(); i++)
                                        lastSongs.remove(i);
                                temp++;
                            }
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    playFinished = true;
                }
            }).start();
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

    public ArrayList<Song> getCurrentPlaylist() {
        return currentPlaylist;
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
                    } else if (mode == 1) {
                        play(currentSong);
                    } else if (mode == 2) {
                        if (temp < lastSongs.size()-1)
                            play(lastSongs.get(temp+1));
                        else {
                            play(currentPlaylist.get(Math.abs(
                                    new Random(System.currentTimeMillis()).nextInt() % (currentPlaylist.size()))
                                    )
                            );
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
                            break;
                        case 1:
                            play(currentSong);
                            break;
                        case 2:
                            play(lastSongs.get(temp--));
                            break;
                        default:
                            return;
                    }
                    finished = true;
                }
            }).start();
    }

    public void launch(Activity context){
        this.context = context;
        bottomViewHolder = new BottomViewHolder(context);

        bottomViewHolder.getNext().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finished)
                    next();
            }
        });

        bottomViewHolder.getPrevious().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finished)
                    previous();
            }
        });

        bottomViewHolder.getPlay().setOnClickListener(new View.OnClickListener() {
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
}
