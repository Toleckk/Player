package com.example.tolek.player.gui;

import android.view.View;

import com.example.tolek.player.gui.viewHolder.ContextMenuViewHolder;


public interface HaveContextMenu {
    void setOnBackPressedListener(OnBackPressedListener onBackPressedListener);
    void hideContextMenu();
    ContextMenuViewHolder createContextMenuView(View.OnClickListener listener);
}
