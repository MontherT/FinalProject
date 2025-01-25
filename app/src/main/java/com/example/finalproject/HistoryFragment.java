package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Fragment for displaying and managing the user's browsing history.
 */
public class HistoryFragment extends Fragment {

    private ListView historyListView;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> historyTitles = new ArrayList<>();
    private ArrayList<String> historySections = new ArrayList<>();
    private ArrayList<String> historyUrls = new ArrayList<>();

    /**
     * Inflates the layout and initializes the view components for the history fragment.
     *
     * @param inflater LayoutInflater to inflate the fragment layout.
     * @param container ViewGroup that contains the fragment UI.
     * @param savedInstanceState Bundle containing the saved state of the fragment.
     * @return The inflated view of the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        historyListView = view.findViewById(R.id.historyListView);
        databaseHelper = new DatabaseHelper(requireContext());

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, historyTitles);
        historyListView.setAdapter(adapter);

        loadHistory();

        // Set up click listener to open articles in DetailActivity
        historyListView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("title", historyTitles.get(position));
            intent.putExtra("section", historySections.get(position));
            intent.putExtra("url", historyUrls.get(position));
            startActivity(intent);
        });

        return view;
    }

    /**
     * Loads the browsing history from the database and updates the ListView.
     */
    public void loadHistory() {
        Cursor cursor = databaseHelper.getHistory();
        historyTitles.clear();
        historySections.clear();
        historyUrls.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String section = cursor.getString(cursor.getColumnIndex("section"));
                String url = cursor.getString(cursor.getColumnIndex("url"));

                historyTitles.add(title);
                historySections.add(section);
                historyUrls.add(url);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Clears the browsing history from the database and updates the UI.
     */
    public void clearHistory() {
        databaseHelper.clearHistory();
        historyTitles.clear();
        historySections.clear();
        historyUrls.clear();
        adapter.notifyDataSetChanged();
        if (getView() != null) {
            Snackbar.make(getView(), getString(R.string.snackbarHistory), Snackbar.LENGTH_SHORT).show();
        }
    }
}
