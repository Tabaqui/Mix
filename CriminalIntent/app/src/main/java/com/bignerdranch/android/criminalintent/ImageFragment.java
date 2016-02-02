package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by vnikolaev on 01.02.2016.
 */
public class ImageFragment extends DialogFragment {

    private static final String ARG_PHOTO = "photo";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_image, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.crime_full_photo);
        String photoPath = (String) getArguments().getSerializable(ARG_PHOTO);
        Uri photoUri = Uri.parse(photoPath);
        Bitmap bitmap = PictureUtils.getScaledBitmap(photoUri.getPath(), getActivity());
        imageView.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    public static ImageFragment newInstance(Uri imageUri) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, imageUri.toString());
        imageFragment.setArguments(args);
        return imageFragment;
    }
}
