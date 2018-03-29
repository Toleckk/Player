package com.example.tolek.player.Widgets;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.view.View;

import com.example.tolek.player.PageFragmentAdapter;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.Util.Player;
import com.example.tolek.player.PlayerActivity.PlayerActivity;

public class MainActivity extends AppCompatActivity {
    private final String MYTAG = "MYTAG";
    private Transition transition;
    private Scene scene1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Player.getInstance().launch(this);
        FileWorker.setContentResolver(getContentResolver());
        FileWorker.launch();

        TabLayout tabLayout = findViewById(R.id.tab);

        ViewPager viewPager = findViewById(R.id.view_pager);
/*
        DisabledSwipeViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setEnableSwipe(false);*/

        viewPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(), 4));

        findViewById(R.id.bottom_card_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), PlayerActivity.class));
                overridePendingTransition(R.anim.animation_player_show, R.anim.animation_main_hide);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }
}
