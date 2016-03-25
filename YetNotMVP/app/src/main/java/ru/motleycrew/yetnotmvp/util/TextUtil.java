package ru.motleycrew.yetnotmvp.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by User on 21.03.2016.
 */
public class TextUtil {

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
