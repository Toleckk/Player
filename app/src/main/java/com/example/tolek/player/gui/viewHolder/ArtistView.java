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
import com.example.tolek.player.domain.Entities.Artist;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.PlaylistActivity.PlaylistActivity;
import com.example.tolek.player.gui.RVAdapter;

import java.util.ArrayList;


public class ArtistView extends CheckableViewHolder {
    public static Drawable defaultArt;
    private static int layoutID;

    public TextView artistText;
    public TextView tracks;
    public ImageView art;
    public Artist artist;

    public ArtistView(View itemView, RVAdapter adapter) {
        super(itemView, (CardView) itemView.findViewById(R.id.artistCardView), adapter);
        artistText = itemView.findViewById(R.id.artist);
        tracks = itemView.findViewById(R.id.tracks_quantity);
        art = itemView.findViewById(R.id.artist_art);
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
        artist = (Artist) entity;

        artistText.setText(artist.getName());
        tracks.setText(String.valueOf(artist.getList().size()));

        Glide.with(mainCardView.getContext())
                .load(artist.getArt())
                .apply(new RequestOptions().placeholder(defaultArt))
                .into(this.art);

        if (!checkable)
            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(
                            new Intent(view.getContext(), PlaylistActivity.class)
                                    .putExtra("Artist",
                                            MediaStore.getArtists().indexOf(artist))
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
