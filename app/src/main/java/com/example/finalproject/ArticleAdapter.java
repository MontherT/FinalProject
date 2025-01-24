package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private Context context;
    private List<Article> articles;

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_article, parent, false);
        }

        Article article = articles.get(position);

        TextView titleView = convertView.findViewById(R.id.article_title);
        TextView sectionView = convertView.findViewById(R.id.article_section);
        ImageView thumbnailView = convertView.findViewById(R.id.article_thumbnail);

        titleView.setText(article.getTitle());
        sectionView.setText(article.getSection());
        thumbnailView.setImageResource(R.drawable.placeholder_image); // Replace with article thumbnail

        return convertView;
    }
}

