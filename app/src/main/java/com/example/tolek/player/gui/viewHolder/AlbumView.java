package com.example.tolek.player.gui.viewHolder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.R;
import com.example.tolek.player.domain.Entities.Album;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.PlaylistActivity.PlaylistActivity;
import com.example.tolek.player.gui.RVAdapter;

import java.util.ArrayList;


public class AlbumView extends CheckableViewHolder {
    public static Drawable defaultArt;
    private static int layoutID;

    public ImageView albumArt;
    public TextView albumText;
    public TextView artist;
    public Album album;


    public AlbumView(View itemView, RVAdapter adapter) {
        super(itemView, (CardView) itemView.findViewById(R.id.albumCardView), adapter);

        albumArt = itemView.findViewById(R.id.album_art);
        albumText = itemView.findViewById(R.id.album);
        artist = itemView.findViewById(R.id.album_artist);
    }

    @Override
    public int getLayoutID() {
        return layoutID;
    }

    @Override
    public Drawable getDefaultArt() {
        return defaultArt;
    }

    @Override
    public void fillFromEntity(Entity entity, ArrayList list, boolean checkable,
                               boolean isChecked, View.OnClickListener checkBoxListener) {
        album = (Album) entity;

        albumText.setText(album.getAlbumName());
        artist.setText(album.getArtist());

        Glide.with(mainCardView.getContext())
                .load(album.getCover())
                .apply(new RequestOptions().placeholder(defaultArt))
                .into(albumArt);


        if (!checkable)
            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(
                            new Intent(view.getContext(), PlaylistActivity.class)
                                    .putExtra("Album",
                                            MediaStore.getAlbums().indexOf(album))
                    );
                }
            });
        else
            mainCardView.setOnClickListener(checkBoxListener);

        checkBox.setVisibility(checkable ? View.VISIBLE : View.GONE);
        checkBox.setImageResource(isChecked
                ? R.drawable.ic_check_circle_black_40dp
                : R.drawable.ic_radio_button_unchecked_black_24dp);
        checkBox.setOnClickListener(checkBoxListener);
    }
}
