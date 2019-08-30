package com.example.swudoit;

public class Diary {
    public String id;
    public String pic;
    public String title;
    public String content;
    public String date;

    public Diary(){ }

    public Diary(String id, String pic, String title, String content, String date) {
        this.id = id;
        this.pic = pic;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
