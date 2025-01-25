package com.example.finalproject;

/**
 * Represents an article with a title, section, and URL.
 */
public class Article {

    private String title;
    private String section;
    private String url;

    /**
     * Constructs an Article object.
     *
     * @param title   the title of the article
     * @param section the section of the article
     * @param url     the URL of the article
     */
    public Article(String title, String section, String url) {
        this.title = title;
        this.section = section;
        this.url = url;
    }

    /**
     * Gets the title of the article.
     *
     * @return the article title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the section of the article.
     *
     * @return the article section
     */
    public String getSection() {
        return section;
    }

    /**
     * Gets the URL of the article.
     *
     * @return the article URL
     */
    public String getUrl() {
        return url;
    }
}
