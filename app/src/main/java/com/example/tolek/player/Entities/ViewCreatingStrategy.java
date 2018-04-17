package com.example.tolek.player.Entities;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public interface ViewCreatingStrategy {
    RecyclerView.ViewHolder createView(View view);
}
