package com.example.konsttest2.data;

public class Link {
    private long id;
    private String title;
    private String weblink;
    private int x;
    private int y;

    public long getId() {
        return id;
    }

    public Link setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Link setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getWeblink() {
        return weblink;
    }

    public Link setWeblink(String weblink) {
        this.weblink = weblink;
        return this;
    }

    public int getX() {
        return x;
    }

    public Link setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Link setY(int y) {
        this.y = y;
        return this;
    }
}
