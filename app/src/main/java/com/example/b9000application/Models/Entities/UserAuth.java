package com.example.b9000application.Models.Entities;

import android.net.Uri;

import androidx.room.Entity;


public class UserAuth {

    String name;
    String email;
    String password;
    Uri userImage;

    public UserAuth(String name, String email, String password, String password2, Uri userImage) throws Exception {
        setName(name);
        setEmail(email);
        setPassword(password);
        vailPass(password2);
        setUserImage(userImage);
    }

    private void vailPass(String password2) throws Exception {
        if (!password.equals(password2)){
            throw new Exception("Password Is Not Match!");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name.isEmpty())
            throw new Exception("Name is not filled!");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (email.isEmpty())
            throw new Exception("Email is not filled!");
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if (password.isEmpty())
            throw new Exception("Password is not filled!");
        this.password = password;
    }

    public Uri getUserImage() {
        return userImage;
    }

    public void setUserImage(Uri userImage) throws Exception {
        if (userImage==null)
            throw new Exception("UserAuth's Image is not selected!");
        this.userImage = userImage;
    }


}
