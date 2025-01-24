package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeUtils {

    private static final String PREFERENCES_NAME = "AppSettings";
    private static final String THEME_KEY = "app_theme";

    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";

    // Apply theme to the current context
    public static void applyTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String theme = preferences.getString(THEME_KEY, THEME_LIGHT);

        if (theme.equals(THEME_DARK)) {
            context.setTheme(R.style.AppTheme_Dark);
        } else {
            context.setTheme(R.style.AppTheme_Light);
        }
    }

    // Set theme preference
    public static void setTheme(Context context, String theme) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(THEME_KEY, theme);
        editor.apply();
    }

    // Get current theme preference
    public static String getTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(THEME_KEY, THEME_LIGHT);
    }
}
