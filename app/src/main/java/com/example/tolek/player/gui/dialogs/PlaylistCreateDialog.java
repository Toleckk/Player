package com.example.tolek.player.gui.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tolek.player.gui.PlaylistCreatingActivity.PlaylistCreatingActivity;
import com.example.tolek.player.R;


public class PlaylistCreateDialog extends DialogFragment implements View.OnClickListener{

    Button buttonOk;
    EditText name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_playlist_creating, container, false);
        buttonOk = view.findViewById(R.id.button_ok);
        name = view.findViewById(R.id.playlist_name_text);

        buttonOk.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Editable timeText = name.getText();

        if(timeText != null && !timeText.toString().equals(""))
            getActivity().startActivityForResult(
                    new Intent(getActivity(), PlaylistCreatingActivity.class)
                    .putExtra("Name", name.getText().toString()), 0
            );

        dismiss();
    }

}
