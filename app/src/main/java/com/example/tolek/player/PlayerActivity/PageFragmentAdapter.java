package com.example.tolek.player.PlayerActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PageFragmentAdapter extends FragmentStatePagerAdapter {
    PlayerPageFragment playerPageFragment;
    TextPageFragment textPageFragment;
    int tabsQuantity;
    final CharSequence[] titles;

    public PageFragmentAdapter(FragmentManager fragmentManager, int tabsQuantity){
        super(fragmentManager);
        this.tabsQuantity = tabsQuantity;
        titles = new CharSequence[3];
        titles[0] = "Player";

        playerPageFragment = new PlayerPageFragment();
        textPageFragment = new TextPageFragment();

    }


    @Override
    public CharSequence getPageTitle(int position){
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return playerPageFragment;
            case 1:
                return textPageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsQuantity;
    }
}
