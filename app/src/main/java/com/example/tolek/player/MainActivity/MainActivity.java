package com.example.tolek.player.MainActivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tolek.player.PageFragmentAdapter;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.debug.Player;
import com.example.tolek.player.search.SearchRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainLayout;
    ViewPager tabPager;
    RecyclerView searchRecyclerView;
    SearchRecyclerViewAdapter searchRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FileWorker.launch();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));

        mainLayout = findViewById(R.id.tab_layout);
        TabLayout tab = findViewById(R.id.tab);
        tabPager = findViewById(R.id.view_pager);

        tabPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(), 4));

        tab.setupWithViewPager(tabPager);

        searchRecyclerView = findViewById(R.id.search_layout);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mainLayout.setVisibility(View.GONE);
                tabPager.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.VISIBLE);
                switch (tabPager.getCurrentItem()) {
                    default:
                        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(getDrawable(R.drawable.ic_music_note_black_70dp));
                        searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
                }
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.VISIBLE);
                tabPager.setVisibility(View.VISIBLE);
                searchRecyclerView.setVisibility(View.GONE);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRecyclerViewAdapter != null)
                    searchRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
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
}
