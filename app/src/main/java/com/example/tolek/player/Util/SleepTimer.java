package com.example.tolek.player.Util;


import com.example.tolek.player.debug.Player;

import java.util.Timer;

public class SleepTimer {
    private Timer timer;
    private boolean launched;


    private class TimerTask extends java.util.TimerTask{

        @Override
        public void run() {
            Player.getInstance().pause();
            launched = false;
        }
    }

    static private SleepTimer instance = new SleepTimer();

    static public SleepTimer getInstance(){
        return instance;
    }

    private SleepTimer(){
        launched = false;
    }

    public void start(int time){
        timer = new Timer();
        timer.schedule(new TimerTask(), time);
        launched = true;
    }

    public void stop(){
        timer.cancel();
        launched = false;
    }

    public boolean isLaunched() {
        return launched;
    }
}
