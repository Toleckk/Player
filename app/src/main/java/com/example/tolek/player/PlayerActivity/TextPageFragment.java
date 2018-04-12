package com.example.tolek.player.PlayerActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tolek.player.R;
import com.example.tolek.player.debug.Logger;

public class TextPageFragment extends Fragment {
    View mainView;
    String text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.text, container, false);

        if(text != null)
            setText(text);

        return mainView;
    }

    public void setText(String text) {
        Logger.log(mainView != null);
        if(mainView != null) {
            TextView textView = mainView.findViewById(R.id.text);
            Logger.log(textView != null);
            if (text != null && !text.equals("")) {
                textView.setText(text);
                textView.setMovementMethod(new ScrollingMovementMethod());
            } else
                textView.setText("No text for this song");
        }
        else
            this.text = text;
    }
}
