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
import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.gui.viewHolder.SongView;

public class SongPageFragment extends Fragment implements HaveRecyclerViewAdapter{

    private RVAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_songs, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.songRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new RVAdapter(
                (Activity)getContext(),
                MediaStore.getSongs(),
                R.layout.card_view_song,
                SongView.class
        );

//        recyclerViewAdapter = new SongRecyclerViewAdapter(
//                MediaStore.getInstance().getList(),
//                getActivity().getDrawable(R.drawable.ic_music_note_black_70dp)
//        );

        recyclerView.setAdapter(recyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public RVAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }
}
