package com.example.tolek.player.gui.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.HaveContextMenu;
import com.example.tolek.player.gui.OnBackPressedListener;
import com.example.tolek.player.gui.RVAdapter;
import com.example.tolek.player.gui.viewHolder.AlbumView;
import com.example.tolek.player.gui.viewHolder.ArtistView;
import com.example.tolek.player.gui.viewHolder.ContextMenuViewHolder;
import com.example.tolek.player.gui.viewHolder.PlaylistView;
import com.example.tolek.player.gui.viewHolder.SongView;

public class MainActivity extends AppCompatActivity implements HaveContextMenu {

    LinearLayout mainLayout;
    ViewPager tabPager;
    OnBackPressedListener onBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FileWorker.launch();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TEMPORARY
        SongView.defaultArt = getDrawable(R.drawable.ic_music_note_black_70dp);
        AlbumView.defaultArt = getDrawable(R.drawable.ic_album_black_70dp);
        ArtistView.defaultArt = getDrawable(R.drawable.ic_music_note_black_70dp);
        PlaylistView.defaultArt = getDrawable(R.drawable.ic_album_black_70dp);
        //

        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));
        Player.getInstance().setPreferences(
                getSharedPreferences("Settings", Context.MODE_PRIVATE)
        );

        mainLayout = findViewById(R.id.tab_layout);
        TabLayout tab = findViewById(R.id.tab);
        tabPager = findViewById(R.id.view_pager);

        tabPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(), 4));

        tab.setupWithViewPager(tabPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(
                menu.findItem(R.id.action_search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((HaveRecyclerViewAdapter)((PageFragmentAdapter)tabPager.getAdapter())
                        .getItem(tabPager.getCurrentItem()))
                        .getRecyclerViewAdapter().getFilter().filter(newText);
                return true;
            }
        });
        tabPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int position;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(this.position != position){
                    if(onBackPressedListener != null)
                        onBackPressedListener.onBackPressed();

                    this.position = position;
                    searchView.onActionViewCollapsed();
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        return true;
    }


    @Override
    protected void onResume() {
        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Player.getInstance().setBottomViewHolder(null);
        super.onDestroy();
    }

    @Override
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener){
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void hideContextMenu() {
        findViewById(R.id.context_menu).setVisibility(View.GONE);
        findViewById(R.id.bottom_card_view).setVisibility(View.VISIBLE);
    }

    @Override
    public ContextMenuViewHolder createContextMenuView(View.OnClickListener listener) {
        findViewById(R.id.context_menu).setVisibility(View.VISIBLE);
        findViewById(R.id.bottom_card_view).setVisibility(View.GONE);
        ContextMenuViewHolder contextMenu = new ContextMenuViewHolder(this, listener);
        contextMenu.delete.setVisibility(
                tabPager.getCurrentItem() == 0 || tabPager.getCurrentItem() == 3
                ? View.VISIBLE
                : View.GONE
        );
        return contextMenu;
    }

    @Override
    public void onBackPressed(){
        if(onBackPressedListener != null)
            onBackPressedListener.onBackPressed();
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1)
            ((PageFragmentAdapter)tabPager.getAdapter()).playlistPageFragment.notifyPlaylistAdded();
    }
}
