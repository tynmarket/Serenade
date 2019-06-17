package com.tynmarket.serenade.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.tynmarket.serenade.R;

import static android.content.Context.MODE_PRIVATE;

public class PrivacyPolicyFragment extends AppCompatDialogFragment {
    public static final String TAG = "privacy_policy";
    public static final String PREF_NAME = "privacy_policy";
    public static final String AGREE = "agree";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.privacy_title)
                .setMessage(R.string.privacy_message)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), R.string.text_ok, Toast.LENGTH_SHORT).show();
                        SharedPreferences pref = getContext().getSharedPreferences(PrivacyPolicyFragment.PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(PrivacyPolicyFragment.AGREE, true);
                        editor.apply();
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), R.string.text_cancel, Toast.LENGTH_SHORT).show();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
