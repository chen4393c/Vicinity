package com.chen4393c.vicinity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

import com.chen4393c.vicinity.settings.SettingsActivity;

public class QueryPreferences {

    private static final String PREF_THEME_QUERY = "themeQuery";

    public static String getStoredTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_THEME_QUERY, null);
    }

    public static void setStoredTheme(Context context, int themeIndex) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_THEME_QUERY, themeIndex)
                .apply();
    }
}
