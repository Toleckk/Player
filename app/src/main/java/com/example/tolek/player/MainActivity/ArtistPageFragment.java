package com.example.tolek.player.MainActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolek.player.ArtistRecyclerViewAdapter;
import com.example.tolek.player.Entities.Artist;
import com.example.tolek.player.Entities.ViewCreatingStrategy;
import com.example.tolek.player.R;
import com.example.tolek.player.RecyclerViewAdapter;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.debug.MediaStore;

public class ArtistPageFragment extends Fragment{
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artists_tab, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.artistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new RecyclerViewAdapter(MediaStore.getInstance().getArtists(),
                getActivity().getDrawable(R.drawable.ic_music_note_black_70dp),
                R.layout.artist_card_view, new ViewCreatingStrategy() {
            @Override
            public RecyclerView.ViewHolder createView(View view) {
                return new Artist.ViewHolder(view);
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    public RecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }
}
