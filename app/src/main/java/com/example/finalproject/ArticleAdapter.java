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

/**
 * Custom adapter for displaying articles in a ListView.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    private Context context;
    private List<Article> articles;

    /**
     * Constructs an ArticleAdapter.
     *
     * @param context  the context in which the adapter is being used
     * @param articles the list of articles to display
     */
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
        this.context = context;
        this.articles = articles;
    }

    /**
     * Provides a view for an AdapterView (ListView) for each article item.
     *
     * @param position    the position of the article in the list
     * @param convertView the recycled view to populate
     * @param parent      the parent ViewGroup that this view will be attached to
     * @return the populated view representing an article
     */
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

        // Set the article details in the corresponding views
        titleView.setText(article.getTitle());
        sectionView.setText(article.getSection());
        thumbnailView.setImageResource(R.drawable.placeholder_image); // Replace with article thumbnail

        return convertView;
    }
}
