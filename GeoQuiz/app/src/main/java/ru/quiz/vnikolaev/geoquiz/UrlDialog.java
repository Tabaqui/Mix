package ru.quiz.vnikolaev.geoquiz;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by User on 20.07.2016.
 */
public class UrlDialog extends DialogFragment {

    public static final String EXTRA_URL = "ru.quiz.vnikolaev.geoquiz";

    private EditText mUrlText;
    private DialogListener mDialogListener;

    public static UrlDialog newInstance(DialogListener listener) {
        UrlDialog fragment = new UrlDialog();
        fragment.mDialogListener = listener;
        return fragment;
    }

    public interface DialogListener {
        void onDialogResult(int resultCode, Intent intent);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_url, null);
        mUrlText = (EditText) v.findViewById(R.id.url_text);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_URL, mUrlText.getText().toString());
                        mDialogListener.onDialogResult(Activity.RESULT_OK, intent);
                        dismiss();
                    }
                })
                .create();
    }
}
