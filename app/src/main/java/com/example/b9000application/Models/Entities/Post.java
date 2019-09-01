package com.example.b9000application.Models.Entities;

import com.google.firebase.Timestamp;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.model.value.ServerTimestampValue;

import java.io.Serializable;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table", indices = {@Index("userId")})
public class Post implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String postKey;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(@NonNull String postKey) {
        this.postKey = postKey;
    }

    @NonNull
    private String category;
    @NonNull
    private String content;
    @NonNull
    private String picture;
    @NonNull
    private String userId;
    @NonNull
    private String userName;

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(@NonNull String userImage) {
        this.userImage = userImage;
    }

    @NonNull
    private String userImage;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted( boolean deleted) {
        this.deleted = deleted;
    }

    @Ignore
    private boolean deleted = false;
    @NonNull
    private int likes;
    /*
    To change the timestap into data time
    * */
    @ServerTimestamp private Date timestamp;



    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;

    }
    public void updatePost(@NonNull String title, @NonNull String second_title, @NonNull String category, @NonNull String content, @NonNull String picture, @NonNull String userId, @NonNull int likes, @NonNull String userImage, @NonNull String userName ) {
        this.likes = likes;
        this.category = category;
        this.content = content;
        this.picture = picture;
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
    }
    public Post( @NonNull String category, @NonNull String content, @NonNull String picture, @NonNull String userId, @NonNull int likes, @NonNull String userImage, @NonNull String userName ) {
        this.likes = likes;
        this.category = category;
        this.content = content;
        this.picture = picture;
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
    }


    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Ignore
    public Post() {
    }


    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getPicture() {
        return picture;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}
