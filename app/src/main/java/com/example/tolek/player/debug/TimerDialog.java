package com.example.tolek.player.debug;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.example.tolek.player.R;
import com.example.tolek.player.Util.SleepTimer;

public class TimerDialog extends DialogFragment implements Button.OnClickListener{

    Button buttonOk;
    EditText time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog, container, false);
        buttonOk = view.findViewById(R.id.button_ok);
        time = view.findViewById(R.id.time_text);
        time.setText(String.valueOf(60));

        buttonOk.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Editable timeText = time.getText();

        if(timeText != null && !timeText.toString().equals("")) {
            SleepTimer.getInstance().start(Integer.valueOf(timeText.toString()) * 1000 * 60);
            ((Button)getActivity().findViewById(R.id.timer_button)).setText(timeText.toString());
        }

        dismiss();
    }

/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d("MYTAG",
                String.valueOf(
                        getActivity().findViewById(R.id.alert_dialog_layout) == null
                )
        );

        View view = getLayoutInflater().inflate(R.layout.alert_dialog,
                (ViewGroup) getActivity().findViewById(R.id.alert_dialog_layout),
                false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Sas");
        builder.setView(view);

        builder.setMessage("Ok")
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }
*/
}