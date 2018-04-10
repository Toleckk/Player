package com.example.tolek.player.AlbumActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.R;
import com.example.tolek.player.SongRecyclerViewAdapter;
import com.example.tolek.player.MainActivity.BottomViewHolder;
import com.example.tolek.player.debug.MediaStore;
import com.example.tolek.player.debug.Player;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));

        RecyclerView recyclerView = findViewById(R.id.songRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ArrayList<Song> songs;
        int index = getIntent().getIntExtra("Album", -1);
        if(index != -1)
            songs = MediaStore.getInstance().getAlbums().get(index).getSongs();
        else
            songs = MediaStore.getInstance().getArtists()
                    .get(getIntent().getIntExtra("Artist", -1)).getSongs();

        recyclerView.setAdapter(new SongRecyclerViewAdapter(
                songs,
                getDrawable(R.drawable.ic_music_note_black_70dp)
        ));
    }

    @Override
    protected void onResume(){
        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));
        super.onResume();
    }
/*
    @Override
    protected void onPause(){
        supportFinishAfterTransition();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
