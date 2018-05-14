package com.example.tolek.player.gui.PlayerActivity;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.SleepTimer;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.dialogs.TimerDialog;


public class PlayerViewHolder implements View.OnClickListener {
    Activity activity;

    private ImageView art;
    private TextView title;
    private TextView artist;
    private TextView album;
    private SeekBar seekBar;
    private ImageButton play;
    private ImageButton mode;
    private ImageButton timer;
    private volatile boolean isUiThreadDone = true;

    public PlayerViewHolder(View context, Activity activity) {
        this.activity = activity;

        art = context.findViewById(R.id.player_art);
        title = context.findViewById(R.id.player_title);
        artist = context.findViewById(R.id.player_artist);
        album = context.findViewById(R.id.player_album);
        seekBar = context.findViewById(R.id.seekBar);
        play = context.findViewById(R.id.player_play);
        mode = context.findViewById(R.id.modeChanger);
        timer = context.findViewById(R.id.timer_button);

        context.findViewById(R.id.player_next).setOnClickListener(this);
        context.findViewById(R.id.player_previous).setOnClickListener(this);

        play.setOnClickListener(this);
        mode.setOnClickListener(this);
        timer.setOnClickListener(this);

        setPlayImage(Player.getInstance().isPlaying()
                ? R.drawable.ic_pause_black_70dp
                : R.drawable.ic_play_arrow_black_70dp);

        changeMode(Player.getInstance().getMode());

        Glide.with(context)
                .load(SleepTimer.getInstance().isLaunched()
                        ? R.drawable.ic_timer_off_black_24dp
                        : R.drawable.ic_timer_black_24dp)
                .into(timer);

        seekBar.setProgress(Player.getInstance().getCurrentPosition());
        if (Player.getInstance().getCurrentSong() != null) {
            seekBar.setMax(Integer.valueOf(Player.getInstance().getCurrentSong().getDuration()));
        }

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
        setSong(Player.getInstance().getCurrentSong());
    }

    public boolean isUiThreadDone() {
        return isUiThreadDone;
    }

    public SeekBar getSeekBar(){
        return seekBar;
    }

    public void setSong(final Song song) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isUiThreadDone = false;
                if (song != null) {
                    title.setText(song.getTitle());
                    album.setText(song.getAlbum());
                    artist.setText(song.getArtist());
                    seekBar.setMax(Integer.valueOf(song.getDuration()));

                    if(song.getAlbumArt() == null || song.getAlbumArt().equals(""))
                        Glide.with(activity)
                                .load(song.getAlbumArt())
                                .apply(new RequestOptions().placeholder(R.drawable.ic_music_note_black_70dp))
                                .into(art);
                    else
                        art.setImageDrawable(Drawable.createFromPath(song.getAlbumArt()));

//                    Glide.with(activity)
//                            .load(song.getAlbumArt())
//                            .apply(new RequestOptions().placeholder(R.drawable.ic_music_note_black_70dp))
//                            .into(art);
                }
                isUiThreadDone = true;
            }
        });

    }

    public void setPlayImage(final int id){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(activity)
                        .load(id)
                        .into(play);
            }
        });

    }

    private void changeMode(int mode){
        int resId = R.drawable.ic_shuffle_black_24dp;
        switch(mode){
            case 0:
                resId = R.drawable.ic_repeat_black_24dp;
                break;
            case 1:
                resId = R.drawable.ic_repeat_one_black_24dp;
                break;
        }
        Glide.with(activity)
                .load(resId)
                .into(this.mode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play:
                if (Player.getInstance().isPlaying())
                    Player.getInstance().pause();
                else
                    Player.getInstance().play();
                return;
            case R.id.player_previous:
                Player.getInstance().previous();
                return;
            case R.id.player_next:
                Player.getInstance().next();
                return;
            case R.id.modeChanger:
                changeMode(Player.getInstance().changeMode());
                return;
            case R.id.timer_button:
                if (!SleepTimer.getInstance().isLaunched()) {
                    new TimerDialog().show(((PlayerActivity) v.getContext()).getSupportFragmentManager(),
                            "Timer");
                } else {
                    timer.setImageResource(R.drawable.ic_timer_black_24dp);
                    SleepTimer.getInstance().stop();
                }
        }
    }
}