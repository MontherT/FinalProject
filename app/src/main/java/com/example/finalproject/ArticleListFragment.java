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

public class ArticleListFragment extends Fragment {

    private ListView articleListView;
    private ArticleAdapter adapter;
    private ArrayList<Article> articles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        articleListView = view.findViewById(R.id.articleListView);

        // Initialize the custom adapter
        adapter = new ArticleAdapter(requireContext(), articles);
        articleListView.setAdapter(adapter);

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

    public void updateArticles(ArrayList<String> titles, ArrayList<String> sections, ArrayList<String> urls) {
        articles.clear();
        for (int i = 0; i < titles.size(); i++) {
            articles.add(new Article(titles.get(i), sections.get(i), urls.get(i)));
        }
        adapter.notifyDataSetChanged();
    }
}
