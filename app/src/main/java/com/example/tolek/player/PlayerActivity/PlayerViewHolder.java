package com.example.tolek.player.PlayerActivity;


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
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.SleepTimer;
import com.example.tolek.player.debug.MediaStore;
import com.example.tolek.player.debug.Player;
import com.example.tolek.player.debug.TimerDialog;


public class PlayerViewHolder implements View.OnClickListener {
    Activity activity;

    private ImageView art;
    private TextView title;
    private TextView artist;
    private TextView album;
    private SeekBar seekBar;
    private ImageButton play;
    private Button mode;
    private Button timer;
    private TextPageFragment textPageFragment;
    private volatile boolean isUiThreadDone = true;

    public PlayerViewHolder(View context, TextPageFragment textPageFragment, Activity activity) {
        this.textPageFragment = textPageFragment;
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
        play.setOnClickListener(this);
        mode.setOnClickListener(this);
        timer.setOnClickListener(this);

        setPlayImage(Player.getInstance().isPlaying()
                ? R.drawable.ic_pause_black_70dp
                : R.drawable.ic_play_arrow_black_70dp);

        timer.setText("N");
        mode.setText(String.valueOf(Player.getInstance().getMode()));

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

                    String text = MediaStore.getInstance().getText(song);
                    textPageFragment.setText(text);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play:
                if (Player.getInstance().isPlaying())
                    Player.getInstance().pause();
                else
                    Player.getInstance().play();
                break;
            case R.id.player_previous:
                Player.getInstance().previous();
                break;
            case R.id.player_next:
                Player.getInstance().next();
                break;
            case R.id.modeChanger:
                Player.getInstance().changeMode();
                mode.setText(String.valueOf(Player.getInstance().getMode()));
                break;
            case R.id.timer_button:
                if (!SleepTimer.getInstance().isLaunched()) {
                    new TimerDialog().show(((PlayerActivity) v.getContext()).getSupportFragmentManager(),
                            "Timer");
                } else {
                    timer.setText("N");
                    SleepTimer.getInstance().stop();
                }
                break;
        }
    }
}