package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SavedArticlesFragment extends Fragment {

    private ListView savedArticlesListView;
    private DatabaseHelper databaseHelper;
    private SavedArticlesAdapter adapter;
    private ArrayList<String> savedArticles = new ArrayList<>();
    private ArrayList<Integer> articleIds = new ArrayList<>();
    private ArrayList<String> articleSections = new ArrayList<>();
    private ArrayList<String> articleUrls = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_articles, container, false);

        savedArticlesListView = view.findViewById(R.id.savedArticlesListView);
        databaseHelper = new DatabaseHelper(requireContext());

        // Custom adapter using the same ArrayList<String>
        adapter = new SavedArticlesAdapter(requireContext(), savedArticles, articleSections);
        savedArticlesListView.setAdapter(adapter);

        loadSavedArticles();

        // Open article details when clicked
        savedArticlesListView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("id", articleIds.get(position)); // Pass the article ID
            intent.putExtra("title", savedArticles.get(position));
            intent.putExtra("section", articleSections.get(position));
            intent.putExtra("url", articleUrls.get(position));
            startActivity(intent);
        });

        // Remove article from favorites on long press with undo option
        savedArticlesListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            // Store article details before removing
            int articleId = articleIds.get(position);
            String removedTitle = savedArticles.get(position);
            String removedSection = articleSections.get(position);
            String removedUrl = articleUrls.get(position);

            // Delete article from the database
            boolean deleteSuccess = databaseHelper.deleteArticleById(articleId);

            if (deleteSuccess) {
                // Temporarily remove the article from the lists
                savedArticles.remove(position);
                articleIds.remove(position);
                articleSections.remove(position);
                articleUrls.remove(position);
                adapter.notifyDataSetChanged();

                // Show snackbar with undo option
                showSnackbar(view, getString(R.string.snackbarRemoveFavourite), getString(R.string.undo), v -> {
                    // Re-add the article to the database
                    boolean restoreSuccess = databaseHelper.saveArticle(articleId, removedTitle, removedSection, removedUrl);
                    if (restoreSuccess) {
                        loadSavedArticles(); // Reload data to reflect undo
                    } else {
                        showSnackbar(view, getString(R.string.snackbarFailedFavourite), null, null);
                    }
                });
            }
            return true;
        });

        return view;
    }

    private void loadSavedArticles() {
        Cursor cursor = databaseHelper.getSavedArticles();
        savedArticles.clear();
        articleIds.clear();
        articleSections.clear();
        articleUrls.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String section = cursor.getString(cursor.getColumnIndex("section"));
                String url = cursor.getString(cursor.getColumnIndex("url"));

                savedArticles.add(title);
                articleIds.add(id);
                articleSections.add(section);
                articleUrls.add(url);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    private void showSnackbar(View view, String message, String action, View.OnClickListener actionListener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (action != null && actionListener != null) {
            snackbar.setAction(action, actionListener);
        }
        snackbar.show();
    }
}
