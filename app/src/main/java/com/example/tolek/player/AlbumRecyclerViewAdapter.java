package com.example.tolek.player;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tolek.player.Entities.Album;
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.AlbumActivity.AlbumActivity;
import com.example.tolek.player.debug.MediaStore;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Deprecated
public final class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumViewHolder>
        implements Filterable{

    ArrayList<Album> albumsList;
    private boolean isQueryEmpty = true;
    ArrayList<Album> filtered;
    Drawable musicArt;

    public AlbumRecyclerViewAdapter(ArrayList<Album> albumsList, Drawable musicArt) {
        this.musicArt = musicArt;
        this.albumsList = albumsList;
        filtered = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_view, parent, false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(v);
        return albumViewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        final ArrayList<Album> albumsList = filtered.size() == 0 && isQueryEmpty ? this.albumsList : filtered;

        holder.albumText.setText(albumsList.get(position).getAlbumName());
        holder.artist.setText(albumsList.get(position).getArtist());

        Glide.with(holder.mainCardView.getContext())
                .load(albumsList.get(position).getCover())
                .apply(new RequestOptions().placeholder(musicArt))
                .into(holder.albumArt);

        holder.album = albumsList.get(position);
    }

    @Override
    public int getItemCount() {
        return filtered.size() == 0 && isQueryEmpty ? albumsList.size() : filtered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filterQuery(constraint.toString());

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<Album>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void filterQuery(String string){
        isQueryEmpty = string == null || string.equals("");
        filtered.clear();

        string = string.toLowerCase();
        for (Album album : albumsList)
            if (album.getAlbumName().toLowerCase().contains(string)
                    || album.getArtist().toLowerCase().contains(string))
                filtered.add(album);
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {

        CardView mainCardView;
        ImageView albumArt;
        TextView albumText;
        TextView artist;
        Album album;

        public AlbumViewHolder(View itemView) {
            super(itemView);

            mainCardView = itemView.findViewById(R.id.albumCardView);
            albumArt = itemView.findViewById(R.id.album_art);
            albumText = itemView.findViewById(R.id.album);
            artist = itemView.findViewById(R.id.album_artist);

            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(
                            new Intent(view.getContext(), AlbumActivity.class)
                                .putExtra("Album",
                                        MediaStore.getInstance().getAlbums().indexOf(album))
                    );
                }
            });
        }
    }
}

//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            (Activity)view.getContext(),
//                            new Pair<>(((Activity)view.getContext())
//                                            .findViewById(R.id.bottom_card_view),
//                                        view.getContext().getString(R.string.transition_name_bottom))
//                    );
//
//                    ((Activity)view.getContext()).startActivity(
//                            new Intent(view.getContext(), AlbumActivity.class)
//                                .putExtra("Album",FileWorker.getAlbums().indexOf(album)),
//                            options.toBundle());
