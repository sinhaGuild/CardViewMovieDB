package com.example.cardviewdemo.detail;

/**
 * Created by anuragsinha on 16-05-28.
 */
public class Reviews {

    public static final String EMPTY = "Empty";

    private String id;//"id": "535868070e0a26067f0007d2",
    private String author;//"author": "Raquel98",
    private String content;//"content": "This movie is very good I loved it so much.",
    private String url;//  "url": "https://www.themoviedb.org/review/535868070e0a26067f0007d2"

    public Reviews() {
        id = EMPTY;
        author = EMPTY;
        content = EMPTY;
        url = EMPTY;
    }

    public Reviews(String author, String content) {


        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return "- By " + author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
