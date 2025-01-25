package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

/**
 * Fragment to display a list of articles using a ListView.
 */
public class ArticleListFragment extends Fragment {

    private ListView articleListView;
    private ArticleAdapter adapter;
    private ArrayList<Article> articles = new ArrayList<>();

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous state.
     * @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        articleListView = view.findViewById(R.id.articleListView);

        // Initialize the custom adapter
        adapter = new ArticleAdapter(requireContext(), articles);
        articleListView.setAdapter(adapter);

        // Set up the click listener to open the DetailActivity
        articleListView.setOnItemClickListener((parent, view1, position, id) -> {
            // Get the clicked article
            Article article = articles.get(position);

            // Pass article details to the DetailActivity
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("section", article.getSection());
            intent.putExtra("url", article.getUrl());
            startActivity(intent);
        });

        return view;
    }

    /**
     * Updates the list of articles in the fragment.
     *
     * @param titles   The list of article titles.
     * @param sections The list of article sections.
     * @param urls     The list of article URLs.
     */
    public void updateArticles(ArrayList<String> titles, ArrayList<String> sections, ArrayList<String> urls) {
        articles.clear();
        for (int i = 0; i < titles.size(); i++) {
            articles.add(new Article(titles.get(i), sections.get(i), urls.get(i)));
        }
        adapter.notifyDataSetChanged();
    }
}
