package com.example.tolek.player.gui.MainActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PageFragmentAdapter extends FragmentStatePagerAdapter {
    SongPageFragment songPageFragment;
    AlbumPageFragment albumPageFragment;
    ArtistPageFragment artistPageFragment;
    PlaylistPageFragment playlistPageFragment;
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
        playlistPageFragment = new PlaylistPageFragment();
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
                return playlistPageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsQuantity;
    }
}
