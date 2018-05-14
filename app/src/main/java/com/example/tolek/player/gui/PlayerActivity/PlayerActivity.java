package com.example.tolek.player.gui.PlayerActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.tolek.player.R;
import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.MediaStore;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.HaveContextMenu;
import com.example.tolek.player.gui.OnBackPressedListener;
import com.example.tolek.player.gui.viewHolder.ContextMenuViewHolder;

public class PlayerActivity extends AppCompatActivity implements HaveContextMenu {

    private SectionsPagerAdapter sectionsPagerAdapter;
    OnBackPressedListener onBackPressedListener;
    private int margin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.playerViewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int position = 1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (this.position != position)
                    if ((this.position = position) == 0)
                        sectionsPagerAdapter.textPageFragment.setText(
                                MediaStore.getText(Player.getInstance().getCurrentSong())
                        );
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        margin = getResources().getDimensionPixelSize(
                getResources().getIdentifier("status_bar_height",
                        "dimen",
                        "android"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.log("PAUSE");
        Player.getInstance().setPlayerViewHolder(null);
        overridePendingTransition(R.anim.animation_main_show, R.anim.animation_player_hide);
    }

    @Override
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void hideContextMenu() {
        View view = sectionsPagerAdapter.getItem(2).getView();
        ((ViewGroup.MarginLayoutParams) view.findViewById(R.id.playlistRecyclerView)
                .getLayoutParams()).setMargins(0, margin, 0, 0);
        view.requestLayout();

        view.findViewById(R.id.context_menu).setVisibility(View.GONE);
    }

    @Override
    public ContextMenuViewHolder createContextMenuView(View.OnClickListener listener) {
        View view = sectionsPagerAdapter.getItem(2).getView();

        ((ViewGroup.MarginLayoutParams) view.findViewById(R.id.playlistRecyclerView)
                .getLayoutParams()).setMargins(0, margin, 0,
                (int) (60f * getResources().getDisplayMetrics().density));
        view.requestLayout();

        view.findViewById(R.id.context_menu).setVisibility(View.VISIBLE);

        return new ContextMenuViewHolder(view, listener);
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.onBackPressed();
        else
            super.onBackPressed();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        PlayerPageFragment playerPageFragment;
        TextPageFragment textPageFragment;
        PlaylistPageFragment playlistPageFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            playerPageFragment = new PlayerPageFragment();
            textPageFragment = new TextPageFragment();
            playlistPageFragment = new PlaylistPageFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
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
