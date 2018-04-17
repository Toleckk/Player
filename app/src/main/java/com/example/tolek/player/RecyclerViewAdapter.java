package com.example.tolek.player;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.tolek.player.Entities.Entity;
import com.example.tolek.player.Entities.ViewCreatingStrategy;
import com.example.tolek.player.debug.Logger;

import java.util.ArrayList;


public class RecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private ArrayList<Entity> list;
    private ArrayList<Entity> filtered;
    private boolean isQueryEmpty = true;
    private Drawable art;
    private int layoutID;
    private ViewCreatingStrategy viewCreatingStrategy;

    public RecyclerViewAdapter(ArrayList<Entity> list, Drawable art, int layoutID,
                               ViewCreatingStrategy viewCreatingStrategy) {
        this.list = list;
        this.art = art;
        this.layoutID = layoutID;
        this.viewCreatingStrategy = viewCreatingStrategy;
        filtered = new ArrayList<>();

        Logger.log(list.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return viewCreatingStrategy.createView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArrayList<Entity> list = filtered.size() == 0 && isQueryEmpty
                ? this.list : filtered;

        list.get(position).createViewHolder(holder, art, list);
    }

    @Override
    public int getItemCount() {
        return filtered.size() == 0 && isQueryEmpty ? list.size() : filtered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String string = constraint.toString();
                isQueryEmpty = string.equals("");
                filtered.clear();

                for (Entity entity : list)
                    if(entity.checkQuery(string))
                        filtered.add(entity);

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<Entity>) results.values;
                notifyDataSetChanged();
            }

        };
    }
}
