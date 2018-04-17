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
import com.example.tolek.player.AlbumActivity.AlbumActivity;
import com.example.tolek.player.Entities.Artist;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.debug.MediaStore;

import java.util.ArrayList;

@Deprecated
public final class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.ArtistViewHolder>
        implements Filterable{

    ArrayList<Artist> artistsList;
    boolean isQueryEmpty = true;
    ArrayList<Artist> filtered;
    Drawable art;

    public ArtistRecyclerViewAdapter(ArrayList<Artist> artistsList, Drawable art){
        this.artistsList = artistsList;
        this.art = art;
        filtered = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_card_view, parent, false);
        ArtistViewHolder songViewHolder = new ArtistViewHolder(v);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        ArrayList<Artist> artistsList = filtered.size() == 0 && isQueryEmpty? this.artistsList : filtered;

        holder.artistText.setText(artistsList.get(position).getName());
        holder.tracks.setText(String.valueOf(artistsList.get(position).getSongs().size()));
        holder.artist = artistsList.get(position);

        Glide.with(holder.mainCardView.getContext())
                .load(artistsList.get(position).getArt())
                .apply(new RequestOptions().placeholder(art))
                .into(holder.art);
    }

    @Override
    public int getItemCount() {
        return filtered.size() == 0 && isQueryEmpty ? artistsList.size() : filtered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filterQuery(constraint.toString());

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<Artist>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void filterQuery(String string){
        isQueryEmpty = string == null || string.equals("");
        filtered.clear();

        for (Artist artist : artistsList)
            if(artist.getName().toLowerCase().contains(string.toLowerCase()))
                filtered.add(artist);
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder{

        CardView mainCardView;
        TextView artistText;
        TextView tracks;
        ImageView art;
        Artist artist;

        public ArtistViewHolder(View itemView){
            super(itemView);

            mainCardView = itemView.findViewById(R.id.artistCardView);
            artistText = itemView.findViewById(R.id.artist);
            tracks = itemView.findViewById(R.id.tracks_quantity);
            art = itemView.findViewById(R.id.artist_art);

            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(
                            new Intent(view.getContext(), AlbumActivity.class)
                                    .putExtra("Artist",
                                            MediaStore.getInstance().getArtists().indexOf(artist))
                    );
                }
            });
        }
    }
}
