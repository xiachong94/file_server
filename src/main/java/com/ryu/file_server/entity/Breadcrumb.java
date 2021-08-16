package com.ryu.file_server.entity;

public class Breadcrumb {

    private String link;
    private String text;

    public Breadcrumb(String link, String text) {
        this.link = link;
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
