package com.example.tolek.player.gui.PlayerActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolek.player.R;
import com.example.tolek.player.Repository.Repository;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.viewHolder.SongView;

public class PlaylistPageFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_activity_tab_playlist, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.playlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ((ViewGroup.MarginLayoutParams) recyclerView
                .getLayoutParams()).setMargins(0, getResources().getDimensionPixelSize(
                getResources().getIdentifier("status_bar_height",
                        "dimen",
                        "android")), 0, 0);

        recyclerView.setAdapter(new RVAdapter(
                (Activity)getContext(),
                Player.getInstance().getCurrentPlaylist(),
                R.layout.card_view_song,
                SongView.class
        ));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }
}
