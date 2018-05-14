package com.example.tolek.player.gui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;

import com.example.tolek.player.R;
import com.example.tolek.player.Repository.EntityKeeper;
import com.example.tolek.player.Util.FileWorker;
import com.example.tolek.player.debug.Logger;
import com.example.tolek.player.domain.Entities.Entity;
import com.example.tolek.player.domain.Entities.Playlist;
import com.example.tolek.player.domain.Entities.Song;
import com.example.tolek.player.domain.Player;
import com.example.tolek.player.gui.dialogs.DeletingDialog;
import com.example.tolek.player.gui.dialogs.contextMenu.PlaylistAddingDialog;
import com.example.tolek.player.gui.viewHolder.CheckableViewHolder;
import com.example.tolek.player.gui.viewHolder.SongView;

import java.io.File;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<CheckableViewHolder> implements Filterable {
    private ArrayList<? extends Entity> list;
    private ArrayList<Entity> filtered;
    private boolean isQueryEmpty = true;
    private int layoutID;
    private Class viewHolderType;
    private ArrayList<Boolean> checkedList;
    public boolean checkable;
    private Activity context;

    public RVAdapter(Activity context, EntityKeeper<? extends Entity> repository, int layoutID,
                     Class<? extends CheckableViewHolder> viewHolderType, boolean checkable) {
        this.list = repository.getList();

        this.layoutID = layoutID;

        this.viewHolderType = viewHolderType;
        this.checkable = checkable;
        this.context = context;

        filtered = new ArrayList<>();
        checkedList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++)
            checkedList.add(false);
    }

    public RVAdapter(Activity context, EntityKeeper<? extends Entity> repository, int layoutID,
                     Class<? extends CheckableViewHolder> viewHolderType) {
        this(context, repository, layoutID, viewHolderType, false);
    }


    @NonNull
    @Override
    public CheckableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);

        try {
            return (CheckableViewHolder) viewHolderType
                    .getDeclaredConstructor(View.class, RVAdapter.class)
                    .newInstance(v, this);
        } catch (Exception e) {
            //STUB: NEVER
            return null;
        }
    }

    public void onBindViewHolder(@NonNull CheckableViewHolder holder, int position) {
        ArrayList<? extends Entity> list = filtered.size() == 0 && isQueryEmpty
                ? this.list : filtered;

        final int pos = holder.getAdapterPosition();
        holder.fillFromEntity(list.get(position), list, checkable,
                checkedList.get(position), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkedList.set(pos, !checkedList.get(pos));
                        ImageButton checkBox = null;

                        if (v instanceof ImageButton)
                            checkBox = (ImageButton) v;
                        else if (v instanceof CardView)
                            checkBox = v.findViewById(R.id.checkBox);

                        if (checkBox != null)
                            checkBox.setImageResource(checkedList.get(pos)
                                    ? R.drawable.ic_check_circle_black_40dp
                                    : R.drawable.ic_radio_button_unchecked_black_24dp);
                    }
                });
    }

    public void select(int position) {
        if (!checkable) {
            checkable = true;

            ((HaveContextMenu) context).createContextMenuView(new OnContextMenuItemSelected());

            ((HaveContextMenu) context).setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public void onBackPressed() {
                    checkable = false;
                    for (int i = 0; i < checkedList.size(); i++)
                        checkedList.set(i, false);
                    notifyDataSetChanged();

                    ((HaveContextMenu) context).hideContextMenu();
                    ((HaveContextMenu) context).setOnBackPressedListener(null);
                }
            });

            notifyDataSetChanged();
        }

        checkedList.set(position, true);
    }

    @Override
    public int getItemCount() {
        return filtered.size() == 0 && isQueryEmpty ? list.size() : filtered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String string = constraint.toString();
                isQueryEmpty = string.equals("");
                filtered.clear();

                for (Entity entity : list)
                    if (entity.checkQuery(string))
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

    public ArrayList<? extends Entity> getSelected() {
        ArrayList<Entity> selected = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            if (checkedList.get(i))
                selected.add(list.get(i));

        return selected;
    }

    public void notifyElementAdded() {
        checkedList.add(false);
        notifyDataSetChanged();
    }

    public Activity getContext() {
        return context;
    }

    public void notifyDeletingConfirmed(){
        ArrayList<Entity> removed = new ArrayList<>();

        for(Entity entity : getSelected())
            removed.add(entity);

        list.removeAll(removed);
        notifyDataSetChanged();

        for(Entity entity : removed)
            FileWorker.delete(entity);
    }

    private class OnContextMenuItemSelected implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.context_play: {
                    ArrayList<Song> songs = new ArrayList<>();
                    if (getSelected().size() > 0 && getSelected().get(0) instanceof EntityKeeper)
                        for (Entity entityKeeper : getSelected())
                            songs.addAll(((EntityKeeper) entityKeeper).getList());
                    else
                        songs.addAll((ArrayList<Song>) getSelected());

                    Player.getInstance().play(new Playlist(songs));

                    context.onBackPressed();
                    return;
                }
                case R.id.context_add: {
                    PlaylistAddingDialog dialog = new PlaylistAddingDialog();
                    dialog.setAdapter(RVAdapter.this);
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Add");
                    return;
                }
                case R.id.context_insert: {
                    ArrayList<Song> songs = new ArrayList<>();
                    if (getSelected().size() > 0 && getSelected().get(0) instanceof EntityKeeper)
                        for (Entity entityKeeper : getSelected())
                            songs.addAll(((EntityKeeper) entityKeeper).getList());
                    else
                        songs.addAll((ArrayList<Song>) getSelected());

                    Player.getInstance().getCurrentPlaylist().getList()
                            .addAll(
                                    Player.getInstance().getCurrentPlaylist().getList().indexOf(
                                            Player.getInstance().getCurrentSong()
                                    ),
                                    songs
                            );

                    context.onBackPressed();
                    return;
                }
                case R.id.context_send: {
                    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    intent.setType("audio/*");
                    ArrayList<Uri> files = new ArrayList<>();
                    for (int i = 0; i < checkedList.size(); i++)
                        if (checkedList.get(i)) {
                            if (list.get(i) instanceof Song)
                                files.add(Uri.parse("file://" + ((Song) list.get(i)).getPath()));
                            else
                                for (Song song : ((EntityKeeper<Song>) list.get(i)).getList())
                                    files.add(Uri.parse("file://" + song.getPath()));
                        }
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                    context.startActivity(Intent.createChooser(intent, "Share with"));
                    return;
                }
                case R.id.context_delete: {
                    DeletingDialog dialog = new DeletingDialog();
                    dialog.setAdapter(RVAdapter.this);
                    dialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "Deleting");
                    return;
                }
            }
        }
    }
}
