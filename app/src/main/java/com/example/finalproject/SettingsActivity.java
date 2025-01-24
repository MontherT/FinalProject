package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme based on saved preference
        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the navigation button
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        // Find UI Elements
        switchDarkMode = findViewById(R.id.switch_dark_mode);

        // Set current state of dark mode switch
        String currentTheme = ThemeUtils.getTheme(this);
        switchDarkMode.setChecked(currentTheme.equals(ThemeUtils.THEME_DARK));

        // Save preference on toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newTheme = isChecked ? ThemeUtils.THEME_DARK : ThemeUtils.THEME_LIGHT;
            ThemeUtils.setTheme(this, newTheme);

            // Restart the app to apply the new theme
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
