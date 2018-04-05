package com.example.tolek.player.PlayerActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.tolek.player.R;
import com.example.tolek.player.Util.Player;

public class PlayerActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.animation_main_show, R.anim.animation_player_hide);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Player.getInstance().setContext(this);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.playerViewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onResume(){
        Player.getInstance().setContext(this);
        super.onResume();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private PlayerPageFragment playerPageFragment = new PlayerPageFragment();
        private TextPageFragment textPageFragment = new TextPageFragment();
        private PlaylistPageFragment playlistPageFragment = new PlaylistPageFragment();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return textPageFragment;
                case 1:
                    return playerPageFragment;
                case 2:
                    return playlistPageFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


    }
}
