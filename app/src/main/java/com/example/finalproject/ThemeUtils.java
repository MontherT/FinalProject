package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for handling theme preferences and applying the selected theme to the application.
 */
public class ThemeUtils {

    private static final String PREFERENCES_NAME = "AppSettings";  // Name of the SharedPreferences file
    private static final String THEME_KEY = "app_theme";  // Key for storing the theme preference

    public static final String THEME_LIGHT = "light";  // Constant for light theme
    public static final String THEME_DARK = "dark";  // Constant for dark theme

    /**
     * Applies the saved theme to the provided context.
     *
     * @param context The context to which the theme should be applied.
     */
    public static void applyTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String theme = preferences.getString(THEME_KEY, THEME_LIGHT);

        if (theme.equals(THEME_DARK)) {
            context.setTheme(R.style.AppTheme_Dark);
        } else {
            context.setTheme(R.style.AppTheme_Light);
        }
    }

    /**
     * Saves the selected theme preference.
     *
     * @param context The context used to access the preferences.
     * @param theme   The theme to be saved, either {@link #THEME_LIGHT} or {@link #THEME_DARK}.
     */
    public static void setTheme(Context context, String theme) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(THEME_KEY, theme);
        editor.apply();
    }

    /**
     * Retrieves the current theme preference.
     *
     * @param context The context used to access the preferences.
     * @return The current theme preference, either {@link #THEME_LIGHT} or {@link #THEME_DARK}.
     */
    public static String getTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(THEME_KEY, THEME_LIGHT);
    }
}
