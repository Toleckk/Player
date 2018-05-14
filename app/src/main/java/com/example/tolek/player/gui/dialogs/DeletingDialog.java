package com.example.tolek.player.gui.dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolek.player.R;
import com.example.tolek.player.gui.RVAdapter;

public class DeletingDialog extends DialogFragment implements View.OnClickListener{

    private RVAdapter adapter;

    public void setAdapter(RVAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_deleting, container, false);

        getDialog().setTitle("Deleting...");

        view.findViewById(R.id.button_yes).setOnClickListener(this);
        view.findViewById(R.id.button_no).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_yes)
            adapter.notifyDeletingConfirmed();

        adapter.getContext().onBackPressed();
        dismiss();
    }
}
