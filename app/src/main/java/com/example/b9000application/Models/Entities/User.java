package com.example.b9000application.Models.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class User implements Serializable {
    @Ignore
    public User() {
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String uid;

    private String name;
    @NonNull
    private String email;

    @NonNull
    private boolean valid = true;

    private String userImage;
    public User(@NonNull String uid,@NonNull String email, String name,  String userImage) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.userImage = userImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {

        if(!name.isEmpty())
            this.name= name;
        else
        {
            throw new Exception("Name is not filled!");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if(!email.isEmpty())
            this.email = email;
        else
        {
            throw new Exception("Email is not filled!");
        }

    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) throws Exception {
        if(!userImage.isEmpty())
            this.userImage = userImage;
        else
            throw new Exception("Image is not selected!");
    }


    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
