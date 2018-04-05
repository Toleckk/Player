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

import com.example.tolek.player.AlbumRecyclerViewAdapter;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;

public class AlbumPageFragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.albums_tab, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.albumRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new AlbumRecyclerViewAdapter(
                FileWorker.getAlbums(),
                getActivity().getDrawable(R.drawable.ic_album_black_70dp)
        ));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }
}
