package me.creese.file.magic.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by scnipper on 21.05.2018.
 */

public class Saves {
    private static Saves instanse;
    public SharedPreferences getPref;
    public SharedPreferences.Editor save;

    private final static String PREF_NAME = "fmagic_prefs";

    private Saves(Context context) {
        getPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        save = getPref.edit();
    }

    public static void init(Context context) {
        instanse = new Saves(context);
    }

    public static Saves getInstanse() {
        return instanse;
    }
}
