package com.example.tolek.player.MainActivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.tolek.player.PageFragmentAdapter;
import com.example.tolek.player.R;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.debug.Player;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FileWorker.launch();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));

        TabLayout tabLayout = findViewById(R.id.tab);
        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(), 4));

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume(){
        Player.getInstance().setPlayerViewHolder(null);
        Player.getInstance().setBottomViewHolder(new BottomViewHolder(this));
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        Player.getInstance().setBottomViewHolder(null);
        super.onDestroy();
    }
}
