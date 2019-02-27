package com.example.konsttest2.data;

public class DesktopItem {
    private long id;
    private String title;
    private String link;
    private int x;
    private int y;
    private int type;

    public int getType() {
        return type;
    }

    public DesktopItem setType(int type) {
        this.type = type;
        return this;
    }

    public long getId() {
        return id;
    }

    public DesktopItem setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DesktopItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLink() {
        return link;
    }

    public DesktopItem setLink(String link) {
        this.link = link;
        return this;
    }

    public int getX() {
        return x;
    }

    public DesktopItem setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public DesktopItem setY(int y) {
        this.y = y;
        return this;
    }
}
