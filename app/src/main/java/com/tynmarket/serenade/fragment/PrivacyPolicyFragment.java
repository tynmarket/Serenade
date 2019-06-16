package com.tynmarket.serenade.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.tynmarket.serenade.R;

public class PrivacyPolicyFragment extends AppCompatDialogFragment {
    public static final String TAG = "privacy_policy";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.privacy_title)
                .setMessage(R.string.privacy_message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() { // R.string.fire
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() { // R.string.cancel
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "ng", Toast.LENGTH_SHORT).show();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
