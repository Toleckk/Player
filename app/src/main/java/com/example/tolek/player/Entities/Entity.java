package com.example.tolek.player.Entities;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public abstract class Entity implements Searchable, Viewable{
    public abstract RecyclerView.ViewHolder createViewHolder(RecyclerView.ViewHolder v, Drawable art, ArrayList<Entity> list);
}
