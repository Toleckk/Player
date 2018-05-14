package com.example.tolek.player.gui.PlaylistActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.tolek.player.R;
import com.example.tolek.player.Repository.EntityKeeper;
import com.example.tolek.player.domain.Entities.Album;
import com.example.tolek.player.domain.Entities.Artist;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.HaveContextMenu;
import com.example.tolek.player.gui.MainActivity.BottomViewHolder;
import com.example.tolek.player.gui.OnBackPressedListener;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.gui.viewHolder.ContextMenuViewHolder;
import com.example.tolek.player.gui.viewHolder.SongView;

public class PlaylistActivity extends AppCompatActivity implements HaveContextMenu{

    private OnBackPressedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));

        RecyclerView recyclerView = findViewById(R.id.songRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        EntityKeeper<? extends Entity> songs;
        int index = getIntent().getIntExtra("Album", -1);
        if(index != -1) {
            songs = MediaStore.getAlbums().get(index);
            setTitle(((Album)songs).getAlbumName());
        }
        else if ((index = getIntent().getIntExtra("Artist", -1)) != -1) {
            songs = MediaStore.getArtists().getList().get(index);
            setTitle(((Artist) songs).getName());
        }
        else {
            songs = MediaStore.getPlaylists().getList()
                    .get(getIntent().getIntExtra("Playlist", -1));
            setTitle(((Playlist)songs).getName());
        }

        recyclerView.setAdapter(new RVAdapter(
                this,
                songs,
                R.layout.card_view_song,
                SongView.class
        ));
    }

    @Override
    protected void onResume(){
        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));
        super.onResume();
    }

    @Override
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        listener = onBackPressedListener;
    }

    @Override
    public void onBackPressed(){
        if(listener != null)
            listener.onBackPressed();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void hideContextMenu() {
        findViewById(R.id.bottom_card_view).setVisibility(View.VISIBLE);
        findViewById(R.id.context_menu).setVisibility(View.GONE);
    }

    @Override
    public ContextMenuViewHolder createContextMenuView(View.OnClickListener listener) {
        findViewById(R.id.context_menu).setVisibility(View.VISIBLE);
        findViewById(R.id.bottom_card_view).setVisibility(View.GONE);

        return new ContextMenuViewHolder(this, listener);
    }
}
