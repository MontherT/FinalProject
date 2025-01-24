package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic to add the viewed_articles table for version 2
        if (oldVersion < 2) {
            String createViewedArticlesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_VIEWED + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_SECTION + " TEXT, " +
                    COLUMN_URL + " TEXT)";
            db.execSQL(createViewedArticlesTable);
        }
    }

    // Save an article to the saved_articles table
    public boolean saveArticle(int id, String title, String section, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id); // Use the provided ID
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SECTION, section);
        values.put(COLUMN_URL, url);

        long result = db.insert(TABLE_NAME_SAVED, null, values);
        return result != -1; // Returns true if the insert was successful
    }

    // Check if an article is saved by its ID
    public boolean isArticleSavedById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM saved_articles WHERE id = ?", new String[]{String.valueOf(id)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();

        // Debug Log
        Log.d("DatabaseHelper", "isArticleSavedById: Article ID " + id + " saved: " + exists);

        return exists;
    }


    // Delete an article by its ID
    public boolean deleteArticleById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_SAVED, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Get all saved articles
    public Cursor getSavedArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_SAVED, null);
    }

    // Get the ID of the last inserted article
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
        return -1; // Return -1 if no articles are found
    }

    // Save a viewed article to the viewed_articles table
    public boolean saveToHistory(String title, String section, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SECTION, section);
        values.put(COLUMN_URL, url);

        long result = db.insert(TABLE_NAME_VIEWED, null, values);
        return result != -1; // Returns true if the insert was successful
    }

    // Get all viewed articles
    public Cursor getHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_VIEWED, null);
    }

    // Clear all history
    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_VIEWED);
    }
}
