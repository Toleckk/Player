package com.example.tolek.player;

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
import com.example.tolek.player.Entities.Song;
import com.example.tolek.player.debug.Player;

import java.util.ArrayList;

@Deprecated
public final class SongRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    ArrayList<Song> songsList;
    ArrayList<Song> filtered;
    boolean isQueryEmpty = true;
    Drawable musicArt;

    public SongRecyclerViewAdapter(ArrayList<Song> songsList, Drawable musicArt) {
        this.songsList = songsList;
        this.musicArt = musicArt;
        filtered = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card_view, parent, false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArrayList<Song> songsList = filtered.size() == 0 && isQueryEmpty
                ? this.songsList : filtered;

        SongViewHolder holder1 = (SongViewHolder) holder;


        holder1.title.setText(songsList.get(position).getTitle());
        holder1.artistAndAlbum.setText(songsList.get(position).getArtist() +
                " — " + songsList.get(position).getAlbum());

        Glide.with(holder1.mainCardView.getContext())
                .load(songsList.get(position).getAlbumArt())
                .apply(new RequestOptions().placeholder(musicArt))
                .into(holder1.albumArt);

        final Song song = songsList.get(position);
        holder1.song = song;
        holder1.playlist = filtered.size() == 0
                ? this.songsList
                : new ArrayList<Song>() {{add(song);}};
    }

    @Override
    public int getItemCount() {
        return filtered.size() == 0 && isQueryEmpty ? songsList.size() : filtered.size();
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
                filtered = (ArrayList<Song>) results.values;
                notifyDataSetChanged();
            }

            private void filterQuery(String string) {
                isQueryEmpty = string == null || string.equals("");
                filtered.clear();

                string = string.toLowerCase();
                for (Song song : songsList)
                    if (song.getTitle().toLowerCase().contains(string)
                            || song.getAlbum().toLowerCase().contains(string)
                            || song.getArtist().toLowerCase().contains(string))
                        filtered.add(song);
            }
        };
    }



    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public CardView mainCardView;
        public ImageView albumArt;
        public TextView title;
        public TextView artistAndAlbum;
        public Song song;
        public ArrayList<Song> playlist;

        public SongViewHolder(final View itemView) {
            super(itemView);

            mainCardView = itemView.findViewById(R.id.songCardView);
            albumArt = itemView.findViewById(R.id.song_art);
            title = itemView.findViewById(R.id.song_title);
            artistAndAlbum = itemView.findViewById(R.id.song_artist_and_album);

            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Player player = Player.getInstance();
                    if (player.getCurrentSong() != null && player.getCurrentSong().equals(song) && player.isPlaying())
                        player.pause();
                    else {
                        if (!player.getCurrentPlaylist().equals(playlist))
                            player.setCurrentPlaylist(new ArrayList<>(playlist));

                        player.play(song);
                    }
                }
            });
        }
    }
}
