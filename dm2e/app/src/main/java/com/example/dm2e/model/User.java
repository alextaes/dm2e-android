package com.example.dm2e.model;



/**
 *
 * @author Alejandro Taghavi Espinosa
 *
 * Proyecto DM2E
 */


public class User {

    String id, name, email;

    public User() {

    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() { return id; }

}
