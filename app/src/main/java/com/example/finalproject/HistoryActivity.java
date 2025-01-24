package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import android.view.Menu;
import android.view.MenuItem;

public class HistoryActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme
        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the navigation button
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // Custom menu icon
        }

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout_history);

        // Set up NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view_history);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getOrder()) {
                case 1: // Home
                    startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                case 2: // Saved Articles
                    startActivity(new Intent(HistoryActivity.this, SavedArticlesActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                case 3: // Stay on History
                    drawerLayout.closeDrawers();
                    return true;
                case 4: // Settings
                    startActivity(new Intent(HistoryActivity.this, SettingsActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                default:
                    return false;
            }
        });

        // Handle Navigation Icon Click
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        // Add HistoryFragment to the container
        FragmentManager fragmentManager = getSupportFragmentManager();
        historyFragment = new HistoryFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_history, historyFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_history, menu); // Ensure toolbar_history is correct
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_history) {
            if (historyFragment != null) {
                historyFragment.clearHistory(); // Notify the fragment to clear the history
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
