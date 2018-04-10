package com.example.tolek.player.MainActivity;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.PlayerActivity.PlayerActivity;
import com.example.tolek.player.R;
import com.example.tolek.player.debug.Player;

public class BottomViewHolder implements View.OnClickListener {
    private Activity activity;
    private TextView artist;
    private TextView title;
    private ImageButton play;
    private ImageView art;

    public BottomViewHolder(final Activity activity) {
        this.activity = activity;
        artist = activity.findViewById(R.id.bottom_artist);
        title = activity.findViewById(R.id.bottom_title);
        play = activity.findViewById(R.id.bottom_play);
        art = activity.findViewById(R.id.bottom_art);

        setPlayImage(Player.getInstance().isPlaying()
                ? R.drawable.ic_pause_black_40dp
                : R.drawable.ic_play_arrow_black_40dp);

        play.setOnClickListener(this);
        activity.findViewById(R.id.bottom_card_view).setOnClickListener(this);
        activity.findViewById(R.id.bottom_next).setOnClickListener(this);
        activity.findViewById(R.id.bottom_previous).setOnClickListener(this);


        setSong(Player.getInstance().getCurrentSong());
    }

    public void setSong(final Song song) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (song != null) {
                    Glide.with(activity)
                            .load(song.getAlbumArt())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_music_note_black_70dp))
                            .into(art);
                    artist.setText(song.getArtist());
                    title.setText(song.getTitle());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_card_view:
                activity.startActivity(new Intent(activity, PlayerActivity.class));
                activity.overridePendingTransition(R.anim.animation_player_show, R.anim.animation_main_hide);
                break;
            case R.id.bottom_play:
                Player player = Player.getInstance();
                if (player.isPlaying())
                    player.pause();
                else
                    player.play();
                break;
            case R.id.bottom_previous:
                Player.getInstance().previous();
                break;
            case R.id.bottom_next:
                Player.getInstance().next();
                break;
        }
    }

    public void setPlayImage(final int id) {
        activity.runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Glide.with(activity)
                                               .load(id)
                                               .into(play);
                                   }
                               }
        );
    }
}
