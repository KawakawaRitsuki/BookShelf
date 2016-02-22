package com.kawakawaplanning.bookshelf.dojin.model;

/**
 * Created by KP on 16/02/15.
 */
public class DojinBookData {
    private String title;
    private int bookId;
    private String circle;
    private String event;

    public String getCircle() {
        return circle;
    }
    public String getEvent() {
        return event;
    }
    public String getTitle() {
        return title;
    }

    public int getBookId() {
        return bookId;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}


