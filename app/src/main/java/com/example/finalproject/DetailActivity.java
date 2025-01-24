package com.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    private TextView articleTitle, articleSection, articleUrl;
    private ImageView articleThumbnail;
    private DatabaseHelper databaseHelper;
    private boolean isArticleSaved = false;
    private int articleId; // For database reference
    private String title, section, url, thumbnailUrl; // Article details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Article Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);

        // Set up Article Details
        articleTitle = findViewById(R.id.articleTitle);
        articleSection = findViewById(R.id.articleSection);
        articleUrl = findViewById(R.id.articleUrl);
        articleThumbnail = findViewById(R.id.articleThumbnail);

        Intent intent = getIntent();
        articleId = intent.getIntExtra("id", -1);
        title = intent.getStringExtra("title");
        section = intent.getStringExtra("section");
        url = intent.getStringExtra("url");
        thumbnailUrl = intent.getStringExtra("thumbnail");

        // Generate a fallback ID if the ID is invalid
        if (articleId == -1 && title != null && url != null) {
            articleId = (title + url).hashCode(); // Generate a unique ID
            Log.w(TAG, "Invalid article ID. Generated fallback ID: " + articleId);
        }

        // Help Button Setup
        FloatingActionButton helpButton = findViewById(R.id.help_details);
        helpButton.setOnClickListener(v -> showHelpDialog());

        // Log details for debugging
        Log.d(TAG, "Received Article ID: " + articleId);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Section: " + section);
        Log.d(TAG, "URL: " + url);

        if (title != null) articleTitle.setText(title);
        if (section != null) articleSection.setText(section);
        if (url != null) {
            articleUrl.setText(url);
            articleUrl.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            });
        }

        // Save the article to history if it has valid details
        if (title != null && section != null && url != null) {
            boolean savedToHistory = databaseHelper.saveToHistory(title, section, url);
            Log.d(TAG, "Article saved to history: " + savedToHistory);
        }

        if (thumbnailUrl != null) {
            new LoadImageTask(articleThumbnail).execute(thumbnailUrl);
        }

        // Check if the article is already saved
        isArticleSaved = databaseHelper.isArticleSavedById(articleId);

        Log.d(TAG, "isArticleSaved: " + isArticleSaved);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_details, menu);

        // Update the icon based on the saved state
        MenuItem favoriteItem = menu.findItem(R.id.action_save);
        updateFavoriteIcon(favoriteItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            handleSaveButtonClick(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSaveButtonClick(MenuItem item) {
        if (articleId == -1) {
            Log.w(TAG, "Invalid article ID. Cannot save or remove.");
            showSnackbar(getString(R.string.snackbarDetail));
            return;
        }

        if (!isArticleSaved) {
            // Save the article to favorites
            boolean success = databaseHelper.saveArticle(articleId, title, section, url);
            if (success) {
                isArticleSaved = true;
                updateFavoriteIcon(item);
                showSnackbar(getString(R.string.snackbarAddFavourite));
            } else {
                showSnackbar(getString(R.string.errorAddFavourite));
            }
        } else {
            // Prompt the user before removing
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.removeFavourite))
                    .setMessage(getString(R.string.deleteConfirmation))
                    .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                        boolean success = databaseHelper.deleteArticleById(articleId);
                        if (success) {
                            isArticleSaved = false;
                            updateFavoriteIcon(item);
                            showSnackbar(getString(R.string.snackbarRemoveFavourite));
                        } else {
                            showSnackbar(getString(R.string.errorRemoveFavourite));
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
    }

    private void updateFavoriteIcon(MenuItem item) {
        item.setIcon(isArticleSaved ? R.drawable.ic_heartfill : R.drawable.ic_heartoutline);
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.helpDetailsTitle))
                .setMessage(getString(R.string.helpDetailsMessage))
                .setPositiveButton(getString(R.string.confirm), null)
                .show();
    }


    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            try {
                URL imageUrl = new URL(url);
                return BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.placeholder_image); // Fallback image
            }
        }
    }
}
