package com.example.tolek.player.gui.viewHolder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.example.tolek.player.R;
import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.gui.RVAdapter;

import java.util.ArrayList;


public abstract class CheckableViewHolder extends RecyclerView.ViewHolder implements Checkable {
    public CardView mainCardView;
    public ImageButton checkBox;
    protected RVAdapter adapter;

    public CheckableViewHolder(View itemView, CardView mainCardView, RVAdapter adapter) {
        super(itemView);

        checkBox = itemView.findViewById(R.id.checkBox);
        this.adapter = adapter;
        this.mainCardView = mainCardView;

        mainCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CheckableViewHolder.this.adapter.select(
                        CheckableViewHolder.this.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public void setChecked(boolean isChecked) {
        if(isChecked)
            checkBox.setVisibility(View.VISIBLE);

        checkBox.setImageResource(isChecked
                ? R.drawable.ic_check_circle_black_40dp
                : R.drawable.ic_radio_button_unchecked_black_24dp);
    }


    public abstract int getLayoutID();

    public abstract Drawable getDefaultArt();

    public abstract void fillFromEntity(Entity entity, ArrayList list, boolean checkable,
                                        boolean isChecked, View.OnClickListener checkBoxListener);
}
