package com.example.tolek.player.PlayerActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.Player;

public class PlayerPageFragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player, container, false);

        Player.getInstance().setPlayerViewHolder(view);

        ImageView imageView = view.findViewById(R.id.bicycle);
        int size = getResources().getDimensionPixelSize(
                getResources().getIdentifier("status_bar_height",
                                    "dimen",
                                 "android"));
        imageView.setMinimumHeight(size);
        imageView.setMaxHeight(size);

        return view;
    }
}

/*
        LinearLayout layout = view.findViewById(R.id.player_layout);
        String art = Player.getInstance().getCurrentSong().getAlbumArt();
        ImageView artImage = new ImageView(getContext());
        artImage.setScaleType(ImageView.ScaleType.CENTER);
        artImage.setAdjustViewBounds(true);
        artImage.setPadding(0, 0, 0, 0);
        layout.addView(artImage, 0);
        Drawable a = art == null
                ? getActivity().getDrawable(R.drawable.ic_music_note_black_70dp)
                : Drawable.createFromPath(art);
        artImage.setImageDrawable(a);*/