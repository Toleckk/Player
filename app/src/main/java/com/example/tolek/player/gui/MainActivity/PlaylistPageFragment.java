package com.example.tolek.player.gui.MainActivity;

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
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.dialogs.PlaylistCreateDialog;
import com.example.tolek.player.gui.viewHolder.PlaylistView;

public class PlaylistPageFragment extends Fragment implements HaveRecyclerViewAdapter{

    private RVAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_playlists, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.playlistsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new RVAdapter(
                (Activity)getContext(),
                MediaStore.getPlaylists(),
                R.layout.card_view_playlist,
                PlaylistView.class
        );

        recyclerView.setAdapter(recyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        view.findViewById(R.id.createPlaylist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlaylistCreateDialog().show(
                        ((MainActivity)getContext()).getSupportFragmentManager(),
                        "Create");
            }
        });

        return view;
    }

    @Override
    public RVAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    public void notifyPlaylistAdded() {
        recyclerViewAdapter.notifyElementAdded();
    }
}
