package com.example.tolek.player.debug;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.PlayerActivity.PlayerActivity;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.Player;
import com.example.tolek.player.Util.SleepTimer;


public class PlayerViewHolder{
    private ImageView art;
    private TextView title;
    private TextView artist;
    private TextView album;
    private SeekBar seekBar;
    private ImageButton previous;
    private ImageButton play;
    private ImageButton next;
    private Button mode;
    private Button timer;
    private Drawable defaultArt;

    public PlayerViewHolder(View context){
        art = context.findViewById(R.id.player_art);
        title = context.findViewById(R.id.player_title);
        artist = context.findViewById(R.id.player_artist);
        album = context.findViewById(R.id.player_album);
        seekBar = context.findViewById(R.id.seekBar);
        previous = context.findViewById(R.id.player_previous);
        play = context.findViewById(R.id.player_play);
        next = context.findViewById(R.id.player_next);
        mode = context.findViewById(R.id.modeChanger);
        timer = context.findViewById(R.id.timer_button);
        defaultArt = context.getContext().getDrawable(R.drawable.ic_music_note_black_70dp);

        timer.setText("N");

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
            Glide.with(context)
                    .load(song.getAlbumArt())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_music_note_black_70dp))
                    .into(art);

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

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SleepTimer.getInstance().isLaunched()) {
                    new TimerDialog().show(((PlayerActivity) v.getContext()).getSupportFragmentManager(),
                            "Timer");
                }
                else {
                    timer.setText("N");
                    SleepTimer.getInstance().stop();
                }
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

    public void setSong(Song song){
        Glide.with(art.getContext())
                .load(song.getAlbumArt())
                .apply(new RequestOptions().placeholder(R.drawable.ic_music_note_black_70dp))
                .into(art);

        artist.setText(song.getArtist());
        title.setText(song.getTitle());
        album.setText(song.getAlbum());
        seekBar.setMax(Integer.valueOf(song.getDuration()));
    }
}
