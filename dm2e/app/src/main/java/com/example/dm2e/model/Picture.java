package com.example.dm2e.model;

public class Picture {

    private String id;
    private String picture;
    private String userName;
    private String title;
    private String description;
    private int likes;


    public Picture() {

    }

    public Picture(String id, String picture, String userName, String title, String description) {
        this.id = id;
        this.picture = picture;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.likes = 0;
    }

    public String getId() {
        return id;
    }
    public String getPicture() {
        return picture;
    }


    public String getUserName() {
        return userName;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public int getLikes() {
        return likes;
    }

    public void addLikes() {
        this.likes += 1;
    }
    public void substractLikes() {
        this.likes -= 1;
    }
}
