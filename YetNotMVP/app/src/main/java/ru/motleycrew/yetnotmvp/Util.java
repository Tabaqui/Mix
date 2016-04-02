package ru.motleycrew.yetnotmvp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by vas on 19.03.16.
 */
public class Util {

    public static void showText(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
