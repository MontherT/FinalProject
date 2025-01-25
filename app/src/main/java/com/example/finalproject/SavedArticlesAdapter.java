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

/**
 * The SavedArticlesAdapter is a custom ArrayAdapter that binds saved articles to a ListView.
 * It displays the article title, section, and a placeholder image for each item.
 */
public class SavedArticlesAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> titles;
    private final List<String> sections;

    /**
     * Constructs a new SavedArticlesAdapter.
     *
     * @param context  The context of the current state of the application/object.
     * @param titles   A list of article titles to display.
     * @param sections A list of corresponding article sections to display.
     */
    public SavedArticlesAdapter(Context context, List<String> titles, List<String> sections) {
        super(context, 0, titles);
        this.context = context;
        this.titles = titles;
        this.sections = sections;
    }

    /**
     * Provides a view for an AdapterView (ListView) using the specified position from the dataset.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view that this view will be attached to.
     * @return The view for the corresponding data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.saved_articles_list_item, parent, false);
        }

        // Retrieve UI components for the list item
        TextView titleView = convertView.findViewById(R.id.saved_article_title);
        TextView sectionView = convertView.findViewById(R.id.saved_article_section);
        ImageView thumbnailView = convertView.findViewById(R.id.saved_article_thumbnail);

        // Set values for title, section, and thumbnail
        titleView.setText(titles.get(position));
        sectionView.setText(sections.get(position));
        thumbnailView.setImageResource(R.drawable.placeholder_image); // Replace with actual thumbnail logic if needed

        return convertView;
    }
}
