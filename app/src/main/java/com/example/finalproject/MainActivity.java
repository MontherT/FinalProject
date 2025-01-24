package com.example.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private LinearLayout landingPageContainer;
    private FrameLayout fragmentContainer;
    private ArticleListFragment articleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme
        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        landingPageContainer = findViewById(R.id.landingPageContainer);
        fragmentContainer = findViewById(R.id.fragment_list);

        // Initialize the fragment
        articleListFragment = new ArticleListFragment();

        // Floating Action Button
        FloatingActionButton helpButton = findViewById(R.id.help_main);
        helpButton.setOnClickListener(v -> showHelpDialog());

        // Initialize Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Set up custom ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        toggle.setDrawerIndicatorEnabled(false); // Disable default hamburger icon
        toolbar.setNavigationIcon(R.drawable.ic_menu); // Use your custom icon
        toolbar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
            } else {
                drawerLayout.openDrawer(navigationView);
            }
        });

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getOrder()) {
                case 1: // Home
                    drawerLayout.closeDrawers();
                    return true;
                case 2: // Saved Articles
                    startActivity(new Intent(MainActivity.this, SavedArticlesActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                case 3: // History
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                case 4: // Settings
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                default:
                    return false;
            }
        });

        // Initialize Fragments
        if (savedInstanceState == null) {
            articleListFragment = new ArticleListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_list, articleListFragment)
                    .commit();
        }
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.help))
                .setMessage(getString(R.string.helpInstructions))
                .setPositiveButton(getString(R.string.confirm), null)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Handle search action directly in the toolbar
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search and update the ArticleListFragment
                if (!query.isEmpty()) {
                    new FetchArticlesTask().execute(query);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.searchTerm), Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private class FetchArticlesTask extends AsyncTask<String, Void, String> {

        private ArrayList<String> articleTitles = new ArrayList<>();
        private ArrayList<String> articleUrls = new ArrayList<>();
        private ArrayList<String> articleSections = new ArrayList<>();
        private ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);  // Show the ProgressBar before fetching data
        }

        @Override
        protected String doInBackground(String... params) {
            String query = params[0];
            String apiUrl = "https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949&q=" + query;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                // Hide landing page and show search results
                landingPageContainer.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);

                // Load the fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_list, articleListFragment)
                        .commit();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONArray results = response.getJSONArray("results");

                    articleTitles.clear();
                    articleUrls.clear();
                    articleSections.clear();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject article = results.getJSONObject(i);
                        String title = article.getString("webTitle");
                        String url = article.getString("webUrl");
                        String section = article.getString("sectionName");

                        articleTitles.add(title);
                        articleUrls.add(url);
                        articleSections.add(getString(R.string.articleSection) + section);
                    }

                    if (articleListFragment != null) {
                        articleListFragment.updateArticles(articleTitles, articleSections, articleUrls);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, getString(R.string.errorParsing), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.errorFetching), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar.setVisibility(View.GONE); // Hide ProgressBar on cancellation
        }
    }
}
