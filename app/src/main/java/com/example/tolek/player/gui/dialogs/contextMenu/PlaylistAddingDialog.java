package com.example.tolek.player.gui.dialogs.contextMenu;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolek.player.R;
import com.example.tolek.player.Repository.EntityKeeper;
import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.DialogRVAdapter;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.gui.viewHolder.ArtistView;
import com.example.tolek.player.gui.viewHolder.PlaylistView;

import java.util.ArrayList;


public class PlaylistAddingDialog extends DialogFragment {
    RVAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_playlist_adding, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.playlist_adding_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DialogRVAdapter(this));

        return view;
    }

    public void setAdapter(RVAdapter adapter){
        this.adapter = adapter;
    }

    public RVAdapter getAdapter(){
        return adapter;
    }
}
