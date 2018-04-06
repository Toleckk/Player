package com.example.tolek.player.MainActivity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.util.Log;
import android.view.View;

import com.example.tolek.player.PageFragmentAdapter;
import com.example.tolek.player.R;
import com.example.tolek.player.Repository.SongsRepository;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.Util.Player;
import com.example.tolek.player.PlayerActivity.PlayerActivity;

public class MainActivity extends AppCompatActivity {
    private final String MYTAG = "MYTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        Log.d(MYTAG, "Start: " + String.valueOf(System.currentTimeMillis()));
        SongsRepository.getInstance().initialize();
        Log.d(MYTAG, "Finish: " + String.valueOf(System.currentTimeMillis()));*/

        Player.getInstance().setContext(this);
        Player.getInstance().setBottomViewHolder();
        FileWorker.setContentResolver(getContentResolver());
        FileWorker.launch();

        TabLayout tabLayout = findViewById(R.id.tab);
        ViewPager viewPager = findViewById(R.id.view_pager);
/*
        DisabledSwipeViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setEnableSwipe(false);*/

        viewPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(), 4));

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume(){
        Player.getInstance().setContext(this);
        Player.getInstance().setBottomViewHolder();
        super.onResume();
    }
}
