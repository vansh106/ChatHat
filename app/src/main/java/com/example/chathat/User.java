package com.example.chathat;

public class User {
    String image,Username;

    public User(String image, String Username) {
        this.image = image;
        this.Username = Username;
    }

    public User() {
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return Username;
    }



    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

}
