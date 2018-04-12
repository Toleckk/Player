package com.example.tolek.player.debug;

import android.util.Log;

public class Logger {
    private Logger(){}
    public static void log(String message){
        Log.d("MyPlayer", message);
    }

    public static void log(boolean message){
        log(String.valueOf(message));
    }
}
