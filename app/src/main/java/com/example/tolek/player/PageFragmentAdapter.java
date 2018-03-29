package com.example.tolek.player;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tolek.player.Widgets.AlbumPageFragment;
import com.example.tolek.player.Widgets.ArtistPageFragment;
import com.example.tolek.player.Widgets.SongPageFragment;

/**
 * Created by Tolek on 06.03.2018.
 */

public class PageFragmentAdapter extends FragmentStatePagerAdapter {
    SongPageFragment songPageFragment;
    AlbumPageFragment albumPageFragment;
    ArtistPageFragment artistPageFragment;
    int tabsQuantity;
    final CharSequence[] titles;

    public PageFragmentAdapter(FragmentManager fragmentManager, int tabsQuantity){
        super(fragmentManager);
        this.tabsQuantity = tabsQuantity;
        titles = new CharSequence[4];
        titles[0] = "Songs";
        titles[1] = "Albums";
        titles[2] = "Artists";
        titles[3] = "Playlists";

        songPageFragment = new SongPageFragment();
        albumPageFragment = new AlbumPageFragment();
        artistPageFragment = new ArtistPageFragment();
    }


    @Override
    public CharSequence getPageTitle(int position){
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return songPageFragment;
            case 1:
                return albumPageFragment;
            case 2:
                return artistPageFragment;
            case 3:
                return new SongPageFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsQuantity;
    }
}
