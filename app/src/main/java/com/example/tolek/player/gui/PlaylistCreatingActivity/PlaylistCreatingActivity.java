package com.example.tolek.player.gui.PlaylistCreatingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tolek.player.R;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.gui.viewHolder.SongView;

import java.util.ArrayList;

public class PlaylistCreatingActivity extends AppCompatActivity {

    RVAdapter adapter;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("Name");

        RecyclerView recyclerView = findViewById(R.id.playlistsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new RVAdapter(
                this,
                MediaStore.getSongs(),
                R.layout.card_view_song,
                SongView.class,
                true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_creating_playlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(0, new Intent());
                finish();
                return true;
            case R.id.menu_ok:
                ArrayList<? extends Entity> selectedSongs = adapter.getSelected();
                if(selectedSongs.size() > 0) {
                    for(Playlist playlist : MediaStore.getPlaylists().getList())
                        if(playlist.getName().toLowerCase().equals(name.toLowerCase()))
                            return true;

                    MediaStore.getPlaylists()
                            .addPlaylist(new Playlist(name, (ArrayList<Song>)adapter.getSelected()));
                    setResult(1, new Intent());
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
