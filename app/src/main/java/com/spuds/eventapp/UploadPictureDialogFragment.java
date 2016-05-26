package com.spuds.eventapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.spuds.eventapp.Shared.MainActivity;

/**
 * Created by tina on 5/22/16.
 */
public class UploadPictureDialogFragment extends DialogFragment {

    public UploadPictureDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] optionsArr = {"Camera" , "Gallery"};

        builder.setTitle("Choose Action")
                .setItems(optionsArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                ((MainActivity)getActivity()).useCamera();
                                return;
                            case 1:
                                ((MainActivity)getActivity()).pickImage(true);
                                return;
                        }
                    }
                });

        return builder.create();
    }
}
