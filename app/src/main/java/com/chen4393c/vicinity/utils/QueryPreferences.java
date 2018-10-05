package com.chen4393c.vicinity.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.chen4393c.vicinity.settings.SettingsActivity;

public class QueryPreferences {

    public static int getThemeIndex(Context context) {
        SharedPreferences preferences = getGeneralPreferences(context);
        int themeIndex;
        try {
            themeIndex = Integer.parseInt(preferences.getString("theme_list", "0"));
        } catch (NumberFormatException e) {
            themeIndex = 0;
        }
        return themeIndex;
    }

    public static String getDisplayName(Context context) {
        SharedPreferences preferences = getGeneralPreferences(context);
        return preferences.getString("display_name_text", null);
    }

    private static SharedPreferences getGeneralPreferences(Context context) {
        return context.getSharedPreferences(
                SettingsActivity.GeneralPreferenceFragment.SETTINGS_SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);
    }
}
