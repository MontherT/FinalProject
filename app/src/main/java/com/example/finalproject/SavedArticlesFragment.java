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

/**
 * The SavedArticlesFragment is responsible for displaying a list of saved articles.
 * Users can view the details of an article by clicking on it or delete it with an undo option.
 */
public class SavedArticlesFragment extends Fragment {

    private ListView savedArticlesListView;
    private DatabaseHelper databaseHelper;
    private SavedArticlesAdapter adapter;
    private ArrayList<String> savedArticles = new ArrayList<>();
    private ArrayList<Integer> articleIds = new ArrayList<>();
    private ArrayList<String> articleSections = new ArrayList<>();
    private ArrayList<String> articleUrls = new ArrayList<>();

    /**
     * Inflates the layout for the fragment and sets up the ListView with saved articles.
     *
     * @param inflater           The LayoutInflater used to inflate the layout.
     * @param container          The parent container the fragment UI should be attached to.
     * @param savedInstanceState The saved state of the fragment.
     * @return The root view of the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_articles, container, false);

        savedArticlesListView = view.findViewById(R.id.savedArticlesListView);
        databaseHelper = new DatabaseHelper(requireContext());

        // Custom adapter to display article titles and sections
        adapter = new SavedArticlesAdapter(requireContext(), savedArticles, articleSections);
        savedArticlesListView.setAdapter(adapter);

        loadSavedArticles();

        // Set up click listener to open article details in DetailActivity
        savedArticlesListView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("id", articleIds.get(position));  // Pass the article ID
            intent.putExtra("title", savedArticles.get(position));
            intent.putExtra("section", articleSections.get(position));
            intent.putExtra("url", articleUrls.get(position));
            startActivity(intent);
        });

        // Set up long click listener to remove articles from favorites with undo option
        savedArticlesListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            // Store article details before removal
            int articleId = articleIds.get(position);
            String removedTitle = savedArticles.get(position);
            String removedSection = articleSections.get(position);
            String removedUrl = articleUrls.get(position);

            // Delete the article from the database
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
                        loadSavedArticles();  // Reload data to reflect the undo action
                    } else {
                        showSnackbar(view, getString(R.string.snackbarFailedFavourite), null, null);
                    }
                });
            }
            return true;
        });

        return view;
    }

    /**
     * Loads saved articles from the database and populates the ListView.
     */
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

    /**
     * Displays a Snackbar message with an optional action.
     *
     * @param view            The view to find a parent from.
     * @param message         The message to display in the Snackbar.
     * @param action          The label for the action button (if any).
     * @param actionListener  The listener to invoke when the action is clicked (if any).
     */
    private void showSnackbar(View view, String message, String action, View.OnClickListener actionListener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (action != null && actionListener != null) {
            snackbar.setAction(action, actionListener);
        }
        snackbar.show();
    }
}
