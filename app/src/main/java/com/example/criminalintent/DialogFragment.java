package com.example.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private static final String ARG_FILE = "file";

    public static DialogFragment newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILE, photoFile);

        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File photoFile = (File) getArguments().getSerializable(ARG_FILE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_crime_photo, null);

        ImageView imageView = (ImageView) v.findViewById(R.id.crime_photo_zoom);

        if (photoFile == null || !photoFile.exists()) {
            imageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
            imageView.setImageBitmap(bitmap);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }
}
