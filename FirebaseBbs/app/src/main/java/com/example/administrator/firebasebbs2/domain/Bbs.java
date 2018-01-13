package com.example.administrator.firebasebbs2.domain;

/**
 * Created by Administrator on 2017-07-03.
 */

public class Bbs {

    String id;
    String title;
    String author;
    String content;
    String fileUriString;
    long date;
    long count;


    public Bbs() {

    }

    public Bbs(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getFileUriString() {
        return fileUriString;
    }

    public void setFileUriString(String fileUriString) {
        this.fileUriString = fileUriString;
    }

}
