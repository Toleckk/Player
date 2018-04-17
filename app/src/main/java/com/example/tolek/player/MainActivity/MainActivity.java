package com.example.tolek.player.MainActivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.LinearLayout;

import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.debug.Player;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainLayout;
    ViewPager tabPager;

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
                Fragment fragment = ((PageFragmentAdapter)tabPager.getAdapter())
                        .getItem(tabPager.getCurrentItem());
                switch(tabPager.getCurrentItem()){
                    case 0:
                        ((SongPageFragment)fragment).getRecyclerViewAdapter().getFilter().filter(newText);
                        break;
                    case 1:
                        ((AlbumPageFragment)fragment).getRecyclerViewAdapter().getFilter().filter(newText);
                        break;
                    case 2:
                        ((ArtistPageFragment)fragment).getRecyclerViewAdapter().getFilter().filter(newText);
                }
                return true;
            }
        });
        tabPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int position;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(this.position != position){
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
}
