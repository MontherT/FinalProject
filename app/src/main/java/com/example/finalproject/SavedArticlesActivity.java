package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class SavedArticlesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme
        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_saved_articles);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Saved Articles");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the navigation button
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // Set custom icon
            toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.navigation_view_saved)));
        }

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout_saved);

        // Set up NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view_saved);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getOrder()) {
                case 1:
                    startActivity(new Intent(SavedArticlesActivity.this, MainActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                case 2:
                    // Stay on this activity
                    drawerLayout.closeDrawers();
                    return true;
                case 3:
                    startActivity(new Intent(SavedArticlesActivity.this, HistoryActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                case 4:
                    startActivity(new Intent(SavedArticlesActivity.this, SettingsActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                default:
                    return false;
            }
        });

        // Initialize the FloatingActionButton
        FloatingActionButton helpButton = findViewById(R.id.help_saved);
        helpButton.setOnClickListener(v -> showHelpDialog());

        // Add SavedArticlesFragment to the container
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_saved, new SavedArticlesFragment())
                .commit();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.helpArticlesTitle))
                .setMessage(getString(R.string.helpArticlesMessage))
                .setPositiveButton(getString(R.string.confirm), null)
                .show();
    }

}
