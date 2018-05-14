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
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.gui.viewHolder.AlbumView;

public class AlbumPageFragment extends Fragment implements HaveRecyclerViewAdapter{
    private RVAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_albums, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.albumRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new RVAdapter(
                (Activity)getContext(),
                MediaStore.getAlbums(),
                R.layout.card_view_album,
                AlbumView.class
        );

//        recyclerViewAdapter = new AlbumRecyclerViewAdapter(MediaStore.getInstance().getAlbums(),
//                getActivity().getDrawable(R.drawable.ic_album_black_70dp));
        recyclerView.setAdapter(recyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public RVAdapter getRecyclerViewAdapter(){
        return recyclerViewAdapter;
    }
}
