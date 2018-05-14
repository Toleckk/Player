package com.example.tolek.player.gui.viewHolder;


import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.tolek.player.R;

public class ContextMenuViewHolder {
    public ImageButton play;
    public ImageButton add;
    public ImageButton insert;
    public ImageButton send;
    public ImageButton delete;

    public ContextMenuViewHolder(Activity context, View.OnClickListener listener){
        play = context.findViewById(R.id.context_play);
        add = context.findViewById(R.id.context_add);
        insert = context.findViewById(R.id.context_insert);
        send = context.findViewById(R.id.context_send);
        delete = context.findViewById(R.id.context_delete);

        play.setOnClickListener(listener);
        add.setOnClickListener(listener);
        insert.setOnClickListener(listener);
        send.setOnClickListener(listener);
        delete.setOnClickListener(listener);
    }

    public ContextMenuViewHolder(View context, View.OnClickListener listener){
        play = context.findViewById(R.id.context_play);
        add = context.findViewById(R.id.context_add);
        insert = context.findViewById(R.id.context_insert);
        send = context.findViewById(R.id.context_send);
        delete = context.findViewById(R.id.context_delete);

        play.setOnClickListener(listener);
        add.setOnClickListener(listener);
        insert.setOnClickListener(listener);
        send.setOnClickListener(listener);
        delete.setOnClickListener(listener);
    }
}
