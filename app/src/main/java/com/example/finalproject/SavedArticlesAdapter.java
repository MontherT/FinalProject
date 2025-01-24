package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class SavedArticlesAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> titles;
    private final List<String> sections;

    public SavedArticlesAdapter(Context context, List<String> titles, List<String> sections) {
        super(context, 0, titles);
        this.context = context;
        this.titles = titles;
        this.sections = sections;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.saved_articles_list_item, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.saved_article_title);
        TextView sectionView = convertView.findViewById(R.id.saved_article_section);
        ImageView thumbnailView = convertView.findViewById(R.id.saved_article_thumbnail);

        titleView.setText(titles.get(position));
        sectionView.setText(sections.get(position));
        thumbnailView.setImageResource(R.drawable.placeholder_image); // Replace with actual thumbnail logic if needed

        return convertView;
    }
}
