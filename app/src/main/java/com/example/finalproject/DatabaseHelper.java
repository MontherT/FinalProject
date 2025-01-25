package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A helper class to manage the application's SQLite database for storing saved and viewed articles.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 2; // Incremented to version 2

    // Table for saved articles
    private static final String TABLE_NAME_SAVED = "saved_articles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SECTION = "section";
    private static final String COLUMN_URL = "url";

    // Table for viewed articles
    private static final String TABLE_NAME_VIEWED = "viewed_articles";

    /**
     * Constructor for DatabaseHelper.
     *
     * @param context The application context.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create saved_articles table
        String createSavedArticlesTable = "CREATE TABLE " + TABLE_NAME_SAVED + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_SECTION + " TEXT, " +
                COLUMN_URL + " TEXT)";
        db.execSQL(createSavedArticlesTable);

        // Create viewed_articles table
        String createViewedArticlesTable = "CREATE TABLE " + TABLE_NAME_VIEWED + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_SECTION + " TEXT, " +
                COLUMN_URL + " TEXT)";
        db.execSQL(createViewedArticlesTable);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String createViewedArticlesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_VIEWED + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_SECTION + " TEXT, " +
                    COLUMN_URL + " TEXT)";
            db.execSQL(createViewedArticlesTable);
        }
    }

    /**
     * Saves an article to the saved_articles table.
     *
     * @param id      The article ID.
     * @param title   The article title.
     * @param section The article section.
     * @param url     The article URL.
     * @return True if the article was successfully saved, false otherwise.
     */
    public boolean saveArticle(int id, String title, String section, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SECTION, section);
        values.put(COLUMN_URL, url);

        long result = db.insert(TABLE_NAME_SAVED, null, values);
        return result != -1;
    }

    /**
     * Checks if an article is saved by its ID.
     *
     * @param id The article ID.
     * @return True if the article is saved, false otherwise.
     */
    public boolean isArticleSavedById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM saved_articles WHERE id = ?", new String[]{String.valueOf(id)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();

        Log.d("DatabaseHelper", "isArticleSavedById: Article ID " + id + " saved: " + exists);
        return exists;
    }

    /**
     * Deletes an article from the saved_articles table by its ID.
     *
     * @param id The article ID.
     * @return True if the article was successfully deleted, false otherwise.
     */
    public boolean deleteArticleById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_SAVED, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    /**
     * Retrieves all saved articles.
     *
     * @return A Cursor containing all saved articles.
     */
    public Cursor getSavedArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_SAVED, null);
    }

    /**
     * Retrieves the ID of the last inserted article.
     *
     * @return The ID of the last inserted article, or -1 if no articles are found.
     */
    public int getLastInsertedId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_NAME_SAVED + " ORDER BY id DESC LIMIT 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
            return id;
        }

        if (cursor != null) {
            cursor.close();
        }
        return -1;
    }

    /**
     * Saves a viewed article to the viewed_articles table.
     *
     * @param title   The article title.
     * @param section The article section.
     * @param url     The article URL.
     * @return True if the article was successfully saved, false otherwise.
     */
    public boolean saveToHistory(String title, String section, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SECTION, section);
        values.put(COLUMN_URL, url);

        long result = db.insert(TABLE_NAME_VIEWED, null, values);
        return result != -1;
    }

    /**
     * Retrieves all viewed articles from the history.
     *
     * @return A Cursor containing all viewed articles.
     */
    public Cursor getHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_VIEWED, null);
    }

    /**
     * Clears all viewed articles from the history.
     */
    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_VIEWED);
    }
}
