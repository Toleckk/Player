package com.example.tolek.player.search;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.R;
import com.example.tolek.player.SongRecyclerViewAdapter;
import com.example.tolek.player.debug.MediaStore;

import java.util.ArrayList;


public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.SongViewHolder>
        implements Filterable {

    private ArrayList<Song>  filtered;
    private Drawable musicArt;

    public SearchRecyclerViewAdapter(Drawable musicArt){
        filtered = new ArrayList<>();
        this.musicArt = musicArt;
    }


    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                filter(constraint);

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<Song>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public SongRecyclerViewAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card_view, parent, false);
        return new SongRecyclerViewAdapter.SongViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final SongRecyclerViewAdapter.SongViewHolder holder, int position) {
        holder.title.setText(filtered.get(position).getTitle());
        holder.artistAndAlbum.setText(filtered.get(position).getArtist() +
                " — " + filtered.get(position).getAlbum());

        Glide.with(holder.mainCardView.getContext())
                .load(filtered.get(position).getAlbumArt())
                .apply(new RequestOptions().placeholder(musicArt))
                .into(holder.albumArt);
        holder.song = filtered.get(position);
        holder.playlist = new ArrayList<Song>(){{add(filtered.get(holder.getAdapterPosition()));}};
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    private void filter(String string){
        filtered.clear();

        for(Song song : MediaStore.getInstance().getSongs())
            if(song.getTitle().toLowerCase().contains(string.toLowerCase()))
                filtered.add(song);
    }
}
