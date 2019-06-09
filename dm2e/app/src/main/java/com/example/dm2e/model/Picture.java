package com.example.dm2e.model;



/**
 *
 * @author Alejandro Taghavi Espinosa
 *
 * Proyecto DM2E
 */


public class Picture {

    private String id;
    private String picture;
    private String userName;
    private String title;
    private String description;
    private int likes;
    private String userId;


    public Picture() {

    }

    public Picture(String id, String picture, String userName, String userId, String title, String description) {
        this.id = id;
        this.picture = picture;
        this.userName = userName;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
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
